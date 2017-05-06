package com.htoyama.licol.application.tag

import com.htoyama.licol.domain.tag.Tag
import com.htoyama.licol.domain.tag.TagRepository
import io.reactivex.Single
import java.util.*
import javax.inject.Inject
import io.reactivex.Observable

/**
 * An application service related to Tag
 */
open class TagAppService @Inject internal constructor(
    private val tagRepository: TagRepository
) {

  /**
   * Register new Tag
   *
   * @param name the name stored as [Tag]
   * @return [Observable] which emits newly stored [Tag]
   */
  open fun registerNewTag(name: String): Single<Tag> {
    val id = tagRepository.publishNextIdentity()
    val tag = Tag(id = id, name = name, createdAt = Date(), tweetIdList = emptyList())
    return tagRepository.store(tag)
        .map { tag }
  }

  /**
   * Retrieves all stored [Tag]s as List
   */
  open fun findAll(): Observable<List<Tag>> {
    return tagRepository.findAll()
  }

  // TODO
  open fun findAllWithTweetCount(): Single<List<TagTweetCountDto>> {
    return tagRepository.findAll()
        .flatMap { tagList -> Observable.fromIterable(tagList) }
        .map { tag ->
          val count = 1
          //val count = likedRealmGateway.getTweetCountBy(tag)
          TagTweetCountDto(tag, count)
        }
        .toList()
  }

}