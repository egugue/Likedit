package com.htoyama.likit.data.user

import com.htoyama.likit.data.liked.tweet.cache.RealmUser
import com.htoyama.likit.domain.user.User

/**
 * A trait transforming between [User] and [RealmUser].
 */
interface UserMapper {

  /**
   * Transform the given [User] into [RealmUser].
   */
  fun mapFrom(user: User): RealmUser {
    return RealmUser(
        id = user.id,
        name = user.name,
        screenName = user.screenName,
        avatorUrl = user.avatorUrl
    )
  }

  /**
   * Transform the given [RealmUser] into [User].
   */
  fun mapFrom(realmUser: RealmUser): User {
    return User(
        id = realmUser.id,
        name = realmUser.name,
        screenName = realmUser.screenName,
        avatorUrl = realmUser.avatorUrl
    )
  }

}