package com.egugue.licol.application.likedtweet

import com.egugue.licol.domain.likedtweet.LikedTweetRepository
import com.egugue.licol.domain.user.UserRepository
import com.egugue.licol.likedTweet
import com.egugue.licol.user
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class LikedTweetAppServiceTest {
  @Mock lateinit var likedRepo: LikedTweetRepository
  @Mock lateinit var userRepo: UserRepository
  @InjectMocks lateinit var service: LikedTweetAppService

  @Before fun setUp() {
    MockitoAnnotations.initMocks(this)
  }

  @Test fun `get all liked tweets`() {
    // given
    val page = 1
    val perPage = 200
    whenever(likedRepo.find(page, perPage)).thenReturn(Observable.just(listOf(
        likedTweet(userId = 1L), likedTweet(userId = 2L))
    ))
    whenever(userRepo.findByIdList(listOf(1L, 2L))).thenReturn(Observable.just(listOf(
        user(id = 1L), user(id = 2L)
    )))

    // when
    val actual = service.getAllLikedTweets(page, perPage).blockingFirst()

    // then
    assertThat(actual).containsExactly(
        LikedTweetPayload(likedTweet(userId = 1L), user(id = 1L)),
        LikedTweetPayload(likedTweet(userId = 2L), user(id = 2L))
    )
  }
}