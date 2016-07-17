package com.htoyama.likit.data.liked.tweet.cache

import io.realm.RealmObject

/**
 * Created by toyamaosamuyu on 2016/07/03.
 */
open class RealmUrl(
    open var url: String = "",
    open var displayUrl: String = "",
    open var start: Int = 0,
    open var end: Int = 0
) : RealmObject()