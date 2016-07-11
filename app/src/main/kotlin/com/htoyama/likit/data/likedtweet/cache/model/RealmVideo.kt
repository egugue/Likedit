package com.htoyama.likit.data.likedtweet.cache.model

import io.realm.RealmObject

/**
 * Created by toyamaosamuyu on 2016/07/03.
 */
open class RealmVideo(
    open var url: String = "",
    open var medium: RealmSize = RealmSize()
) : RealmObject()
