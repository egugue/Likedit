package com.egugue.licol.application.likedtweet

import com.egugue.licol.domain.likedtweet.LikedTweetRepository
import com.egugue.licol.likedTweet
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class LikedTweetAppServiceTest {
  @Mock lateinit var likedRepo: LikedTweetRepository
  @InjectMocks lateinit var service: LikedTweetAppService

  @Before fun setUp() {
    MockitoAnnotations.initMocks(this)
  }

  @Test fun `get all liked tweets`() {
    val expected = listOf(likedTweet())
    whenever(likedRepo.find(1, 200)).thenReturn(Observable.just(expected))

    service.getAllLikedTweets(1).test()
        .assertValue(expected)
  }
}