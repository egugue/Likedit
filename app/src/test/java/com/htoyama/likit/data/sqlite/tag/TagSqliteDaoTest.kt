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

  @Suppress("UNUSED_VARIABLE")
  @Test fun `search tag by name`() {
    val searchWord = "B%_B"

    val id1 = tagDao.insert(tag(name = "bb%_bbbbaacc", id = Tag.UNASSIGNED_ID)) // hit
    val id2 = tagDao.insert(tag(name = "baaccb%_b", id = Tag.UNASSIGNED_ID)) // hit
    val id3 = tagDao.insert(tag(name = "aaabbbccc", id = Tag.UNASSIGNED_ID))
    val id4 = tagDao.insert(tag(name = "aaaBBBccc", id = Tag.UNASSIGNED_ID))
    val id5 = tagDao.insert(tag(name = "aaaB%_Bccc", id = Tag.UNASSIGNED_ID)) // hit
    val id6 = tagDao.insert(tag(name = "abc", id = Tag.UNASSIGNED_ID))
    val id7 = tagDao.insert(tag(name = "aabbc", id = Tag.UNASSIGNED_ID))
    val id8 = tagDao.insert(tag(name = "あああいいいううう", id = Tag.UNASSIGNED_ID))

    val actual = tagDao.searchTagBy(searchWord).test()

    // order by name
    actual.assertValue(listOf(
        tag(id5),
        tag(id2),
        tag(id1)
    ))
  }

  @Suppress("UNUSED_VARIABLE")
  @Test fun `search tag by multibyte name`() {
    val searchWord = "あああ"
    val id1 = tagDao.insert(tag(name = "あああいいいううう", id = Tag.UNASSIGNED_ID))
    val id2 = tagDao.insert(tag(name = "いいいいいいあああいいいううう", id = Tag.UNASSIGNED_ID))
    val id3 = tagDao.insert(tag(name = "いいあああ", id = Tag.UNASSIGNED_ID))
    val id4 = tagDao.insert(tag(name = "いいああ", id = Tag.UNASSIGNED_ID))
    val id5 = tagDao.insert(tag(name = "ああいいうう", id = Tag.UNASSIGNED_ID))
    val id6 = tagDao.insert(tag(name = "あｓｆｄｓｆｄｆｓ", id = Tag.UNASSIGNED_ID))

    val actual = tagDao.searchTagBy(searchWord).test()

    // order by name
    actual.assertValue(listOf(
        tag(id1),
        tag(id3),
        tag(id2)
    ))
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

  @Test fun `not insert a tag when the tag's id is a specific one`() {
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

  @Test fun `not update a tag if the tag's id is invalid`() {
    val invalidId = Tag.UNASSIGNED_ID
    try {
      tagDao.updateName(tag(id = invalidId))
    } catch (actual: IllegalArgumentException) {
      assertThat(actual).hasMessageThat().isEqualTo("tag id must not be TAG.UNASSIGNED_ID")
    }
  }

  @Test fun `delete a tag`() {
    val newlyAssignedId = tagDao.insert(tag(id = Tag.UNASSIGNED_ID))
    tagDao.deleteById(newlyAssignedId)

    tagDao.selectTagBy(newlyAssignedId).test()
        .assertErrorMessage("No such tag which has id($newlyAssignedId)")
  }

}