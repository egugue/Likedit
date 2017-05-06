package com.htoyama.licol.domain.user

import io.reactivex.Observable

/**
 * A repository related to [User]
 */
interface UserRepository {

  /**
   * Retrieve [User]s as list which have user name containing given arg.
   */
  fun findByNameContaining(part: String): Observable<List<User>>
}