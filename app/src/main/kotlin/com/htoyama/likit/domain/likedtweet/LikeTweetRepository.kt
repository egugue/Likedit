package com.htoyama.likit.domain.likedtweet

import com.htoyama.likit.domain.tag.Tag
import rx.Observable

/**
 * A repository related to [LikedTweet]
 */
interface LikeTweetRepository {

  /**
   * Retrieves all [LikedTweet] as list.
   */
  fun findAll(): Observable<List<LikedTweet>>

  /**
   * Retrives some [LikedTweet]s by a given [Tag]
   */
  fun findByTag(tag: Tag): Observable<List<Tag>>
}
