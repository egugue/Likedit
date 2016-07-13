package com.htoyama.likit.data.likedtweet.cache.model

import com.htoyama.likit.data.tag.RealmTag
import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by toyamaosamuyu on 2016/07/12.
 */
open class RealmLikedTweet(
    var tweet: RealmTweet = RealmTweet(),
    var tagList: RealmList<RealmTag> = RealmList()
) : RealmObject()
