package com.htoyama.likit.data.sqlite.entity

import com.htoyama.likit.domain.tweet.Url
import com.htoyama.likit.domain.tweet.media.Photo
import com.htoyama.likit.domain.tweet.media.Video

fun fullTweetEntity(
    id: Long,
    userId: Long = 1,
    userName: String = "userName",
    userScreenName: String = "userScreenName",
    userAvatarUrl: String = "userAvatarUrl",
    text: String = "text",
    imageList: List<Photo> = emptyList(),
    urlList: List<Url> = emptyList(),
    video: Video? = null,
    created: Long = 1L
)= FullTweetEntity(
    TweetEntity(
        id, userId, text, imageList, urlList, video, created
    ),
    UserEntity(
        userId, userName, userScreenName, userAvatarUrl
    )
)
