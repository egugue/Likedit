package com.htoyama.licol.domain.user

/**
 * Created by toyamaosamuyu on 2016/06/28.
 */
data class User(
    val id: Long,
    val name: String,
    val screenName: String,
    val avatorUrl: String
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other?.javaClass != javaClass) return false

    other as User

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }
}