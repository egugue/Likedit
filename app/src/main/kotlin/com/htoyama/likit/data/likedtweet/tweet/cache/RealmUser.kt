package com.htoyama.likit.data.likedtweet.tweet.cache

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by toyamaosamuyu on 2016/07/03.
 */
open class RealmUser(
    @PrimaryKey open var id: Long = -1,
    open var name: String = "",
    open var screenName: String = "",
    open var avatorUrl: String = ""
) : RealmObject()
