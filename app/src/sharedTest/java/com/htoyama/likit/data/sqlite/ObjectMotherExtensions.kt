package com.htoyama.likit.data.sqlite

import com.htoyama.likit.data.sqlite.relation.TweetTagRelation
import com.htoyama.likit.data.sqlite.tag.TagEntity
import com.htoyama.likit.data.sqlite.tweet.FullTweetEntity
import com.htoyama.likit.data.sqlite.tweet.TweetEntity
import com.htoyama.likit.data.sqlite.user.UserEntity
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
) = FullTweetEntity(
    TweetEntity(
        id, userId, text, imageList, urlList, video, created
    ),
    UserEntity(
        userId, userName, userScreenName, userAvatarUrl
    )
)

fun tagEntity(
    id: Long,
    name: String,
    created: Long
) = TagEntity(
    id, name, created
)

fun tweetTagRelation(
    tweetId: Long,
    tagId: Long
) = TweetTagRelation(tweetId, tagId)
