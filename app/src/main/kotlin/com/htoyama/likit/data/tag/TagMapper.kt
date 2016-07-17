package com.htoyama.likit.data.tag

import com.htoyama.likit.domain.tag.Tag
import javax.inject.Inject

/**
 * Transform between [Tag] and [RealmTag]
 */
open class TagMapper @Inject internal constructor() {

  /**
   * Transform [Tag] into [RealmTag]
   */
  fun mapFrom(tag: Tag): RealmTag {
    return RealmTag(
        id = tag.id,
        name = tag.name,
        createdAt = tag.createdAt
    )
  }

  /**
   * Transform [RealmTag] into [Tag]
   */
  fun mapFrom(realmTag: RealmTag): Tag {
    return Tag(
        id = realmTag.id,
        name = realmTag.name,
        createdAt = realmTag.createdAt
    )
  }
}