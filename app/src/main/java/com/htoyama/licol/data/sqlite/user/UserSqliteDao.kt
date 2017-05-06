package com.htoyama.licol.data.sqlite.user

import com.htoyama.licol.common.AllOpen
import com.htoyama.licol.domain.user.User
import io.reactivex.Observable
import javax.inject.Inject

@AllOpen
class UserSqliteDao @Inject constructor(
    private val userGateway: UserTableGateway
) {

  /**
   * Select All [Users]s as list by the given args
   */
  fun selectAll(page: Int, perPage: Int): Observable<List<User>> {
    return userGateway.selectAll(page, perPage)
        .map { it.map { it.toUser() } }
  }

  /**
   * Select some users which have name or screen name with part of the given each arg.
   */
  fun searchByNameOrScreenName(name: String, screenName: String, limit: Int): Observable<List<User>> {
    return userGateway.selectByNameOrScreenName(name, screenName, limit)
        .map { it.map { it.toUser() } }
  }
}