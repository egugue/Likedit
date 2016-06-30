package com.htoyama.likit.model.tweet

import com.htoyama.likit.model.tweet.media.Photo
import com.htoyama.likit.model.tweet.media.Video
import com.htoyama.likit.model.user.User

/**
 * Created by toyamaosamuyu on 2016/06/28.
 */
data class Tweet(
    val id: Long,
    val user: User,
    val createdAt: Long,
    val text: String,
    val photos: List<Photo>,
    val urls: List<Url>,
    val video: Video?
)
