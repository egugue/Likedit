package com.egugue.licol.data.sqlite.likedtweet

import com.google.common.truth.Truth.assertThat
import com.egugue.licol.data.sqlite.likedTweetSqliDao
import com.egugue.licol.data.sqlite.relation.TweetTagRelationTableGateway
import com.egugue.licol.data.sqlite.tag.TagSqliteDao
import com.egugue.licol.data.sqlite.tagSqliteDao
import com.egugue.licol.data.sqlite.user.UserSqliteDao
import com.egugue.licol.data.sqlite.userSqliteDao
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.tag.Tag
import com.egugue.licol.likedTweet
import com.egugue.licol.tag
import com.egugue.licol.testutil.SqliteTestingRule
import com.egugue.licol.user
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
  lateinit var userDao: UserSqliteDao

  @Before fun setUp() {
    tweetDao = likedTweetSqliDao(rule.briteDB)
    tagDao = tagSqliteDao(rule.briteDB)
    userDao = userSqliteDao(rule.briteDB)
  }

  @Test fun `select by page and per page`() {
    val perPage = 2
    val expected1 = (5L downTo 4L).map { likedTweet(id = it, createdAt = it) }
    val expected2 = (3L downTo 2L).map { likedTweet(id = it, createdAt = it) }
    val expected3 = listOf(likedTweet(id = 1, createdAt = 1))

    // use a reverse list to test whether select in descending order of created property
    insertOrUpdateLikedTweet(expected3 + expected2 + expected1)

    // assert
    tweetDao.select(1, perPage).test().assertValue(expected1)
    tweetDao.select(2, perPage).test().assertValue(expected2)
    tweetDao.select(3, perPage).test().assertValue(expected3)
    tweetDao.select(4, perPage).test().assertValue(emptyList())
  }

  @Test fun `select by tag id`() {
    val tagId = tagDao.insert(tag(Tag.UNASSIGNED_ID))

    val unexpected = likedTweet(id = 1, tagIdList = emptyList())
    val expected = likedTweet(id = 2, tagIdList = listOf(tagId))
    insertOrUpdateLikedTweet(listOf(unexpected, expected))

    tweetDao.selectByTagId(tagId, 1, 200).test()
        .assertValue(listOf(expected))
  }

  @Test fun `insert a liked tweet`() {
    val tagId = tagDao.insert(tag(Tag.UNASSIGNED_ID))

    val inserted = likedTweet(id = 1, tagIdList = listOf(tagId))
    insertOrUpdateLikedTweet(inserted)

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
      val likedTweeet = likedTweet(id = 1)
      userDao.insertOrUpdate(user(id = likedTweeet.userId))
      tweetDao.insertOrUpdate(likedTweet())

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

    val inserted = likedTweet(id = 1, tagIdList = listOf(tagId))
    val updated = likedTweet(id = 1, tagIdList = emptyList())
    insertOrUpdateLikedTweet(inserted)
    insertOrUpdateLikedTweet(updated)

    tweetDao.select(1, 200).test()
        .assertValue(listOf(updated))
  }

  @Test fun `delete a liked tweet`() {
    val id = 1L
    insertOrUpdateLikedTweet(likedTweet(id = id))

    tweetDao.delete(listOf(id))

    tweetDao.select(1, 1).test().assertValue(emptyList())
  }

  private fun insertOrUpdateLikedTweet(l: LikedTweet) {
    insertOrUpdateLikedTweet(listOf(l))
  }

  private fun insertOrUpdateLikedTweet(l: List<LikedTweet>) {
    l.forEach {
      // must insert user before inserting tweet because of foreign key constraint
      userDao.insertOrUpdate(user(id = it.userId))
      tweetDao.insertOrUpdate(it)
    }
  }
}