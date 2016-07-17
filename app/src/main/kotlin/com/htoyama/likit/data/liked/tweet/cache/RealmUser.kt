package com.htoyama.likit.data.liked.tweet.cache

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * A user stored to Realm
 */
open class RealmUser(
    @PrimaryKey open var id: Long = -1,
    open var name: String = "",
    open var screenName: String = "",
    open var avatorUrl: String = ""
) : RealmObject()
