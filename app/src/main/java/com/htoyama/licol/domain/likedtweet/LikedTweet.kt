package com.htoyama.licol.domain.likedtweet

import com.htoyama.licol.domain.tweet.Tweet

/**
 * A Tweet the logged-in user likes
 */
data class LikedTweet(
    val tweet: Tweet,
    val tagIdList: List<Long>
) {
}
