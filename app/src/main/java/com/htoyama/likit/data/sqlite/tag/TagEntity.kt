package com.htoyama.likit.data.sqlite.tag

import com.htoyama.likit.data.sqlite.IdMap
import com.htoyama.likit.data.sqlite.TagModel
import com.htoyama.likit.domain.tag.Tag
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
