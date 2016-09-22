package com.htoyama.likit.domain.tag

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
  fun findAll(): Single<List<Tag>>

  /**
   * Store a [Tag]
   */
  fun store(tag: Tag): Single<Any>

  /**
   * remove a [Tag]
   */
  fun remove(tag: Tag): Single<Any>

  /**
   * Retrieve some [Tag]s which have a name containing the given arg.
   */
  fun findByNameContaining(part: String): Single<List<Tag>>
}