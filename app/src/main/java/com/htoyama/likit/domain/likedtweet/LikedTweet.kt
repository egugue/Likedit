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
    val id: Long,
    val user: User,
    val createdAt: Long,
    val text: String,
    val photoList: List<Photo>,
    val urlList: List<Url>,
    val video: Video?,
    val tagIdList: List<Long>
) {
  @Deprecated("use just properties")
  fun tweet(): Tweet = Tweet(id, user, createdAt, text, photoList, urlList, video)
}
