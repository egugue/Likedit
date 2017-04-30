package com.htoyama.likit

import com.htoyama.likit.domain.likedtweet.LikedTweet
import com.htoyama.likit.domain.tweet.Tweet
import com.htoyama.likit.domain.tweet.Url
import com.htoyama.likit.domain.tweet.media.Photo
import com.htoyama.likit.domain.tweet.media.Video
import com.htoyama.likit.domain.user.User
import com.twitter.sdk.android.core.models.Tweet as TwitterTweet
import com.twitter.sdk.android.core.models.User as TwitterUser

/**
 * Extensions which create instance for testing.
 */

fun likedTweet(
    tweet: Tweet = tweet(),
    tagIdList: List<Long> = emptyList()
) = LikedTweet(
    tweet,
    tagIdList
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

fun user(
    id: Long = 0,
    name: String = "name",
    screenName: String = "screen name",
    avatarUrl: String = "avatar url"
) = User(id, name, screenName, avatarUrl)

fun twitterTweet(
    id: Long = 1,
    createdAt: String = "Tue Sep 04 15:55:52 +0000 2012",
    text: String = "text",
    user: TwitterUser = tweetUser()
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

fun tweetUser(
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
