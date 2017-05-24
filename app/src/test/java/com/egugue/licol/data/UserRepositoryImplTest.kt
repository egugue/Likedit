package com.egugue.licol.data

import com.google.common.truth.Truth.assertThat
import com.egugue.licol.data.sqlite.user.UserSqliteDao
import com.egugue.licol.user
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class UserRepositoryImplTest {
  @Mock lateinit var dao: UserSqliteDao
  @InjectMocks lateinit var repo: UserRepositoryImpl

  @Before fun setUp() {
    MockitoAnnotations.initMocks(this)
  }

  @Test fun `find all users`() {
    val expected = listOf(user())
    whenever(dao.selectAllOrderedByLikedTweetCount(1, 1)).thenReturn(Observable.just(expected))

    repo.findAll(1, 1).test()
        .assertValue(expected)
  }

  @Test fun `not find all users if the given page is invalid`() {
    whenever(dao.selectAllOrderedByLikedTweetCount(any(), any())).thenReturn(Observable.just(emptyList()))

    try {
      repo.findAll(0, 1)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("0 < page required but it was 0")
    }

    repo.findAll(1, 1)
  }

  @Test fun `not find all users if the given per page is invalid`() {
    whenever(dao.selectAllOrderedByLikedTweetCount(any(), any())).thenReturn(Observable.just(emptyList()))

    try {
      repo.findAll(1, 0)
      fail()
    } catch (e: IllegalArgumentException) {
      assertThat(e).hasMessageThat().isEqualTo("0 < perPage required but it was 0")
    }

    repo.findAll(1, 1)
  }

  @Test fun `find some users by the given id list`() {
    val expected = listOf(user(id = 1), user(id = 2), user(id = 3))
    whenever(dao.selectByIdList(listOf(1, 2, 3)))
        .thenReturn(Observable.just(expected))

    repo.findByIdList(listOf(1, 2, 3)).test()
        .assertValue(expected)
  }

  @Test fun `search users by name containing the given arg`() {
    val arg = "part of name"
    val limit = 10
    val expected = listOf(user())
    whenever(dao.searchByNameOrScreenName(arg, arg, limit))
        .thenReturn(Observable.just(expected))

    repo.findByNameContaining(arg).test()
        .assertValue(expected)
  }
}