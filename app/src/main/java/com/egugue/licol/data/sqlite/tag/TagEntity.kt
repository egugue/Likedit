package com.egugue.licol.data.sqlite.tag

import com.egugue.licol.data.sqlite.IdMap
import com.egugue.licol.data.sqlite.TagModel
import com.egugue.licol.domain.tag.Tag
import java.util.*

data class TagEntity(
    val id: Long,
    val name: String,
    val created: Long
) : TagModel {

  override fun id(): Long = id
  override fun name(): String = name
  override fun created(): Long = created

  /**
   * Convert into a [Tag]
   */
  fun toTag(idMap: IdMap) = Tag(id, name, Date(created), idMap.getOrEmptyList(id))

  companion object {
    val FACTORY = TagModel.Factory(TagModel.Creator(::TagEntity))
  }
}
