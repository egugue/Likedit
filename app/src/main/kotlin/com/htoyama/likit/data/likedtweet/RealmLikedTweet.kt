package com.htoyama.likit.data.likedtweet

import com.htoyama.likit.data.tag.RealmTag
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index

/**
 * Created by toyamaosamuyu on 2016/07/12.
 */
open class RealmLikedTweet(
    @Index var tweetId: Long = -1,
    var tagList: RealmList<RealmTag> = RealmList()
) : RealmObject()
