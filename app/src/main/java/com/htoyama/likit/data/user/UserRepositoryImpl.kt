package com.htoyama.likit.data.user

import com.htoyama.likit.domain.user.User
import com.htoyama.likit.domain.user.UserRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * A implementation of [UserRepository]
 */
class UserRepositoryImpl internal @Inject constructor(
    private val userRealmDao: UserRealmDao
) : UserRepository {

  override fun findByNameContaining(part: String): Single<List<User>> {
    return Single.fromCallable {
      userRealmDao.selectByNameContaining(part)
    }
  }

}