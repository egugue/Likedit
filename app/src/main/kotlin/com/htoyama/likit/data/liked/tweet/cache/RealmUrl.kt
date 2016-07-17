package com.htoyama.likit.data.liked.tweet.cache

import io.realm.RealmObject

/**
 * A url stored to Realm.
 */
open class RealmUrl(
    open var url: String = "",
    open var displayUrl: String = "",
    open var start: Int = 0,
    open var end: Int = 0
) : RealmObject()