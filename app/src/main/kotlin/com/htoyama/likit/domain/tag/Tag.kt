package com.htoyama.likit.domain.tag

import java.util.*

/**
 * A tag that describes a liked Tweet.
 */
class Tag(
    val id: Long,
    name: String,
    var createdAt: Date
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

}