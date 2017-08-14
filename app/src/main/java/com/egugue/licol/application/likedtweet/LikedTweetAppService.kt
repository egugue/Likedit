package com.egugue.licol.application.likedtweet

import com.egugue.licol.common.extensions.zipWith
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.likedtweet.LikedTweetRepository
import com.egugue.licol.domain.user.User
import com.egugue.licol.domain.user.UserRepository
import io.reactivex.Observable
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

/**
 * An application service related to [LikedTweet]
 */
class LikedTweetAppService @Inject internal constructor(
    private val likedRepository: LikedTweetRepository,
    private val userRepository: UserRepository
) {

  /**
   * Get All [LikedTweet]s with each [User]
   */
  fun getAllLikedTweets(page: Int, perPage: Int): Observable<List<LikedTweetPayload>> {
    return likedRepository.find(page, perPage)
        .delay(3, SECONDS)
        .flatMap(
            { userRepository.findByIdList(it.map { it.userId }.distinct()) },
            { likedTweetList, userList -> LikedTweetPayload.listFrom(likedTweetList, userList) }
        )
  }

  /**
   * Get All [LikedTweet]s with each [User]
   */
  fun getAllLikedTweetsByUserId(userId: Long, page: Int,
      perPage: Int): Observable<List<LikedTweetPayload>> {
    return likedRepository.findByUserId(userId, page, perPage)
        .delay(3, SECONDS)
        .zipWith(
            userRepository.findByUserId(userId),
            { likedTweetList, user -> likedTweetList.map { LikedTweetPayload(it, user) } }
        )
  }
}