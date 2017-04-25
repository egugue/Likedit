package com.htoyama.likit.data.sqlite.tweet

import com.htoyama.likit.PhotoBuilder
import com.htoyama.likit.data.sqlite.fullTweetEntity
import com.htoyama.likit.data.sqlite.lib.SqliteOpenHelper
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class LikedTweetTableGatewayTest {
  lateinit var gateway: LikedTweetTableGateway
  lateinit var helper: SqliteOpenHelper

  @Before fun setUp() {
    helper = SqliteOpenHelper(RuntimeEnvironment.application)
    gateway = LikedTweetTableGateway(helper)
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

  @Test fun `should ignore to update tweet` () {
    val original = fullTweetEntity(id = 1, userId = 1)
    val updated = fullTweetEntity(id = 1, userId = 1,
        userName = "updated user name",
        text = "updated tweet text")

    gateway.insertOrUpdateTweet(original)
    gateway.insertOrUpdateTweet(updated)

    val list = gateway.selectAllTweets()
    assertThat(list).containsExactly(
        fullTweetEntity(id = 1, userId = 1,
            userName = "updated user name"
            // text = "updated tweet text"   this is expected to ignore
        ))
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

  @Test fun shouldDeleteTweet() {
    val id = 1L
    gateway.insertOrUpdateTweet(fullTweetEntity(id))
    gateway.deleteTweetById(id)

    val tweets = gateway.selectAllTweets()
    assertThat(tweets).isEmpty()
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
}
