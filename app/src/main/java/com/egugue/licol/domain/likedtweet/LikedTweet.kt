package com.egugue.licol.domain.likedtweet

import com.egugue.licol.domain.tweet.Url
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.tweet.media.Video

/**
 * A Tweet the logged-in user likes
 */
data class LikedTweet(
    val id: Long,
    val userId: Long,
    val createdAt: Long,
    val text: String,
    val likedCount: Int,
    val photoList: List<Photo>,
    val urlList: List<Url>,
    val video: Video?,
    val quoted: QuotedTweet?,
    val tagIdList: List<Long>
) {
}
