package com.htoyama.likit.application.likedtweet

import com.htoyama.likit.domain.likedtweet.LikedTweetRepository
import com.htoyama.likit.domain.likedtweet.LikedTweet
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

/**
 * An application service related to [LikedTweet]
 */
class LikedTweetAppService @Inject internal constructor(
    private val likedRepository: LikedTweetRepository
){

  fun find(page: Int): Observable<List<LikedTweet>> {
    return likedRepository.find(page, 200)
  }

}