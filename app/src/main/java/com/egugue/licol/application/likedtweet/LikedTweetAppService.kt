package com.egugue.licol.application.likedtweet

import com.egugue.licol.application.likedtweet.LikedTweetPayload.Companion.listFrom
import com.egugue.licol.domain.likedtweet.LikedTweetRepository
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.user.User
import com.egugue.licol.domain.user.UserRepository
import io.reactivex.Observable
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
        .flatMap(
            { userRepository.findByIdList(it.map { it.userId }) },
            { likedTweetList, userList -> LikedTweetPayload.listFrom(likedTweetList, userList) }
        )
  }
}