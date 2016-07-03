package com.htoyama.likit.domain.tweet

import rx.Observable

/**
 * A repository related to [Tweet]
 */
interface TweetRepository {

  fun findLikedTweetList(page: Int, count: Int): Observable<List<Tweet>>
}