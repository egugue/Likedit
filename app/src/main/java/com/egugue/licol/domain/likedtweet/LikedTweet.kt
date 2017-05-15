package com.egugue.licol.domain.likedtweet

import com.egugue.licol.domain.tweet.Tweet

/**
 * A Tweet the logged-in user likes
 */
data class LikedTweet(
    val tweet: Tweet,
    val tagIdList: List<Long>
) {
}
