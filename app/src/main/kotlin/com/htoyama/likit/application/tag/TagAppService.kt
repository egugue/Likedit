package com.htoyama.likit.application.tag

import com.htoyama.likit.data.liked.LikedRealmGateway
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tag.TagRepository
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * An application service related to Tag
 */
open class TagAppService @Inject internal constructor(
    private val tagRepository: TagRepository,
    private val likedRealmGateway: LikedRealmGateway
) {

  /**
   * Register new Tag
   *
   * @param name the name stored as [Tag]
   * @return [Observable] which emits newly stored [Tag]
   */
  open fun registerNewTag(name: String): Observable<Tag> {
    val id = tagRepository.publishNextIdentity()
    val tag = Tag(id = id, name = name, createdAt = Date())
    return tagRepository.store(tag)
        .map { tag }
  }

  /**
   * Retrieves all stored [Tag]s as List
   */
  open fun findAll(): Observable<List<Tag>> {
    return tagRepository.findAll()
  }

  open fun findAllWithTweetCount(): Observable<List<TagTweetCountDto>> {
    return tagRepository.findAll()
        .flatMap { Observable.from(it) }
        .map { tag ->
          val count = likedRealmGateway.getCountBy(tag)
          TagTweetCountDto(tag, count)
        }
        .toList()
  }

}