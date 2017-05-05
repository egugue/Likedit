package com.htoyama.likit.data

import com.htoyama.likit.domain.user.User
import com.htoyama.likit.domain.user.UserRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * A implementation of [UserRepository]
 */
class UserRepositoryImpl internal @Inject constructor(
) : UserRepository {

  override fun findByNameContaining(part: String): Observable<List<User>> {
    return Observable.never()
  }

}