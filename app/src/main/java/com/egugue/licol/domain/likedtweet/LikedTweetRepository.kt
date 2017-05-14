package com.egugue.licol.domain.likedtweet

import com.egugue.licol.domain.tag.Tag
import io.reactivex.Observable

/**
 * A repository related to [LikedTweet]
 */
interface LikedTweetRepository {

  /**
   * Retrieve some [LikedTweet]s by the given args
   *
   * @param page
   *     the number of page, which must be a positive integer
   * @param perPage
   *    the number of tweets to retrieve per a page,
   *    which must be between 1 and 200
   */
  fun find(page: Int, perPage: Int): Observable<List<LikedTweet>>

  /**
   * Retrieve some [LikedTweet]s by the given args
   *
   * @param tagId the identifier of [Tag]
   * @param page
   *     the number of page, which must be a positive integer
   * @param perPage
   *    the number of tweets to retrieve per a page,
   *    which must be between 1 and 200
   */
  fun findByTagId(tagId: Long, page: Int, perPage: Int): Observable<List<LikedTweet>>
}
