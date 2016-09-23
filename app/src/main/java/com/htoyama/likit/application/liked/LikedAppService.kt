package com.htoyama.likit.application.liked

import com.htoyama.likit.domain.liked.LikedRepository
import com.htoyama.likit.domain.liked.LikedTweet
import io.reactivex.Single
import javax.inject.Inject

/**
 * An application service related to [LikedTweet]
 */
class LikedAppService @Inject internal constructor(
    private val likedRepository: LikedRepository
){

  fun find(page: Int): Single<List<LikedTweet>> {
    return likedRepository.find(page, 200)
  }

}