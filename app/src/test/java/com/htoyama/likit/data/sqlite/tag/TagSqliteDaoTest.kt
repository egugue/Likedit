package com.htoyama.likit.data.sqlite.tag

import com.google.common.truth.Truth.assertThat
import com.htoyama.likit.data.sqlite.likedTweetSqliDao
import com.htoyama.likit.data.sqlite.likedtweet.LikedTweetSqliteDao
import com.htoyama.likit.data.sqlite.tagSqliteDao
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.likedTweet
import com.htoyama.likit.tag
import com.htoyama.likit.testutil.SqliteTestingRule
import com.htoyama.likit.tweet
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TagSqliteDaoTest {

  @Rule @JvmField val rule = SqliteTestingRule()

  lateinit var tweetDao: LikedTweetSqliteDao
  lateinit var tagDao: TagSqliteDao

  @Before fun setUp() {
    tweetDao = likedTweetSqliDao(rule.briteDB)
    tagDao = tagSqliteDao(rule.briteDB)
  }

  @Test fun `insert a tag`() {
    tweetDao.insertOrUpdate(likedTweet(tweet(id = 10)))

    val insertedTag = tag(id = Tag.UNASSIGNED_ID, tweetIdList = listOf(10))
    val newlyAssigendId = tagDao.insert(insertedTag)

    val actual = tagDao.selectTagBy(newlyAssigendId).blockingFirst()
    actual.run {
      assertThat(id).isEqualTo(newlyAssigendId)
      assertThat(name).isEqualTo(insertedTag.name)
      assertThat(createdAt).isEqualTo(insertedTag.createdAt)
      assertThat(tweetIdList).isEqualTo(insertedTag.tweetIdList)
    }
  }

  @Test fun `throw an exception when inserting a tag with a specific id`() {
    try {
      tagDao.insert(tag(id = 1))
    } catch(e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("tag id must be TAG.UNASSIGNED_ID. but was 1")
    }
  }

  @Test fun `update a tag`() {
    tweetDao.insertOrUpdate(likedTweet(tweet(id = 10)))

    val insertedTag = tag(id = Tag.UNASSIGNED_ID, tweetIdList = listOf(10))
    val newlyAssignedId = tagDao.insert(insertedTag)

    val updatedTag = tag(id = newlyAssignedId, name = "updated", tweetIdList = emptyList())
    tagDao.updateName(updatedTag)

    val actual = tagDao.selectTagBy(newlyAssignedId).blockingFirst()
    actual.run {
      assertThat(name).isEqualTo(updatedTag.name)
      assertThat(tweetIdList).isEqualTo(updatedTag.tweetIdList)
    }
  }
}