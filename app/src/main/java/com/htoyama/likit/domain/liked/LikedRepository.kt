package com.htoyama.likit.domain.liked

import com.htoyama.likit.domain.tag.Tag
import io.reactivex.Single

/**
 * A repository related to [LikedTweet]
 */
interface LikedRepository {

  /**
   * Retrieves all [LikedTweet] as list.
   */
  fun find(page: Int, count: Int): Single<List<LikedTweet>>

  /**
   * Retrives some [LikedTweet]s by a given [Tag]
   */
  fun findByTag(tag: Tag): Single<List<Tag>>
}
