package com.egugue.licol.application.search

import com.egugue.licol.application.likedtweet.LikedTweetPayload
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

class SearchAppServiceTest {
  @Mock lateinit var userRepo: UserRepository
  @Mock lateinit var likedTweetRepo: LikedTweetRepository
  @InjectMocks lateinit var service: SearchAppService

  @Before fun setUp() {
    MockitoAnnotations.initMocks(this)
  }

  @Test fun `get suggestions`() {
    val emittedUsers = listOf(user(id = 1), user(id = 2))
    whenever(userRepo.findByNameContaining("query"))
        .thenReturn(Observable.just(emittedUsers))

    val suggestions = service.getSearchSuggestions("query").blockingFirst()
    assertThat(suggestions.userList).isEqualTo(emittedUsers)
  }

  @Test fun `get empty suggestion`() {
    whenever(userRepo.findByNameContaining("query"))
        .thenReturn(Observable.just(emptyList()))

    val suggestions = service.getSearchSuggestions("query").blockingFirst()
    assertThat(suggestions).isEqualTo(Suggestions.empty())
  }

  @Test fun `getSearchResult`() {
    val expectedLikedList = listOf(likedTweet(id = 1, userId = 1), likedTweet(id = 2, userId = 1))
    val expectedUserList = listOf(user(id = 1))
    whenever(likedTweetRepo.findByTextContaining("query", 1, 200)).thenReturn(Observable.just(expectedLikedList))
    whenever(userRepo.findByIdList(listOf(1))).thenReturn(Observable.just(expectedUserList))

    val actual = service.getSearchResult("query", 1, 200).blockingFirst()
    assertThat(actual).isEqualTo(LikedTweetPayload.listFrom(expectedLikedList, expectedUserList))
  }
}