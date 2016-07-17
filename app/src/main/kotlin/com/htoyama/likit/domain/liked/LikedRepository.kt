package com.htoyama.likit.domain.liked

import com.htoyama.likit.domain.tag.Tag
import rx.Observable

/**
 * A repository related to [LikedTweet]
 */
interface LikedRepository {

  /**
   * Retrieves all [LikedTweet] as list.
   */
  fun find(page: Int, count: Int): Observable<List<LikedTweet>>

  /**
   * Retrives some [LikedTweet]s by a given [Tag]
   */
  fun findByTag(tag: Tag): Observable<List<Tag>>
}
