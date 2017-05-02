package com.htoyama.likit.domain.likedtweet

import com.htoyama.likit.domain.tweet.Tweet
import com.htoyama.likit.domain.tweet.Url
import com.htoyama.likit.domain.tweet.media.Photo
import com.htoyama.likit.domain.tweet.media.Video
import com.htoyama.likit.domain.user.User

/**
 * A Tweet the logged-in user likes
 */
data class LikedTweet(
    val tweet: Tweet,
    val tagIdList: List<Long>
) {
}
