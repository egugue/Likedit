package com.htoyama.likit

import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.models.User

/**
 * Extensions which creates instance for testing.
 */

fun twitterTweet(
    id: Long = 1,
    createdAt: String = "Tue Sep 04 15:55:52 +0000 2012",
    text: String = "text",
    user: User = tweetUser()
) = Tweet(
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
) = User(
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

