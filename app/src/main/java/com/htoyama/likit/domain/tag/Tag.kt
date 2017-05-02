package com.htoyama.likit.domain.tag

import java.util.*

/**
 * A tag that describes a liked Tweet.
 */
class Tag(
    val id: Long,
    name: String,
    var createdAt: Date,
    val tweetIdList: List<Long>
) {

  var name: String = ""
    set(name) {
      if (name.length > 15) {
        throw IllegalArgumentException(
            "name must be within 15 length. but was " + name.length)
      }
      field = name
    }

  init {
    this.name = name
  }

  override fun equals(other: Any?): Boolean{
    if (this === other) return true
    if (other?.javaClass != javaClass) return false

    other as Tag

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int{
    return id.hashCode()
  }

}