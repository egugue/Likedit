package com.htoyama.likit.data.sqlite.entity

import com.htoyama.likit.data.sqlite.UserModel

/**
 * An entity which represents a user who has tweeted.
 */
data class UserEntity(
    val id: Long,
    val name: String,
    val screenName: String,
    val avatarUrl: String
) : UserModel {

  override fun id(): Long = id
  override fun name(): String = name
  override fun screen_name(): String = screenName
  override fun avatar_url(): String  = avatarUrl

  companion object {
    val FACTORY = UserModel.Factory<UserEntity>(::UserEntity)
  }
}
