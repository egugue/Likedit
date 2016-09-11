package com.htoyama.likit.data.liked

import com.htoyama.likit.data.liked.tweet.cache.RealmTweet
import com.htoyama.likit.data.tag.RealmTag
import io.realm.RealmList
import io.realm.RealmObject

/**
 * A Liked stored to Realm
 */
open class RealmLikedTweet(
    var tweet: RealmTweet = RealmTweet(),
    var tagList: RealmList<RealmTag> = RealmList()
) : RealmObject()
