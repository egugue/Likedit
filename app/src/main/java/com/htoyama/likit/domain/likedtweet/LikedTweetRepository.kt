package com.htoyama.likit.domain.likedtweet

import com.htoyama.likit.domain.tag.Tag
import io.reactivex.Observable
import io.reactivex.Single

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
   * Retrieves all [LikedTweet] as list.
   */
  //fun find(page: Int, count: Int): Single<List<LikedTweet>>

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
