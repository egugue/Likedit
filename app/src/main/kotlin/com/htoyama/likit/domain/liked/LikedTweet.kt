package com.htoyama.likit.domain.liked

import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tweet.Tweet

/**
 * A Tweet the logged-in user likes
 */
data class LikedTweet(
    val tweet: Tweet,
    val tagList: List<Tag>
)
