package com.egugue.licol.data.sqlite

import com.egugue.licol.data.sqlite.relation.TweetTagRelation
import com.egugue.licol.data.sqlite.tag.TagEntity
import com.egugue.licol.data.sqlite.likedtweet.FullLikedTweetEntity
import com.egugue.licol.data.sqlite.likedtweet.LikedTweetEntity
import com.egugue.licol.data.sqlite.likedtweet.QuotedTweetEntity
import com.egugue.licol.data.sqlite.user.UserEntity
import com.egugue.licol.domain.likedtweet.QuotedTweet
import com.egugue.licol.domain.tweet.Url
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.tweet.media.Video

fun fullTweetEntity(
    id: Long = 1,
    userId: Long = 1,
    text: String = "text",
    likedCount: Int = 1,
    imageList: List<Photo> = emptyList(),
    urlList: List<Url> = emptyList(),
    video: Video? = null,
    quotedTweet: QuotedTweetEntity? = null,
    created: Long = 1L
) = FullLikedTweetEntity(
    LikedTweetEntity(
        id, userId, text, likedCount, imageList, urlList, video, quotedTweet?.id, created
    ),
    quotedTweet
)

fun quotedTweetEntity(
    id: Long = 1,
    text: String = "text",
    userId: Long = 1,
    userName: String = "userName",
    userScreenName: String = "userScreenName",
    userAvatarUrl: String = "userAvatarUrl"
) = QuotedTweetEntity(
    id,
    text,
    userId,
    userName,
    userScreenName,
    userAvatarUrl
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
