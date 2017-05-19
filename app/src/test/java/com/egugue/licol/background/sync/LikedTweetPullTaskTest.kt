package com.egugue.licol.background.sync

import com.egugue.licol.data.net.FavoriteService
import com.egugue.licol.twitterTweet
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import java.lang.IllegalStateException

class LikedTweetPullTaskTest {
  @Mock lateinit var service: FavoriteService
  @Mock lateinit var dbFacade: SqliteFacade
  @InjectMocks lateinit var task: LikedTweetPullTask

  @Before fun setUp() {
    MockitoAnnotations.initMocks(this)
  }

  @Test fun `not store to SQLite if first emitted is empty`() {
    whenFavoriteApiIsInvokedWithPage(1).thenReturn(Single.just(emptyList()))

    val test = task.execute().test()

    test.assertNoErrors()
        .assertComplete()
    verify(dbFacade, never()).insertLikedTweetAndUser(any())
  }

  /**
   * It should store to SQLite as many as until emptyList is emitted.
   */
  @Test fun `store to SQLite as many as until empty list is emitted`() {
    whenFavoriteApiIsInvokedWithPage(1).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(2).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(3).thenReturn(Single.just(emptyList()))

    val test = task.execute().test()

    test.assertNoErrors()
        .assertComplete()
    verify(dbFacade, times(2)).insertLikedTweetAndUser(any())
  }

  @Test fun `execute when net work error is caused`() {
    whenFavoriteApiIsInvokedWithPage(1).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(2).thenReturn(Single.just(listOf(twitterTweet())))

    val aNetworkError = IllegalStateException("foo")
    whenFavoriteApiIsInvokedWithPage(3).thenReturn(Single.error(aNetworkError))

    val test = task.execute().test()

    test.assertError(aNetworkError)
    verify(dbFacade, times(2)).insertLikedTweetAndUser(any())
  }

  @Test fun execute_whenAllApisAreSuccess() {
    whenFavoriteApiIsInvokedWithPage(1).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(2).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(3).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(4).thenReturn(Single.just(listOf(twitterTweet())))

    val test = task.execute().test()

    test.assertNoErrors()
        .assertComplete()
    verify(dbFacade, times(4)).insertLikedTweetAndUser(any())
  }

  @Test fun execute_whenFetchingLikesIsBeingLimited() {
    val limited = HttpException(Response.error<Any>(429, mock()))
    whenFavoriteApiIsInvokedWithPage(1).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(2).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(3).thenReturn(Single.just(listOf(twitterTweet())))
    whenFavoriteApiIsInvokedWithPage(4).thenReturn(Single.error(limited))

    val test = task.execute().test()

    test.assertNoErrors()
        .assertComplete()
    verify(dbFacade, times(3)).insertLikedTweetAndUser(any())
  }

  private fun whenFavoriteApiIsInvokedWithPage(page: Int) =
    whenever(service.list(null, null, 200, null, null, true, page))
}