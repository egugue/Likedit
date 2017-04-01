package com.htoyama.likit.data.sqlite.lib

import android.database.sqlite.SQLiteConstraintException
import com.google.common.truth.Truth.assertThat
import com.htoyama.likit.PhotoBuilder
import com.htoyama.likit.data.sqlite.entity.fullTweetEntity
import com.htoyama.likit.data.sqlite.entity.tagEntity
import com.htoyama.likit.data.sqlite.entity.tweetTagRelation
import junit.framework.Assert.fail
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class SqliteGatewayTest {
  lateinit var gateway: SqliteGateway
  lateinit var helper: SqliteOpenHelper

  @Before fun setUp() {
    helper = SqliteOpenHelper(RuntimeEnvironment.application)
    gateway = SqliteGateway(helper)
  }

  @After fun tearDown() {
    helper.close()
    RuntimeEnvironment.application.deleteDatabase("likedit")
  }

  @Test fun shouldInsertTweet() {
    val photoB = PhotoBuilder()
    val tweet = fullTweetEntity(id = 1, userId = 1, imageList = listOf(photoB.build(), photoB.build()))
    gateway.insertOrUpdateTweet(tweet)

    val list = gateway.selectAllTweets()
    assertThat(list).containsExactly(tweet)
  }

  @Test fun shouldUpdateTweet() {
    val original = fullTweetEntity(id = 1, userId = 1)
    val updated = fullTweetEntity(id = 1, userId = 1,
        userName = "updated user name",
        text = "updated tweet text")

    gateway.insertOrUpdateTweet(original)
    gateway.insertOrUpdateTweet(updated)

    val list = gateway.selectAllTweets()
    assertThat(list).containsExactly(updated)
  }

  @Test fun shouldInsertSomeTweets() {
    val list = listOf(
        fullTweetEntity(id = 10, userId = 1),
        fullTweetEntity(id = 20, userId = 10)
    )
    gateway.insertOrUpdateTweetList(list)

    val actual = gateway.selectAllTweets()

    assertThat(actual).containsExactlyElementsIn(list)
  }

  @Test fun shouldSelectByPage() {
    // setUp
    val perPage = 3
    val expected1 = (8L downTo 6L).map { fullTweetEntity(id = it, created = it) }
    val expected2 = (5L downTo 3L).map { fullTweetEntity(id = it, created = it) }
    val expected3 = (2L downTo 1L).map { fullTweetEntity(id = it, created = it) }

    // use a reverse list to test whether select in descending order of created property
    gateway.insertOrUpdateTweetList(expected3 + expected2 + expected1)

    // assert
    val actual1 = gateway.selectTweet(1, perPage)
    assertThat(actual1).hasSize(perPage)
    assertThat(actual1).isEqualTo(expected1)

    val actual2 = gateway.selectTweet(2, perPage)
    assertThat(actual2).hasSize(perPage)
    assertThat(actual2).isEqualTo(expected2)

    val actual3 = gateway.selectTweet(3, perPage)
    assertThat(actual3).hasSize(2)
    assertThat(actual3).isEqualTo(expected3)

    val actual4 = gateway.selectTweet(4, perPage)
    assertThat(actual4).isEmpty()
  }

  @Suppress("JoinDeclarationAndAssignment")
  @Test fun selectTweet_perPage() {
    var pp: Int // means per page

    pp = 0
    try {
      gateway.selectTweet(1, pp)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessage("0 < perPage < 201 required but it was 0")
    }

    pp = 1
    gateway.selectTweet(1, pp)

    pp = 200
    gateway.selectTweet(1, pp)

    pp = 201
    try {
      gateway.selectTweet(1, pp)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessage("0 < perPage < 201 required but it was 201")
    }

    pp = Int.MIN_VALUE
    try {
      gateway.selectTweet(1, pp)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessage("0 < perPage < 201 required but it was ${Int.MIN_VALUE}")
    }

    pp = Int.MAX_VALUE
    try {
      gateway.selectTweet(1, pp)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessage("0 < perPage < 201 required but it was ${Int.MAX_VALUE}")
    }
  }

  @Suppress("JoinDeclarationAndAssignment")
  @Test fun selectTweet_page() {
    var p: Int // means page

    p = 0
    try {
      gateway.selectTweet(p, 1)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessage("0 < page required but it was 0")
    }

    p = 1
    gateway.selectTweet(p, 1)

    p = Int.MIN_VALUE
    try {
      gateway.selectTweet(p, 1)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessage("0 < page required but it was ${Int.MIN_VALUE}")
    }

    p = Int.MAX_VALUE
    gateway.selectTweet(p, 1)
  }

  @Test fun selectTagById_whenTagInserted() {
    val tagId = gateway.insertTag("foo", 1)
    assertThat(tagId).isNotEqualTo(-1)

    val actual = gateway.selectTagById(tagId)
    assertThat(actual).isEqualTo(tagEntity(tagId, "foo", 1))
  }

  @Test fun selectTagById_whenNoTagInserted() {
    val actual = gateway.selectTagById(1)
    assertThat(actual).isNull()
  }

  @Test fun searchTagByName() {
    gateway.insertTag("bb%_bbbbaacc", 1) // hit
    gateway.insertTag("baaccb%_b", 1) // hit
    gateway.insertTag("aaabbbccc", 1)
    gateway.insertTag("aaaBBBccc", 1)
    gateway.insertTag("aaaB%_Bccc", 1) // hit
    gateway.insertTag("abc", 1)
    gateway.insertTag("aabbc", 1)
    gateway.insertTag("あああいいいううう", 1)

    val actual = gateway.searchTagByName("B%_B")

    assertThat(actual).isEqualTo(listOf(
        tagEntity(5, "aaaB%_Bccc", 1),
        tagEntity(2, "baaccb%_b", 1),
        tagEntity(1, "bb%_bbbbaacc", 1)
    ))
  }

  @Test fun searchTagNameName_multiByte() {
    gateway.insertTag("あああいいいううう", 1)
    gateway.insertTag("いいいいいいあああいいいううう", 1)
    gateway.insertTag("いいあああ", 1)
    gateway.insertTag("いいああ", 1)
    gateway.insertTag("ああいいうう", 1)
    gateway.insertTag("あｓｆｄｓｆｄｆｓ", 1)

    val actual = gateway.searchTagByName("あああ")

    assertThat(actual).isEqualTo(listOf(
        tagEntity(1, "あああいいいううう", 1),
        tagEntity(3, "いいあああ", 1),
        tagEntity(2, "いいいいいいあああいいいううう", 1)
    ))
  }

  @Test fun updateTagName() {
    val id = gateway.insertTag("before update", 1)
    gateway.updateTagNameById(id, "after update")

    val actual = gateway.selectTagById(id)

    assertThat(actual).isEqualTo(tagEntity(id, "after update", 1))
  }

  @Test fun updateTagName_whenInvalidIdSpecified() {
    try {
      gateway.updateTagNameById(1, "the tag with the id has not yet inserted")
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessage(
          "tried to update the name of the tag with id(1). but it has not inserted.")
    }
  }

  @Test fun deleteTagById() {
    val id = gateway.insertTag("any", 1)

    val beforeDelete = gateway.selectTagById(id)
    assertThat(beforeDelete).isNotNull()

    gateway.deleteTagById(id)

    val afterDelete = gateway.selectTagById(id)
    assertThat(afterDelete).isNull()
  }

  @Test fun deleteTagById_whenInvaildIdSpecified() {
    try {
      gateway.deleteTagById(1)
      fail()
    } catch (e: IllegalStateException) {
      assertThat(e).hasMessage("tried to delete the tag with id(1). but there is no such tag.")
    }
  }

  @Test fun shouldInsertTweetTagRelations() {
    // given
    gateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = 1),
        fullTweetEntity(id = 2)))

    val tagId = gateway.insertTag("id-1", 1)

    // when
    gateway.insertTweetTagRelation(listOf(1, 2), tagId)

    // then
    val relations = gateway.selectAllTweetTagRelations()
    assertThat(relations).isEqualTo(listOf(
        tweetTagRelation(1, tagId),
        tweetTagRelation(2, tagId)
    ))
  }

  @Test fun shouldNotInsertTweetTagRelations_whenNoTagInserted() {
    // given
    gateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = 1),
        fullTweetEntity(id = 2)))

    // not insert
    // val tagId = gateway.insertTag("id-1", 1)
    val invalidTagId = 1L

    // when
    try {
      gateway.insertTweetTagRelation(listOf(1, 2), invalidTagId)
      fail()
    } catch (e: SQLiteConstraintException) {
    }
  }

  @Test fun shouldNotInsertTweetTagRelations_whenNoTweetInserted() {
    // given
    /* not insert
    gateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = 1),
        fullTweetEntity(id = 2)))
        */

    val tagId = gateway.insertTag("id-1", 1)

    // when
    try {
      gateway.insertTweetTagRelation(listOf(1), tagId)
      fail()
    } catch (e: SQLiteConstraintException) {
    }
  }

  @Test fun shouldDeleteTweetTagRelations() {
    // given
    val deletingId = 1L
    gateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = deletingId),
        fullTweetEntity(id = 2)))

    val tagId = gateway.insertTag("id-1", 1)
    gateway.insertTweetTagRelation(listOf(deletingId, 2), tagId)

    // when
    gateway.deleteTweetTagRelation(listOf(deletingId), tagId)

    // then
    val rels = gateway.selectAllTweetTagRelations()
    assertThat(rels).doesNotContain(tweetTagRelation(deletingId, tagId))
  }

  /**
   * Because of defining "ON DELETE CASCADE" on CREATE statement,
   * it should delete relations when a tag is deleted.
   */
  @Test fun shouldDeleteRelations_whenTagIsDeleted() {
    // given
    gateway.insertOrUpdateTweetList(listOf(
        fullTweetEntity(id = 1),
        fullTweetEntity(id = 2),
        fullTweetEntity(id = 3))
    )
    val deletingId = gateway.insertTag("will delete", 1)
    val tagId1 = gateway.insertTag("a tag 1", 1)

    gateway.insertTweetTagRelation(listOf(1, 2), deletingId)
    gateway.insertTweetTagRelation(listOf(1, 2), tagId1)

    // when
    gateway.deleteTagById(deletingId)
    val rels = gateway.selectAllTweetTagRelations()

    // then
    assertThat(rels).containsNoneOf(
        tweetTagRelation(tweetId = 1, tagId = deletingId),
        tweetTagRelation(tweetId = 2, tagId = deletingId))
  }
}