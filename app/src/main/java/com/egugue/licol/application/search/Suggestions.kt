package com.egugue.licol.application.search

import com.egugue.licol.domain.user.User

/**
 * Representing search suggestions
 *
 * @param userList an [User] list
 */
data class Suggestions(
    val userList: List<User>
) {

  companion object {
    private val EMPTY = Suggestions(emptyList())

    /** Return the [Suggestions] representing empty of suggestions */
    fun empty() = EMPTY
  }
}
