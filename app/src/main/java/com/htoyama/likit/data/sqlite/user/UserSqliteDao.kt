package com.htoyama.likit.data.sqlite.user

import com.htoyama.likit.domain.user.User
import rx.Observable
import javax.inject.Inject

class UserSqliteDao @Inject constructor(
    private val userGateway: UserTableGateway
){

  fun searchByNameOrScreenName(name: String, screenName: String, limit: Int): Observable<List<User>> {
    return userGateway.selectByNameOrScreenName(name, screenName, limit)
        .map { it.map { it.toUser() } }
  }
}