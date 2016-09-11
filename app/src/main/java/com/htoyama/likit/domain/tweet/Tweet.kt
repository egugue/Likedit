package com.htoyama.likit.domain.tweet

import com.htoyama.likit.domain.tweet.media.Photo
import com.htoyama.likit.domain.tweet.media.Video
import com.htoyama.likit.domain.user.User

/**
 * Created by toyamaosamuyu on 2016/06/28.
 */
data class Tweet(
    val id: Long,
    val user: User,
    val createdAt: Long,
    val text: String,
    val photoList: List<Photo>,
    val urlList: List<Url>,
    val video: Video?
)