package com.egugue.licol.data.sqlite.user

import android.annotation.SuppressLint
import com.egugue.licol.data.sqlite.lib.*
import com.squareup.sqlbrite2.BriteDatabase
import io.reactivex.Observable
import javax.inject.Inject

@SuppressLint("CheckResult")
class UserTableGateway @Inject constructor(
    private val db: BriteDatabase
) {

  fun selectById(userId: Long): Observable<UserEntity> {
    val stmt = UserEntity.FACTORY.select_by_id(userId)
    return db.createQuery(stmt)
        .mapToOneOrDefault({ UserEntity.FACTORY.select_by_idMapper().map(it) }, UserEntity.NONE)
        .doOnNext { if (it == UserEntity.NONE) throw NoSuchElementException("cannot find the user of id($userId)") }
  }

  fun selectAll(page: Int, perPage: Int): Observable<List<UserEntity>> {
    val (limit, offset) = (page to perPage).toLimitAndOffset()
    val stmt = UserEntity.FACTORY.select_all_order_by_liked_count(limit, offset)
    return db.createQuery(stmt)
        .mapToList { it.toUserEntity() }
  }

  fun selectByIdList(idList: List<Long>): Observable<List<UserEntity>> {
    val stmt = UserEntity.FACTORY.select_by_id_list(idList.toLongArray())
    return db.createQuery(stmt)
        .mapToList { UserEntity.FACTORY.select_by_id_listMapper().map(it) }
  }

  fun selectByNameOrScreenName(name: String, screenName: String, limit: Int)
      : Observable<List<UserEntity>> {
    val escapedName = name.escapeForQuery()
    val escapedScreenName = screenName.escapeForQuery()

    val stmt = UserEntity.FACTORY.search_by_name_or_screen_name(
        escapedName, escapedScreenName, limit.toLong())
    return db.createQuery(stmt)
        .mapToList { UserEntity.FACTORY.search_by_name_or_screen_nameMapper().map(it) }
  }

  fun insertOrUpdate(user: UserEntity) {
    insertOrUpdate(listOf(user))
  }

  fun insertOrUpdate(list: List<UserEntity>) {
    db.transaction {
      list.forEach {
        SqliteScripts.insertOrUpdateIntoUser(db, it)
      }
    }
  }

}