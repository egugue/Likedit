package com.htoyama.likit.ui.search

import com.htoyama.likit.domain.tag.TagRepository
import com.htoyama.likit.domain.user.UserRepository
import rx.Observable
import javax.inject.Inject

@SearchScope
internal class SearchAssistAction @Inject constructor(
    private val userRepository: UserRepository,
    private val tagRepository: TagRepository
) {

  fun getAssist(query: String): Observable<Assist> {
    return tagRepository.findByNameContaining(query)
        .zipWith(userRepository.findByNameContaining(query),
            { tagList, userList ->
              Assist.from(tagList, userList)
            })
  }
}