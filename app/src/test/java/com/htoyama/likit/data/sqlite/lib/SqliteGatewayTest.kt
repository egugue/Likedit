package com.htoyama.likit.data.sqlite.lib

import com.google.common.truth.Truth.assertThat
import com.htoyama.likit.PhotoBuilder
import com.htoyama.likit.data.sqlite.entity.fullTweetEntity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class SqliteGatewayTest {
  lateinit var gateway: SqliteGateway

  @Before fun setUp() {
    gateway = SqliteGateway(
        SqliteOpenHelper(RuntimeEnvironment.application)
    )
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

    // use reverse list to test whether select in descending order of created property
    gateway.insertOrUpdateTweetList(expected3 + expected2 + expected1)

    // assert
    val actual1 = gateway.selectTweetForTest(1, perPage)
    assertThat(actual1).hasSize(perPage)
    assertThat(actual1).isEqualTo(expected1)

    val actual2 = gateway.selectTweetForTest(2, perPage)
    assertThat(actual2).hasSize(perPage)
    assertThat(actual2).isEqualTo(expected2)

    val actual3 = gateway.selectTweetForTest(3, perPage)
    assertThat(actual3).hasSize(2)
    assertThat(actual3).isEqualTo(expected3)

    val actual4 = gateway.selectTweetForTest(4, perPage)
    assertThat(actual4).isEmpty()
  }
}