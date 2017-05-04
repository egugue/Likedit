package com.htoyama.likit.data.sqlite.user

import com.htoyama.likit.data.sqlite.lib.createQuery
import com.htoyama.likit.data.sqlite.lib.escapeForQuery
import com.squareup.sqlbrite.BriteDatabase
import rx.Observable
import javax.inject.Inject

class UserTableGateway @Inject constructor(
    private val db: BriteDatabase
) {

  fun selectByNameOrScreenName(name: String, screenName: String, limit: Int)
      : Observable<List<UserEntity>> {
    val escapedName = name.escapeForQuery()
    val escapedScreenName = screenName.escapeForQuery()

    val stmt = UserEntity.FACTORY.search_by_name_or_screen_name(
        escapedName, escapedScreenName, limit.toLong())
    return db.createQuery(stmt)
        .mapToList { UserEntity.FACTORY.search_by_name_or_screen_nameMapper().map(it) }
  }
}