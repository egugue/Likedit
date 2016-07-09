package com.htoyama.likit.domain.tag

import rx.Observable

/**
 * A repository related to [Tag]
 */
interface  TagRepository {

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
}