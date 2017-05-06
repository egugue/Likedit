package com.htoyama.licol.ui.search

import com.htoyama.licol.common.extensions.zipWith
import com.htoyama.licol.domain.tag.TagRepository
import com.htoyama.licol.domain.user.UserRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@SearchScope
internal class SearchAssistAction @Inject constructor(
    private val userRepository: UserRepository,
    private val tagRepository: TagRepository
) {

  fun getAssist(query: String): Observable<Assist> {
    val tagQuery = tagRepository.findByNameContaining(query)
        .subscribeOn(Schedulers.io())

    val userQuery = userRepository.findByNameContaining(query)
        .subscribeOn(Schedulers.io())

    return tagQuery.zipWith(userQuery, { tags, users -> Assist.from(tags, users)})
  }
}