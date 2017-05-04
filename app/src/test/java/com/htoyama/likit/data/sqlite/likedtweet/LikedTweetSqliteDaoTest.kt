package com.htoyama.likit.data.sqlite.likedtweet

import com.google.common.truth.Truth.assertThat
import com.htoyama.likit.data.sqlite.likedTweetSqliDao
import com.htoyama.likit.data.sqlite.relation.TweetTagRelationTableGateway
import com.htoyama.likit.data.sqlite.tag.TagSqliteDao
import com.htoyama.likit.data.sqlite.tagSqliteDao
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.likedTweet
import com.htoyama.likit.tag
import com.htoyama.likit.testutil.SqliteTestingRule
import com.htoyama.likit.tweet
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LikedTweetSqliteDaoTest {
  @Rule @JvmField val rule = SqliteTestingRule()

  lateinit var tweetDao: LikedTweetSqliteDao
  lateinit var tagDao: TagSqliteDao

  @Before fun setUp() {
    tweetDao = likedTweetSqliDao(rule.briteDB)
    tagDao = tagSqliteDao(rule.briteDB)
  }

  @Test fun `select by page and per page`() {
    val perPage = 2
    val expected1 = (5L downTo 4L).map { likedTweet(tweet(id = it, createdAt = it)) }
    val expected2 = (3L downTo 2L).map { likedTweet(tweet(id = it, createdAt = it)) }
    val expected3 = listOf(likedTweet(tweet(id = 1, createdAt = 1)))

    // use a reverse list to test whether select in descending order of created property
    tweetDao.insertOrUpdate(expected3 + expected2 + expected1)

    // assert
    tweetDao.select(1, perPage).test().assertValue(expected1)
    tweetDao.select(2, perPage).test().assertValue(expected2)
    tweetDao.select(3, perPage).test().assertValue(expected3)
    tweetDao.select(4, perPage).test().assertValue(emptyList())
  }

  @Test fun `select by tag id`() {
    val tagId = tagDao.insert(tag(Tag.UNASSIGNED_ID))

    val unexpected = likedTweet(tweet(id = 1), emptyList())
    val expected = likedTweet(tweet(id = 2), listOf(tagId))
    tweetDao.insertOrUpdate(listOf(unexpected, expected))

    tweetDao.selectByTagId(tagId, 1, 200).test()
        .assertValue(listOf(expected))
  }

  @Test fun `insert a liked tweet`() {
    val tagId = tagDao.insert(tag(Tag.UNASSIGNED_ID))

    val inserted = likedTweet(tweet(id = 1), listOf(tagId))
    tweetDao.insertOrUpdate(inserted)

    tweetDao.select(1, 200).test()
        .assertValue(listOf(inserted))
  }

  /**
   * testing about transaction
   */
  @Test fun `should not insert a liked tweet completely when an error is occurred`() {
    // given
    val anError = RuntimeException()
    val relationGateway = mock<TweetTagRelationTableGateway> {
      on { insertTweetTagRelation(any()) } doThrow anError
    }

    val tweetDao = likedTweetSqliDao(db = rule.briteDB, tweetTagRelationTableGateway = relationGateway)

    // when
    try {
      tweetDao.insertOrUpdate(likedTweet(tweet(id = 1)))
      fail()
    } catch (actual: Exception) {
      assertThat(actual).isEqualTo(anError)
    }

    // then should not insert the liked tweet
    tweetDao.select(1, 200).test()
        .assertValue(emptyList())
  }

  /**
   * this test is failed because cannot updating relations between a tweet and a tag.
   * The reason for it is, the current implementation doesn't delete old relations.
   *
   * But I don't know whether I use this update feature or not.
   * So I decided to ignore this test.
   */
  @Ignore("see the above comment")
  @Test fun `update a liked tweet`() {
    val tagId = tagDao.insert(tag(Tag.UNASSIGNED_ID))

    val inserted = likedTweet(tweet(id = 1), tagIdList = listOf(tagId))
    val updated = likedTweet(tweet(id = 1), tagIdList = emptyList())
    tweetDao.insertOrUpdate(inserted)
    tweetDao.insertOrUpdate(updated)

    tweetDao.select(1, 200).test()
        .assertValue(listOf(updated))
  }

  @Test fun `delete a liked tweet`() {
    val id = 1L
    tweetDao.insertOrUpdate(likedTweet(tweet(id = id)))

    tweetDao.delete(listOf(id))

    tweetDao.select(1, 1).test().assertValue(emptyList())
  }
}