package com.htoyama.likit.data.user

import com.htoyama.likit.data.liked.tweet.cache.RealmUser
import com.htoyama.likit.domain.user.User
import io.realm.Case
import io.realm.Realm
import io.realm.Sort

import javax.inject.Inject

/**
 * A Data Access Object handling User Realm Database.
 */
class UserRealmDao @Inject constructor(
) : UserMapper {

  /**
   * Retrieve some users which have name containing the given arg.
   */
  fun selectByNameContaining(part: String): List<User> {
    Realm.getDefaultInstance().use { realm ->
      val realmList = realm.where(RealmUser::class.java)
          .contains("name", part, Case.INSENSITIVE)
          .findAllSorted("name", Sort.ASCENDING)

      return realmList.map { realmUser -> mapFrom(realmUser) }
    }
  }
}
