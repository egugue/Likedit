package com.htoyama.licol.data

import com.htoyama.licol.data.sqlite.user.UserSqliteDao
import com.htoyama.licol.user
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
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