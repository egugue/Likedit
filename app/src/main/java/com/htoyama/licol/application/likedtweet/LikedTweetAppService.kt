package com.htoyama.licol.application.likedtweet

import com.htoyama.licol.domain.likedtweet.LikedTweetRepository
import com.htoyama.licol.domain.likedtweet.LikedTweet
import io.reactivex.Observable
import javax.inject.Inject

/**
 * An application service related to [LikedTweet]
 */
class LikedTweetAppService @Inject internal constructor(
    private val likedRepository: LikedTweetRepository
){

  fun getAllLikedTweets(page: Int): Observable<List<LikedTweet>> {
    return likedRepository.find(page, 200)
  }
}