package com.htoyama.likit.data.tag

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * A [RealmObject] that describes a tag.
 */
open class RealmTag(
    @PrimaryKey var id: Long = -1,
    var name: String = "",
    var createdAt: Date = Date()
): RealmObject()
