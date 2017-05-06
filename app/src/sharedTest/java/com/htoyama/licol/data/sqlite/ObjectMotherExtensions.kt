package com.htoyama.licol.data.sqlite

import com.htoyama.licol.data.sqlite.relation.TweetTagRelation
import com.htoyama.licol.data.sqlite.tag.TagEntity
import com.htoyama.licol.data.sqlite.likedtweet.FullLikedTweetEntity
import com.htoyama.licol.data.sqlite.likedtweet.LikedTweetEntity
import com.htoyama.licol.data.sqlite.user.UserEntity
import com.htoyama.licol.domain.tweet.Url
import com.htoyama.licol.domain.tweet.media.Photo
import com.htoyama.licol.domain.tweet.media.Video

fun fullTweetEntity(
    id: Long = 1,
    userId: Long = 1,
    userName: String = "userName",
    userScreenName: String = "userScreenName",
    userAvatarUrl: String = "userAvatarUrl",
    text: String = "text",
    imageList: List<Photo> = emptyList(),
    urlList: List<Url> = emptyList(),
    video: Video? = null,
    created: Long = 1L
) = FullLikedTweetEntity(
    LikedTweetEntity(
        id, userId, text, imageList, urlList, video, created
    ),
    UserEntity(
        userId, userName, userScreenName, userAvatarUrl
    )
)

fun userEntity(
    id: Long = 0,
    name: String = "name",
    screenName: String = "screen name",
    avatarUrl: String = "avatar url"
) = UserEntity(id, name, screenName, avatarUrl)

fun tagEntity(
    id: Long = 0,
    name: String = "name",
    created: Long = 1493770273
) = TagEntity(id, name, created)

fun tweetTagRelation(
    tweetId: Long,
    tagId: Long
) = TweetTagRelation(tweetId, tagId)
