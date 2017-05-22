package com.egugue.licol.domain.user

/**
 * Representing a user whose tweet was liked
 */
data class User(
    val id: Long,
    val name: String,
    val screenName: String,
    val avatorUrl: String,
    val likedTweetIdList: List<Long>
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