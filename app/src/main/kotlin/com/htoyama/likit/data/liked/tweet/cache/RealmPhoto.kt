package com.htoyama.likit.data.liked.tweet.cache

import io.realm.RealmObject

/**
 * A photo stored to Realm.
 */
open class RealmPhoto(
    open var url: String = "",
    open var medium: RealmSize = RealmSize()
) : RealmObject()