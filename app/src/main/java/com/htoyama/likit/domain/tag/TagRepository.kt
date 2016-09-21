package com.htoyama.likit.domain.tag

import rx.Observable

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
  fun store(tag: Tag): Observable<Void>

  /**
   * remove a [Tag]
   */
  fun remove(tag: Tag): Observable<Void>

  /**
   * Retrieve some [Tag]s which have a name containing the given arg.
   */
  fun findByNameContaining(part: String): Observable<List<Tag>>
}