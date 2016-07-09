package com.htoyama.likit.data.tweet.cache.model

import io.realm.RealmObject

/**
 * Created by toyamaosamuyu on 2016/07/03.
 */
open class RealmPhoto(
    open var url: String = "",
    open var medium: RealmSize = RealmSize()
) : RealmObject()