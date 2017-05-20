package com.egugue.licol.application.likedtweet

import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.user.User
import timber.log.Timber

/**
 * A domain payload object which has [LikedTweet] and [User]
 *
 * https://vaughnvernon.co/?page_id=40
 */
data class LikedTweetPayload(
    val likedTweet: LikedTweet,
    val user: User
) {

  companion object {

    /**
     * Create list of [LikedTweetPayload]
     */
    fun listFrom(tweetList: List<LikedTweet>, userList: List<User>): List<LikedTweetPayload> {
      val userMap = mutableMapOf<Long, User>()
      userList.forEach { userMap.put(it.id, it) }

      val list = ArrayList<LikedTweetPayload>(tweetList.size)
      for (likedTweet in tweetList) {
        val user = userMap[likedTweet.userId]
        if (user == null) {
          Timber.e("cannot find user(${likedTweet.userId}) of tweet(${likedTweet.id}")
          break
        }

        list.add(LikedTweetPayload(likedTweet, user))
      }

      return list
    }
  }
}