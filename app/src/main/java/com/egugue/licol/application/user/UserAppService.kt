package com.egugue.licol.application.user

import com.egugue.licol.domain.user.User
import com.egugue.licol.domain.user.UserRepository
import io.reactivex.Observable
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

/**
 * An application service for [User]
 */
class UserAppService @Inject constructor(
    private val userRepository: UserRepository
) {

  /**
   * Get all [User]s
   */
  fun getAllUsers(page: Int, perPage: Int): Observable<List<User>> {
    return userRepository.findAll(page, perPage)
        .delay(3, SECONDS)
  }

  /**
   * Get [User]s as list which have user name containing given arg.
   */
  fun getUsersByNameContaining(partOfName: String): Observable<List<User>> {
    return userRepository.findByNameContaining(partOfName)
        .delay(3, SECONDS)
  }
}
