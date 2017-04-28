package com.htoyama.likit.data

import com.htoyama.likit.common.Irrelevant
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tag.TagRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * A implementation of [TagRepository]
 */
class TagRepositoryImpl @Inject internal constructor(
) : TagRepository {

  override fun publishNextIdentity(): Long {
    return 1
  }

  //TODO
  override fun findAll(): Single<List<Tag>>
      = Single.never()

  // TODO
  override fun store(tag: Tag): Single<Any> {
    return Single.fromCallable {
      Irrelevant.get()
    }
  }

  // TODO
  override fun remove(tag: Tag): Single<Any> {
    return Single.fromCallable {
      Irrelevant.get()
    }
  }

  // TODO
  override fun findByNameContaining(part: String): Single<List<Tag>> {
    return Single.never()
  }

}
