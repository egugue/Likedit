package com.egugue.licol.application.search

import com.egugue.licol.application.likedtweet.LikedTweetPayload
import com.egugue.licol.common.extensions.subscribeOnIo
import com.egugue.licol.domain.likedtweet.LikedTweet
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
  fun getSearchSuggestions(searchQuery: String): Observable<Suggestions> {
    return userRepository.findByNameContaining(searchQuery)
        .map {
          if (it.isEmpty()) {
            Suggestions.empty()
          }
          else {
            Suggestions(it)
          }
        }
        .subscribeOnIo()
  }

  /**
   * Retrieve search result by the given query
   */
  fun getSearchResult(searchQuery: String, page: Int, perPage: Int): Observable<List<LikedTweetPayload>> {
    //TODO
    return likedTweetRepository.find(page, perPage)
        .flatMap(
            { userRepository.findByIdList(it.map { it.userId }) },
            { likedTweetList, userList -> LikedTweetPayload.listFrom(likedTweetList, userList) }
        )
  }
}