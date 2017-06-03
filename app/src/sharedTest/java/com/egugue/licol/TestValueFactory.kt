package com.egugue.licol

import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.likedtweet.QuotedTweet
import com.egugue.licol.domain.tag.Tag
import com.egugue.licol.domain.tweet.Tweet
import com.egugue.licol.domain.tweet.Url
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.tweet.media.Size
import com.egugue.licol.domain.tweet.media.Sizes
import com.egugue.licol.domain.tweet.media.Video
import com.egugue.licol.domain.user.User
import java.util.*
import com.twitter.sdk.android.core.models.Tweet as TwitterTweet
import com.twitter.sdk.android.core.models.User as TwitterUser

/**
 * Extensions which create instance for testing.
 */

fun likedTweet(
    id: Long = 1,
    userId: Long = 1L,
    text: String = "text",
    likedCount: Int = 1,
    photoList: List<Photo> = emptyList(),
    urlList: List<Url> = emptyList(),
    video: Video? = null,
    quoted: QuotedTweet? = null,
    createdAt: Long = 1L,
    tagIdList: List<Long> = emptyList()
) = LikedTweet(
    id,
    userId,
    createdAt,
    text,
    likedCount,
    photoList,
    urlList,
    video,
    quoted,
    tagIdList
)

fun quotedTweet(
    id: Long = 1,
    text: String = "text",
    userId: Long = 1,
    userName: String = "userName",
    userScreenName: String = "userScreenName",
    userAvatarUrl: String = "userAvatarUrl"
) = QuotedTweet(
    id,
    text,
    userId,
    userName,
    userScreenName,
    userAvatarUrl
)

fun tweet(
    id: Long = 1,
    user: User = user(),
    text: String = "text",
    photoList: List<Photo> = emptyList(),
    urlList: List<Url> = emptyList(),
    video: Video? = null,
    createdAt: Long = 1L
) = Tweet(
    id,
    user,
    createdAt,
    text,
    photoList,
    urlList,
    video
)

fun photo(): Photo = Photo("url", Sizes(Size(1, 1)))

fun user(
    id: Long = 0,
    name: String = "name",
    screenName: String = "screen name",
    avatarUrl: String = "avatar url",
    likedTweetIdList: List<Long> = emptyList()
) = User(id, name, screenName, avatarUrl, likedTweetIdList)

fun tag(
    id: Long = 1,
    name: String = "name",
    createdAt: Date = Date(),
    tweetIdList: List<Long> = emptyList()
) = Tag(id, name, createdAt, tweetIdList)

fun twitterTweet(
    id: Long = 1,
    createdAt: String = "Tue Sep 04 15:55:52 +0000 2012",
    text: String = "text",
    user: TwitterUser = twitterUser()
) = TwitterTweet(
    null,
    createdAt,
    null,

    null,
    null,
    1,

    true,
    null,
    id,
    id.toString(),

    "inReplyToScreenName",
    1,
    "inReplyToStatusIdStr",

    1,
    "inReplyToStatusId",
    "lang",
    null,

    false,
    null,
    1,
    "quotedStatusIdStr",

    null,
    1,
    false,
    null,

    "source",
    text,
    emptyList(),
    false,

    user,
    false,
    emptyList(),

    "withheldScope",
    null
)

fun twitterUser(
    id: Long = 1,
    name: String = "name",
    screenName: String = "screenName",
    profileImageUrlHttps: String = "https://si0.twimg.com/profile_images/1812284389/allseeingeye_normal.jpg"
) = TwitterUser(
    false,
    "createdAt",
    false,

    false,
    "description",
    "emailAddress",

    null,
    1,
    false,

    0,
    0,
    false,
    id,
    id.toString(),

    false,
    "lang",
    1,
    "location",
    name,

    "profileBackgroundColor",
    "profileBackgroundImageUrl",

    "profileBackgroundImageUrlHttps",
    false,

    "profileBannerUrl",
    "profileImageUrl",
    profileImageUrlHttps,

    "profileLinkColor",
    "profileSidebarBorderColor",

    "profileSidebarFillColor",
    "profileTextColor",

    false,
    false,
    screenName,

    false,
    null,
    1,
    "timezone",

    "url",
    1,
    true,
    emptyList(),

    "withheldScope"
)
