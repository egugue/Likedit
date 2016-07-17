package com.htoyama.likit.data.liked

import com.htoyama.likit.data.tag.RealmTag
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index

/**
 * A Liked stored to Realm
 */
open class RealmLikedTweet(
    @Index var tweetId: Long = -1,
    var tagList: RealmList<RealmTag> = RealmList()
) : RealmObject()
