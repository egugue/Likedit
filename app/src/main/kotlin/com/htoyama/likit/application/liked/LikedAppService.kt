package com.htoyama.likit.application.liked

import com.htoyama.likit.domain.liked.LikedRepository
import com.htoyama.likit.domain.liked.LikedTweet
import rx.Observable
import javax.inject.Inject

/**
 * An application service related to [LikedTweet]
 */
class LikedAppService @Inject internal constructor(
    private val likedRepository: LikedRepository
){

  fun find(page: Int): Observable<List<LikedTweet>> {
    return likedRepository.find(page, 200)
  }

}