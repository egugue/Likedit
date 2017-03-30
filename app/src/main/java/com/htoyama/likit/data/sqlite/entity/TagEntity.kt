package com.htoyama.likit.data.sqlite.entity

import com.htoyama.likit.data.sqlite.TagModel

data class TagEntity(
    val id: Long,
    val name: String,
    val created: Long
) : TagModel {

  override fun id(): Long = id
  override fun name(): String = name
  override fun created(): Long = created

  companion object {
    val FACTORY = TagModel.Factory(TagModel.Creator(::TagEntity))
  }
}
