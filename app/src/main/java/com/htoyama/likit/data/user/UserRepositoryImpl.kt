package com.htoyama.likit.data.user

import com.htoyama.likit.domain.user.User
import com.htoyama.likit.domain.user.UserRepository
import rx.Observable
import javax.inject.Inject

/**
 * A implementation of [UserRepository]
 */
class UserRepositoryImpl internal @Inject constructor(
    private val userRealmDao: UserRealmDao
) : UserRepository {

  override fun findByNameContaining(part: String): Observable<List<User>> {
    return Observable.fromCallable {
      userRealmDao.selectByNameContaining(part)
    }
  }

}