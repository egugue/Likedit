package com.htoyama.likit.application.tag

import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tag.TagRepository
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * An application service related to Tag
 */
class TagAppService @Inject internal constructor(
    private val tagRepository: TagRepository) {

  /**
   * Register new Tag
   *
   * @param name the name stored as [Tag]
   * @return [Observable] which emits newly stored [Tag]
   */
  fun registerNewTag(name: String): Observable<Tag> {
    val id = tagRepository.publishNextIdentity()
    val tag = Tag(id = id, name = name, createdAt = Date())
    return tagRepository.store(tag)
        .map { tag }
  }

}