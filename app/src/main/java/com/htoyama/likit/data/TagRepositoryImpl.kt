package com.htoyama.likit.data

import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tag.TagRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

/**
 * A implementation of [TagRepository]
 */
class TagRepositoryImpl @Inject internal constructor(
) : TagRepository {

  override fun findAll(): Observable<List<Tag>> {
    return Observable.never()
  }

  override fun store(tag: Tag): Single<Long> {
    return Single.never()
  }

  override fun remove(tag: Tag): Observable<Any> {
    return Observable.never()
  }

  override fun findByNameContaining(part: String): Observable<List<Tag>> {
    return Observable.never()
  }

  override fun removeById(tagId: Long): Completable {
    return Completable.never()
  }

  override fun publishNextIdentity(): Long {
    return 1
  }
}
