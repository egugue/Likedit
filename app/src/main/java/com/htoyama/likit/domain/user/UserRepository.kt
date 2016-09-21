package com.htoyama.likit.domain.user

import rx.Observable

/**
 * A repository related to [User]
 */
interface UserRepository {

  /**
   * Retrieve [User]s as list which have user name containing given arg.
   */
  fun findByNameContaining(part: String): Observable<List<User>>
}