package com.htoyama.likit.data.tweet.cache.model

import io.realm.RealmObject

/**
 * Created by toyamaosamuyu on 2016/07/03.
 */
open class RealmUser(
    open var name: String = "",
    open var screenName: String = "",
    open var avatorUrl: String = ""
) : RealmObject()
