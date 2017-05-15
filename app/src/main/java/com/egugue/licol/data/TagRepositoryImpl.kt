package com.egugue.licol.data

import com.egugue.licol.common.Contract
import com.egugue.licol.data.sqlite.tag.TagSqliteDao
import com.egugue.licol.domain.tag.Tag
import com.egugue.licol.domain.tag.TagRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

/**
 * An implementation of [TagRepository]
 */
class TagRepositoryImpl @Inject internal constructor(
    private val tagSqliteDao: TagSqliteDao
) : TagRepository {

  override fun findAll(): Observable<List<Tag>> {
    return Observable.never()
  }

  override fun findAll(page: Int, perPage: Int): Observable<List<Tag>> {
    Contract.require(page > 0, "0 < page required but it was $page")
    Contract.require(perPage in 1..200, "1 <= perPage <= 200 required but it was $perPage")

    return tagSqliteDao.selectAll(page, perPage)
  }

  override fun findByNameContaining(part: String): Observable<List<Tag>> {
    return tagSqliteDao.searchTagBy(part)
  }

  override fun store(tag: Tag): Single<Long> {
    return Single.fromCallable {
      if (tag.id == Tag.UNASSIGNED_ID) {
        tagSqliteDao.insert(tag)
      } else {
        tagSqliteDao.updateName(tag)
        tag.id
      }
    }
  }

  override fun removeById(tagId: Long): Completable {
    return Completable.fromAction {
      tagSqliteDao.deleteById(tagId)
    }
  }

  override fun publishNextIdentity(): Long {
    return 1
  }
}
