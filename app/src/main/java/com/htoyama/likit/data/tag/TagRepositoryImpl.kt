package com.htoyama.likit.data.tag

import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tag.TagRepository
import rx.Observable
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

  override fun findAll(): Observable<List<Tag>>
      = dao.selectAll()

  override fun store(tag: Tag): Observable<Void> {
    return Observable.fromCallable {
      dao.insertOrUpdate(tag)
      null
    }
  }

  override fun remove(tag: Tag): Observable<Void> {
    return Observable.fromCallable {
      dao.delete(tag)
      null
    }
  }

  override fun findByNameContaining(part: String): Observable<List<Tag>> {
    return Observable.fromCallable {
      dao.selectTagListByNameContaining(part)
    }
  }

}
