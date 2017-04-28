package com.htoyama.likit.data.sqlite.relation

import android.database.sqlite.SQLiteConstraintException
import com.google.common.truth.Truth.assertThat
import com.htoyama.likit.data.sqlite.briteDatabaseForTest
import com.htoyama.likit.data.sqlite.fullTweetEntity
import com.htoyama.likit.data.sqlite.tag.TagTableGateway
import com.htoyama.likit.data.sqlite.likedtweet.LikedTweetTableGateway
import com.htoyama.likit.data.sqlite.tweetTagRelation
import com.squareup.sqlbrite.BriteDatabase
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class TweetTagRelationTableGatewayTest {
  lateinit var gateway: TweetTagRelationTableGateway
  lateinit var tagGateway: TagTableGateway
  lateinit var tweetGateway: LikedTweetTableGateway
  lateinit var db: BriteDatabase

  @Before fun setUp() {
    db = briteDatabaseForTest()
    gateway = TweetTagRelationTableGateway(db)
    tagGateway = TagTableGateway(db)
    tweetGateway = LikedTweetTableGateway(db)
  }

  @After fun tearDown() {
    db.close()
    RuntimeEnvironment.application.deleteDatabase("likedit")
  }

  @Test fun shouldInsertTweetTagRelations() {
    // given
    tweetGateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = 1),
        fullTweetEntity(id = 2)))
        .subscribe()

    val tagId = tagGateway.insertTag("id-1", 1).blockingGet()

    // when
    gateway.insertTweetTagRelation(listOf(1, 2), tagId).subscribe()

    // then
    val relations = gateway.selectAllTweetTagRelations().test()
    relations.assertValue(listOf(
        tweetTagRelation(1, tagId),
        tweetTagRelation(2, tagId)
    ))
  }

  @Test fun shouldNotInsertTweetTagRelations_whenNoTagInserted() {
    // given
    tweetGateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = 1),
        fullTweetEntity(id = 2)))
        .subscribe()
    // not insert
    // val tagId = gateway.insertTag("id-1", 1)
    val invalidTagId = 1L

    // when
    gateway.insertTweetTagRelation(listOf(1, 2), invalidTagId).test()
        .assertError(SQLiteConstraintException::class.java)
  }

  @Test fun shouldNotInsertTweetTagRelations_whenNoTweetInserted() {
    // given
    /* not insert
    gateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = 1),
        fullTweetEntity(id = 2)))
        */

    val tagId = tagGateway.insertTag("id-1", 1).blockingGet()

    // when
    gateway.insertTweetTagRelation(listOf(1), tagId).test()
        .assertError(SQLiteConstraintException::class.java)
  }

  @Test fun shouldDeleteTweetTagRelations() {
    // given
    val deletingId = 1L
    tweetGateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = deletingId),
        fullTweetEntity(id = 2)))

    val tagId = tagGateway.insertTag("id-1", 1).blockingGet()
    gateway.insertTweetTagRelation(listOf(deletingId, 2), tagId)

    // when
    gateway.deleteTweetTagRelation(listOf(deletingId), tagId).subscribe()

    // then
    val rels = gateway.selectAllTweetTagRelations().test().values().first()
    assertThat(rels).doesNotContain(tweetTagRelation(deletingId, tagId))
  }

  /**
   * Because of defining "ON DELETE CASCADE" on CREATE statement,
   * it should delete relations when a tag is deleted.
   */
  @Test fun shouldDeleteTweetTagRelations_whenTagIsDeleted() {
    // given
    tweetGateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = 1),
        fullTweetEntity(id = 2),
        fullTweetEntity(id = 3))
    )
    val deletingId = tagGateway.insertTag("will delete", 1).blockingGet()
    val tagId1 = tagGateway.insertTag("a tag 1", 1).blockingGet()

    gateway.insertTweetTagRelation(listOf(1, 2), deletingId)
    gateway.insertTweetTagRelation(listOf(1, 2), tagId1)

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
  @Test fun shouldDeleteTweetTagRelations_whenTweetIsDeleted() {
    // given
    val deletingId = 1L
    tweetGateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = deletingId),
        fullTweetEntity(id = 2),
        fullTweetEntity(id = 3))
    )
    val tagId1 = tagGateway.insertTag("a tag 1", 1).blockingGet()
    val tagId2 = tagGateway.insertTag("a tag 2", 1).blockingGet()

    gateway.insertTweetTagRelation(listOf(deletingId, 2), tagId1)
    gateway.insertTweetTagRelation(listOf(deletingId, 2), tagId2)

    // when
    tweetGateway.deleteTweetById(deletingId)
    val rels = gateway.selectAllTweetTagRelations().test().values().first()

    // then
    assertThat(rels).containsNoneOf(
        tweetTagRelation(tweetId = deletingId, tagId = tagId1),
        tweetTagRelation(tweetId = deletingId, tagId = tagId2))
  }
}