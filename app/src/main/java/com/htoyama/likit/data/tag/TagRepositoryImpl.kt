package com.htoyama.likit.data.tag

import com.htoyama.likit.common.Irrelevant
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tag.TagRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * A implementation of [TagRepository]
 */
class TagRepositoryImpl @Inject internal constructor(
    private val dao: TagRealmDao
) : TagRepository {

  override fun publishNextIdentity(): Long {
    return dao.lastInsertedId() + 1
  }

  override fun findAll(): Single<List<Tag>>
      = dao.selectAll()

  override fun store(tag: Tag): Single<Any> {
    return Single.fromCallable {
      dao.insertOrUpdate(tag)
      Irrelevant.get()
    }
  }

  override fun remove(tag: Tag): Single<Any> {
    return Single.fromCallable {
      dao.delete(tag)
      Irrelevant.get()
    }
  }

  override fun findByNameContaining(part: String): Single<List<Tag>> {
    return Single.fromCallable {
      dao.selectTagListByNameContaining(part)
    }
  }

}
