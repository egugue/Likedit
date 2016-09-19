package com.htoyama.likit.data.user

import com.htoyama.likit.domain.user.User

import javax.inject.Inject

/**
 * Created by toyamaosamuyu on 2016/09/19.
 */
class UserRealmDao
@Inject constructor() {

  fun selectByNameContaining(part: String): List<User> {

  }
}
