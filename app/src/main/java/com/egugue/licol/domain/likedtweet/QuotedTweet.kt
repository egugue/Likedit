package com.egugue.licol.domain.likedtweet

/**
 * Created by htoyama on 2017/05/19.
 */
data class QuotedTweet(
    val id: Long,
    val text: String,
    val userId: Long,
    val userName: String,
    val userScreenName: String,
    val userAvatarUrl: String
)