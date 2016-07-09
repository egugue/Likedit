package com.htoyama.likit.data.tweet.cache.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by toyamaosamuyu on 2016/07/03.
 */
open class RealmTweet(
    @PrimaryKey open var id: Long = -1,
    open var user: RealmUser = RealmUser(),
    open var text: String = "",
    open var photoList: RealmList<RealmPhoto> = RealmList(),
    open var video: RealmVideo? = null,
    open var urlList: RealmList<RealmUrl> = RealmList(),
    open var createAt: Date = Date()
) : RealmObject()
