package com.egugue.licol.data

import com.egugue.licol.common.Contract
import com.egugue.licol.data.sqlite.user.UserSqliteDao
import com.egugue.licol.domain.user.User
import com.egugue.licol.domain.user.UserRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * A implementation of [UserRepository]
 */
class UserRepositoryImpl internal @Inject constructor(
    private val userSqliteDao: UserSqliteDao
) : UserRepository {

  override fun findByUserId(userId: Long): Observable<User> {
    Contract.require(userId >= 0, "userId >= 0 required but it was $userId")
    return userSqliteDao.selectUserById(userId)
  }

  override fun findAll(page: Int, perPage: Int): Observable<List<User>> {
    Contract.require(page > 0, "0 < page required but it was $page")
    Contract.require(perPage > 0, "0 < perPage required but it was $perPage")
    return userSqliteDao.selectAllOrderedByLikedTweetCount(page, perPage)
  }

  override fun findByIdList(idList: List<Long>): Observable<List<User>> {
    return userSqliteDao.selectByIdList(idList)
  }

  override fun findByNameContaining(part: String): Observable<List<User>> {
    val limit = 10
    return userSqliteDao.searchByNameOrScreenName(part, part, limit)
  }
}