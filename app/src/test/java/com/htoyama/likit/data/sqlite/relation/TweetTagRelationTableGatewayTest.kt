package com.htoyama.likit.data.sqlite.relation

import android.database.sqlite.SQLiteConstraintException
import com.google.common.truth.Truth.assertThat
import com.htoyama.likit.data.sqlite.*
import com.htoyama.likit.data.sqlite.tag.TagTableGateway
import com.htoyama.likit.data.sqlite.likedtweet.LikedTweetTableGateway
import com.htoyama.likit.testutil.SqliteTestingRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TweetTagRelationTableGatewayTest {
  @Rule @JvmField val rule = SqliteTestingRule()
  lateinit var gateway: TweetTagRelationTableGateway
  lateinit var tagGateway: TagTableGateway
  lateinit var tweetGateway: LikedTweetTableGateway

  @Before fun setUp() {
    gateway = tweetTagRelationTableGateway(rule.briteDB)
    tagGateway = tagTableGateway(rule.briteDB)
    tweetGateway = likedTweetTableGateway(rule.briteDB)
  }

  /*********************
   * about select method
   *********************/

  @Test fun `select relations related to the given tweet id list`() {
    // given
    tweetGateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = 1),
        fullTweetEntity(id = 2)))

    val tagId1 = tagGateway.insertTag("id-1", 1)
    val tagId2 = tagGateway.insertTag("id-2", 2)

    gateway.insertTweetTagRelation(listOf(
        tweetTagRelation(1, tagId1),
        tweetTagRelation(1, tagId2),
        tweetTagRelation(2, tagId2)))

    // when
    val relations = gateway.selectRelationsByTweetIdList(listOf(1, 2)).blockingFirst()

    // then
    assertThat(relations).containsExactly(
        tweetTagRelation(tweetId = 1, tagId = tagId1),
        tweetTagRelation(tweetId = 1, tagId = tagId2),
        tweetTagRelation(tweetId = 2, tagId = tagId2)
    )
  }

  @Test fun `throw an exception when tweet id list is empty`() {
    try {
      gateway.selectRelationsByTweetIdList(emptyList())
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("the given list must not be empty")
    }
  }

  @Test fun `select relations related to the given tag id list`() {
    // given
    tweetGateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = 1),
        fullTweetEntity(id = 2)))

    val tagId1 = tagGateway.insertTag("id-1", 1)
    val tagId2 = tagGateway.insertTag("id-2", 2)

    gateway.insertTweetTagRelation(listOf(
        tweetTagRelation(1, tagId1),
        tweetTagRelation(1, tagId2),
        tweetTagRelation(2, tagId2)
    ))

    // when
    val relations = gateway.selectRelationsByTagIdList(listOf(tagId1, tagId2)).blockingFirst()

    // then
    assertThat(relations).containsExactly(
        tweetTagRelation(tweetId = 1, tagId = tagId1),
        tweetTagRelation(tweetId = 1, tagId = tagId2),
        tweetTagRelation(tweetId = 2, tagId = tagId2)
    )
  }

  @Test fun `throw an exception when tag id list is empty`() {
    try {
      gateway.selectRelationsByTagIdList(emptyList())
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("the given list must not be empty")
    }
  }


  /************************
   * about insert method
   *************************/

  @Test fun `insert relations`() {
    // given
    tweetGateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = 1),
        fullTweetEntity(id = 2)))

    val tagId = tagGateway.insertTag("id-1", 1)

    // when
    gateway.insertTweetTagRelation(listOf(
        tweetTagRelation(tweetId = 1, tagId = tagId),
        tweetTagRelation(tweetId = 2, tagId = tagId)
    ))

    // then
    val relations = gateway.selectAllTweetTagRelations().test()
    relations.assertValue(listOf(
        tweetTagRelation(1, tagId),
        tweetTagRelation(2, tagId)
    ))
  }

  @Test fun `throw an exception when a tag doesn't be inserted`() {
    // given
    tweetGateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = 1)))

    // a tag doesn't be inserted here
    val invalidTagId = 1L

    // when
    try {
      gateway.insertTweetTagRelation(listOf(
          tweetTagRelation(tweetId = 1, tagId = invalidTagId)
      ))
      fail()
    } catch (e: SQLiteConstraintException) {
    }
  }

  @Test fun `throw an exception when a tweet doesn't be inserted`() {
    // given
    // a tweet doesn't be inserted here
    val invalidTweetId = 1L
    val tagId = tagGateway.insertTag("id-1", 1)

    // when
    try {
      gateway.insertTweetTagRelation(listOf(
          tweetTagRelation(invalidTweetId, tagId)))
      fail()
    } catch (e: SQLiteConstraintException) {
    }
  }


  /************************
   * about delete method
   *************************/

  @Test fun `delete relations`() {
    // given
    val deletingId = 1L
    tweetGateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = deletingId),
        fullTweetEntity(id = 2)))

    val tagId = tagGateway.insertTag("id-1", 1)

    gateway.insertTweetTagRelation(listOf(
        tweetTagRelation(deletingId, tagId),
        tweetTagRelation(2, tagId)))

    // when
    gateway.deleteTweetTagRelation(listOf(tweetTagRelation(deletingId, tagId)))

    // then
    val rels = gateway.selectAllTweetTagRelations().blockingFirst()
    assertThat(rels).doesNotContain(tweetTagRelation(deletingId, tagId))
  }


  /************************
   * about delete cascade
   *************************/

  /**
   * Because of defining "ON DELETE CASCADE" on CREATE statement,
   * it should delete relations when a tag is deleted.
   */
  @Test fun `delete relations automatically when a tag is deleted`() {
    // given
    tweetGateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = 1),
        fullTweetEntity(id = 2))
    )
    val deletingId = tagGateway.insertTag("will delete", 1)
    val tagId1 = tagGateway.insertTag("a tag 1", 1)

    gateway.insertTweetTagRelation(listOf(
        tweetTagRelation(1, deletingId),
        tweetTagRelation(2, deletingId),
        tweetTagRelation(1, tagId1),
        tweetTagRelation(2, tagId1)))

    // when
    tagGateway.deleteTagById(deletingId)
    val rels = gateway.selectAllTweetTagRelations().blockingFirst()

    // then
    assertThat(rels).containsNoneOf(
        tweetTagRelation(tweetId = 1, tagId = deletingId),
        tweetTagRelation(tweetId = 2, tagId = deletingId))
  }

  /**
   * Because of defining "ON DELETE CASCADE" on CREATE statement,
   * it should delete relations when a tweet is deleted.
   */
  @Test fun `delete relations automatically when a tweet is deleted`() {
    // given
    val deletingId = 1L
    tweetGateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = deletingId),
        fullTweetEntity(id = 2))
    )
    val tagId1 = tagGateway.insertTag("a tag 1", 1)
    val tagId2 = tagGateway.insertTag("a tag 2", 1)

    gateway.insertTweetTagRelation(listOf(
        tweetTagRelation(deletingId, tagId1),
        tweetTagRelation(2, tagId1),
        tweetTagRelation(deletingId, tagId2),
        tweetTagRelation(2, tagId2)))

    // when
    tweetGateway.deleteTweetById(deletingId)
    val rels = gateway.selectAllTweetTagRelations().blockingFirst()

    // then
    assertThat(rels).containsNoneOf(
        tweetTagRelation(tweetId = deletingId, tagId = tagId1),
        tweetTagRelation(tweetId = deletingId, tagId = tagId2))
  }

}