package com.htoyama.licol.data

import com.google.common.truth.Truth.assertThat
import com.htoyama.licol.data.sqlite.likedtweet.LikedTweetSqliteDao
import com.htoyama.licol.likedTweet
import com.htoyama.licol.tweet
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class LikedTweetRepositoryImplTest {

  @Mock lateinit var dao: LikedTweetSqliteDao
  lateinit var repo: LikedTweetRepositoryImpl

  @Before fun setUp() {
    MockitoAnnotations.initMocks(this)
    repo = LikedTweetRepositoryImpl(dao)
  }

  @Test fun `select liked tweet list`() {
    whenever(dao.select(1, 2)).thenReturn(Observable.just(listOf(
        likedTweet(tweet = tweet(id = 1)),
        likedTweet(tweet = tweet(id = 2))
    )))

    val expected = repo.find(1, 2).blockingFirst()

    assertThat(expected.size).isEqualTo(2)
  }

  @Test fun `not select liked tweet list if the given arg is invalid`() {
    whenever(dao.select(any(), any())).thenReturn(Observable.just(emptyList()))

    // page
    try {
      val invalid = 0
      repo.find(invalid, 1)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("0 < page required but it was 0")
    }

    val validPage = 1
    repo.find(validPage, 1)
  }

  @Test fun `not select liked tweet list if the given per page is invalid`() {
    whenever(dao.select(any(), any())).thenReturn(Observable.just(emptyList()))

    try {
      val invalid = 0
      repo.find(1, invalid)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("1 <= perPage <= 200 required but it was 0")
    }

    repo.find(1, 1)
    repo.find(1, 200)

    try {
      val invalidValue = 201
      repo.find(1, invalidValue)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("1 <= perPage <= 200 required but it was 201")
    }
  }

  @Test fun `select liked tweet list by tag id`() {
    val tagId = 1L
    whenever(dao.selectByTagId(tagId, 1, 2)).thenReturn(Observable.just(listOf(
        likedTweet(tweet = tweet(id = 1)),
        likedTweet(tweet = tweet(id = 2))
    )))

    val expected = repo.findByTagId(tagId, 1, 2).blockingFirst()

    assertThat(expected.size).isEqualTo(2)
  }

  @Test fun `not select liked tweet list by tag id if the given tad id is invalid`() {
    whenever(dao.selectByTagId(any(), any(), any())).thenReturn(Observable.just(emptyList()))

    // page
    try {
      val invalid = -1L
      repo.findByTagId(invalid, 1, 1)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("tagId >= 0 required but it was -1")
    }

    repo.findByTagId(0, 1, 1)
  }

  @Test fun `not select liked tweet list by tag id if the given page is invalid`() {
    whenever(dao.selectByTagId(any(), any(), any())).thenReturn(Observable.just(emptyList()))

    // page
    try {
      val invalid = 0
      repo.findByTagId(1, invalid, 1)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("0 < page required but it was 0")
    }

    val validPage = 1
    repo.findByTagId(1, validPage, 1)
  }

  @Test fun `not select liked tweet list by tag id if the given per page is invalid`() {
    whenever(dao.select(any(), any())).thenReturn(Observable.just(emptyList()))

    try {
      val invalid = 0
      repo.findByTagId(1, 1, invalid)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("1 <= perPage <= 200 required but it was 0")
    }

    repo.findByTagId(1, 1, 1)
    repo.findByTagId(1, 1, 200)

    try {
      val invalidValue = 201
      repo.findByTagId(1, 1, perPage = invalidValue)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("1 <= perPage <= 200 required but it was 201")
    }
  }
}