package com.egugue.licol.data.sqlite.user

import com.egugue.licol.common.extensions.toV2Observable
import com.egugue.licol.data.sqlite.lib.*
import com.squareup.sqlbrite.BriteDatabase
import io.reactivex.Observable
import javax.inject.Inject

class UserTableGateway @Inject constructor(
    private val db: BriteDatabase
) {

  fun selectAll(page: Int, perPage: Int): Observable<List<UserEntity>> {
    val (limit, offset) = (page to perPage).toLimitAndOffset()

    val stmt = UserEntity.FACTORY.select_all(limit, offset)
    return db.createQuery(stmt)
        .mapToList { UserEntity.FACTORY.select_allMapper().map(it) }
        .toV2Observable()
  }

  fun selectByNameOrScreenName(name: String, screenName: String, limit: Int)
      : Observable<List<UserEntity>> {
    val escapedName = name.escapeForQuery()
    val escapedScreenName = screenName.escapeForQuery()

    val stmt = UserEntity.FACTORY.search_by_name_or_screen_name(
        escapedName, escapedScreenName, limit.toLong())
    return db.createQuery(stmt)
        .mapToList { UserEntity.FACTORY.search_by_name_or_screen_nameMapper().map(it) }
        .toV2Observable()
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