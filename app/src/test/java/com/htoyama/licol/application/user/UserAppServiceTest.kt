package com.htoyama.licol.application.user

import com.htoyama.licol.domain.user.UserRepository
import com.htoyama.licol.user
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class UserAppServiceTest {
  @Mock lateinit var userRepo: UserRepository
  @InjectMocks lateinit var service: UserAppService

  @Before fun setUp() {
    MockitoAnnotations.initMocks(this)
  }

  @Test fun `get all users`() {
    val expected = listOf(user())
    whenever(userRepo.findAll(1, 1)).thenReturn(Observable.just(expected))

    service.getAllUsers(1, 1).test()
        .assertValue(expected)
  }

  @Test fun `get users by name containing the given arg`() {
    val expected = listOf(user())
    whenever(userRepo.findByNameContaining("foo")).thenReturn(Observable.just(expected))

    service.getUsersByNameContaining("foo").test()
        .assertValue(expected)
  }

}