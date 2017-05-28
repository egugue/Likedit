package com.egugue.licol.application.search

import com.egugue.licol.common.extensions.subscribeOnIo
import com.egugue.licol.domain.likedtweet.LikedTweetRepository
import com.egugue.licol.domain.user.UserRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * An application service related to search
 */
class SearchAppService @Inject constructor(
    private val likedTweetRepository: LikedTweetRepository,
    private val userRepository: UserRepository
) {

  /**
   * Retrieve search suggestions by the given query
   */
  fun getSearchSuggestion(searchQuery: String): Observable<Suggestions> {
    return userRepository.findByNameContaining(searchQuery)
        .map { Suggestions(it) }
        .subscribeOnIo()
  }

  /**
   * Retrieve search result by the given query
   */
  fun getSearchResult(searchQuery: String, page: Int, perPage: Int) {
  }
}