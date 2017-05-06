package com.htoyama.licol.application.user

import com.htoyama.licol.domain.user.User
import com.htoyama.licol.domain.user.UserRepository
import io.reactivex.Observable
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
  }

  /**
   * Get [User]s as list which have user name containing given arg.
   */
  fun getUsersByNameContaining(partOfName: String): Observable<List<User>> {
    return userRepository.findByNameContaining(partOfName)
  }
}
