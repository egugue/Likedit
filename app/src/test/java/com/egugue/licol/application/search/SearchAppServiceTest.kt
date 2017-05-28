package com.egugue.licol.application.search

import com.egugue.licol.domain.likedtweet.LikedTweetRepository
import com.egugue.licol.domain.user.UserRepository
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
}