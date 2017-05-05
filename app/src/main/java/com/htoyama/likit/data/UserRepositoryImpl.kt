package com.htoyama.likit.data

import com.htoyama.likit.data.sqlite.user.UserSqliteDao
import com.htoyama.likit.domain.user.User
import com.htoyama.likit.domain.user.UserRepository
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