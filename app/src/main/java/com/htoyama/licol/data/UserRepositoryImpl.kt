package com.htoyama.licol.data

import com.htoyama.licol.data.sqlite.user.UserSqliteDao
import com.htoyama.licol.domain.user.User
import com.htoyama.licol.domain.user.UserRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * A implementation of [UserRepository]
 */
class UserRepositoryImpl internal @Inject constructor(
    private val userSqliteDao: UserSqliteDao
) : UserRepository {

  override fun findByNameContaining(part: String): Observable<List<User>> {
    val limit = 10
    return userSqliteDao.searchByNameOrScreenName(part, part, limit)
  }
}