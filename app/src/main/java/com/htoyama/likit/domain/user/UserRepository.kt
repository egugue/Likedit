package com.htoyama.likit.domain.user

import io.reactivex.Single

/**
 * A repository related to [User]
 */
interface UserRepository {

  /**
   * Retrieve [User]s as list which have user name containing given arg.
   */
  fun findByNameContaining(part: String): Single<List<User>>
}