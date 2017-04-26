package com.htoyama.likit.application.likedtweet

import com.htoyama.likit.domain.likedtweet.LikedRepository
import com.htoyama.likit.domain.likedtweet.LikedTweet
import io.reactivex.Single
import javax.inject.Inject

/**
 * An application service related to [LikedTweet]
 */
class LikedTweetAppService @Inject internal constructor(
    private val likedRepository: LikedRepository
){

  fun find(page: Int): Single<List<LikedTweet>> {
    return likedRepository.find(page, 200)
  }

}