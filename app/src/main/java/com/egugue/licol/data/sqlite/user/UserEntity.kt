package com.egugue.licol.data.sqlite.user

import com.egugue.licol.data.sqlite.UserModel
import com.egugue.licol.domain.user.User

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

    fun from(u: User) = UserEntity(u.id, u.name, u.screenName, u.avatorUrl)
  }
}

fun UserEntity.toUser() = User(id, name, screenName, avatarUrl)
