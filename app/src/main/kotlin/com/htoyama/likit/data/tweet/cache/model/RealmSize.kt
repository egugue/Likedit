package com.htoyama.likit.data.tweet.cache.model

import io.realm.RealmObject

/**
 * Created by toyamaosamuyu on 2016/07/03.
 */
open class RealmSize(
    open var width: Int = 0,
    open var height: Int = 0
) : RealmObject()
