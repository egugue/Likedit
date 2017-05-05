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
  fun publishNextIdentity(): Long

  /**
   * Retrieve all stored [Tag]s as [List].
   */
  fun findAll(): Observable<List<Tag>>

  /**
   * Store a [Tag]
   */
  fun store(tag: Tag): Single<Long>

  /**
   * remove a [Tag]
   */
  fun remove(tag: Tag): Observable<Any>

  /**
   * remove a [Tag] with the given id
   */
  fun removeById(tagId: Long): Completable

  /**
   * Retrieve some [Tag]s which have a name containing the given arg.
   */
  fun findByNameContaining(part: String): Observable<List<Tag>>
}