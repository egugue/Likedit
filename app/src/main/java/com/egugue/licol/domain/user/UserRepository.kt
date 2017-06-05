package com.egugue.licol.domain.user

import io.reactivex.Observable

/**
 * A repository related to [User]
 */
interface UserRepository {

  /**
   * Retrieve the user which has the given user id
   *
   * @throws IllegalArgumentException if user id is not a positive integer
   */
  fun findByUserId(userId: Long): Observable<User>

  /**
   * Retrieve some [User]s by the given args
   * Returned list is ordered by liked tweet count.
   *
   * @param page
   *     the number of page, which must be a positive integer
   * @param perPage
   *    the number of tweets to retrieve per a page,
   *    which must be a positive integer
   */
  fun findAll(page: Int, perPage: Int): Observable<List<User>>

  /**
   * Retrieve some [User]s by the given id list
   */
  fun findByIdList(idList: List<Long>): Observable<List<User>>

  /**
   * Retrieve [User]s as list which have user name containing given arg.
   */
  fun findByNameContaining(part: String): Observable<List<User>>
}