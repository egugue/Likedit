package com.htoyama.likit.domain.tag

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * A repository related to [Tag]
 */
interface TagRepository {

  /**
   * Publish a next [Tag]'s Identity.
   */
  @Deprecated("Use Tag.UNASSIGNED_ID")
  fun publishNextIdentity(): Long

  /**
   * Retrieve all stored [Tag]s as [List].
   */
  @Deprecated("Use findAll(page, perPage)")
  fun findAll(): Observable<List<Tag>>

  /**
   * Retrieve all stored [Tag]s as [List] by the given args
   *
   * @param page
   *     the number of page, which must be a positive integer
   * @param perPage
   *    the number of tweets to retrieve per a page, which must be between 1 and 200
   */
  fun findAll(page: Int, perPage: Int): Observable<List<Tag>>

  /**
   * Retrieve some [Tag]s which have a name containing the given arg.
   */
  fun findByNameContaining(part: String): Observable<List<Tag>>

  /**
   * Store a [Tag]
   */
  fun store(tag: Tag): Single<Long>

  /**
   * Remove a [Tag] with the given id
   */
  fun removeById(tagId: Long): Completable
}