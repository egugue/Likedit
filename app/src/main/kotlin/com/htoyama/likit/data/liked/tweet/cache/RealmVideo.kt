package com.htoyama.likit.data.liked.tweet.cache

import io.realm.RealmObject

/**
 * A video stored to Realm.
 */
open class RealmVideo(
    open var url: String = "",
    open var medium: RealmSize = RealmSize()
) : RealmObject()
