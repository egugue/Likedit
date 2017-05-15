package com.egugue.licol.application.likedtweet

import com.egugue.licol.domain.likedtweet.LikedTweetRepository
import com.egugue.licol.domain.likedtweet.LikedTweet
import io.reactivex.Observable
import javax.inject.Inject

/**
 * An application service related to [LikedTweet]
 */
class LikedTweetAppService @Inject internal constructor(
    private val likedRepository: LikedTweetRepository
){

  /**
   * Get All [LikedTweet]s
   */
  fun getAllLikedTweets(page: Int): Observable<List<LikedTweet>> {
    return likedRepository.find(page, 200)
  }
}