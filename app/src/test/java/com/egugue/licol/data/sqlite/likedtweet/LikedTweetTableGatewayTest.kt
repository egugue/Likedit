package com.egugue.licol.data.sqlite.likedtweet

import com.egugue.licol.PhotoBuilder
import com.egugue.licol.data.sqlite.fullTweetEntity
import com.google.common.truth.Truth.assertThat
import com.egugue.licol.data.sqlite.likedTweetTableGateway
import com.egugue.licol.data.sqlite.quotedTweetEntity
import com.egugue.licol.testutil.SqliteTestingRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@Ignore("because too verbose")
@RunWith(RobolectricTestRunner::class)
class LikedTweetTableGatewayTest {
  @Rule @JvmField val rule = SqliteTestingRule()
  lateinit var gateway: LikedTweetTableGateway

  @Before fun setUp() {
    gateway = likedTweetTableGateway(rule.briteDB)
  }

  @Test fun shouldInsertTweet() {
    val photoB = PhotoBuilder()
    val tweet = fullTweetEntity(id = 1, userId = 1, imageList = listOf(photoB.build(), photoB.build()))
    gateway.insertOrIgnoreTweet(tweet)

    val actual = gateway.selectAllTweets().test()
    actual.assertValue(listOf(tweet))
  }

  @Test fun `should ignore to update tweet` () {
    val original = fullTweetEntity(id = 1,
        quotedTweet = quotedTweetEntity(id = 1))

    val updated = fullTweetEntity(id = 1,
        text = "updated tweet text",
        quotedTweet = quotedTweetEntity(id = 1,
            userName = "update user name"))

    gateway.insertOrIgnoreTweet(original)
    gateway.insertOrIgnoreTweet(updated)

    val actual = gateway.selectAllTweets().test()
    actual.assertValue(listOf(
        fullTweetEntity(id = 1, quotedTweet = quotedTweetEntity()))
    )
  }

  @Test fun shouldInsertSomeTweets() {
    val list = listOf(
        fullTweetEntity(id = 10, userId = 1),
        fullTweetEntity(id = 20, userId = 10)
    )
    gateway.insertOrIgnoreTweetList(list)

    val actual = gateway.selectAllTweets().test()

    actual.assertValue(list)
  }

  @Test fun shouldDeleteTweet() {
    val id = 1L
    gateway.insertOrIgnoreTweet(fullTweetEntity(id))
    gateway.deleteTweetById(id)

    val actual = gateway.selectAllTweets().test()
    actual.assertValue(emptyList())
  }

  @Test fun shouldSelectByPage() {
    // setUp
    val perPage = 3
    val expected1 = (8L downTo 6L).map { fullTweetEntity(id = it, created = it) }
    val expected2 = (5L downTo 3L).map { fullTweetEntity(id = it, created = it) }
    val expected3 = (2L downTo 1L).map { fullTweetEntity(id = it, created = it) }

    // use a reverse list to test whether select in descending order of created property
    gateway.insertOrIgnoreTweetList(expected3 + expected2 + expected1)

    // assert
    val actual1 = gateway.selectTweet(1, perPage).test()
    actual1.assertValue(expected1)

    val actual2 = gateway.selectTweet(2, perPage).test()
    actual2.assertValue(expected2)

    val actual3 = gateway.selectTweet(3, perPage).test()
    actual3.assertValue(expected3)

    val actual4 = gateway.selectTweet(4, perPage).test()
    actual4.assertValue(emptyList())
  }

  @Suppress("JoinDeclarationAndAssignment")
  @Test fun selectTweet_perPage() {
    var pp: Int // means per page

    pp = 0
    try {
      gateway.selectTweet(1, pp)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("0 < perPage < 201 required but it was 0")
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
      assertThat(e).hasMessageThat().isEqualTo("0 < perPage < 201 required but it was 201")
    }

    pp = Int.MIN_VALUE
    try {
      gateway.selectTweet(1, pp)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("0 < perPage < 201 required but it was ${Int.MIN_VALUE}")
    }

    pp = Int.MAX_VALUE
    try {
      gateway.selectTweet(1, pp)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("0 < perPage < 201 required but it was ${Int.MAX_VALUE}")
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
      assertThat(e).hasMessageThat().isEqualTo("0 < page required but it was 0")
    }

    p = 1
    gateway.selectTweet(p, 1)

    p = Int.MIN_VALUE
    try {
      gateway.selectTweet(p, 1)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("0 < page required but it was ${Int.MIN_VALUE}")
    }

    p = Int.MAX_VALUE
    gateway.selectTweet(p, 1)
  }
}
