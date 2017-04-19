package com.htoyama.likit.background.sync

import com.htoyama.likit.data.common.net.FavoriteService
import com.htoyama.likit.data.sqlite.tweet.TweetTableGateway
import com.htoyama.likit.twitterTweet
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.lang.IllegalStateException

class LikesPullTaskTest {
  @Mock lateinit var service: FavoriteService
  @Mock lateinit var gateway: TweetTableGateway
  lateinit var task: LikesPullTask

  @Before fun setUp() {
    MockitoAnnotations.initMocks(this)
    task = LikesPullTask(service, gateway)
  }

  /**
   * It should not store to SQLite when first emitted item is empty.
   */
  @Test fun execute_whenFirstEmittedIsEmpty() {
    whenFavoriteApiIsInvokedWithPage(1).thenReturn(Single.just(emptyList()))

    val test = task.execute().test()

    test.assertNoErrors()
        .assertComplete()
    verify(gateway, never()).insertOrUpdateTweetList(any())
  }

  /**
   * It should store to SQLite as many as until emptyList is emitted.
   */
  @Test fun execute_whenIntermediateEmittedValueIsEmptyList() {
    whenFavoriteApiIsInvokedWithPage(1).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(2).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(3).thenReturn(Single.just(emptyList()))

    val test = task.execute().test()

    test.assertNoErrors()
        .assertComplete()
    verify(gateway, times(2)).insertOrUpdateTweetList(any())
  }

  @Test fun execute_whenNetworkErrorIsCaused() {
    whenFavoriteApiIsInvokedWithPage(1).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(2).thenReturn(Single.just(listOf(twitterTweet())))

    val aNetworkError = IllegalStateException("foo")
    whenFavoriteApiIsInvokedWithPage(3).thenReturn(Single.error(aNetworkError))

    val test = task.execute().test()

    test.assertError(aNetworkError)
    verify(gateway, times(2)).insertOrUpdateTweetList(any())
  }

  @Test fun execute_whenAllApisAreSuccess() {
    whenFavoriteApiIsInvokedWithPage(1).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(2).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(3).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(4).thenReturn(Single.just(listOf(twitterTweet())))

    val test = task.execute().test()

    test.assertNoErrors()
        .assertComplete()
    verify(gateway, times(4)).insertOrUpdateTweetList(any())
  }

  @Test fun execute_whenFetchingLikesIsBeingLimited() {
    val limited = IllegalArgumentException()// TODO
    whenFavoriteApiIsInvokedWithPage(1).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(2).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(3).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(4).thenReturn(Single.error(limited))

    val test = task.execute().test()

    test.assertNoErrors()
        .assertComplete()
    verify(gateway, times(3)).insertOrUpdateTweetList(any())
  }

  private fun whenFavoriteApiIsInvokedWithPage(page: Int) =
    whenever(service.list(null, null, 200, null, null, true, page))
}