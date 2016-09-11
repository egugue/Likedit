package com.htoyama.likit.data.liked.tweet.cache

import io.realm.RealmObject

/**
 * A size stored to Realm.
 */
open class RealmSize(
    open var width: Int = 0,
    open var height: Int = 0
) : RealmObject()