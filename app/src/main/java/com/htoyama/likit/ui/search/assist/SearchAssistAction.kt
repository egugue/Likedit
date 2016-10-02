package com.htoyama.likit.ui.search.assist

import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tag.TagRepository
import com.htoyama.likit.domain.user.User
import com.htoyama.likit.domain.user.UserRepository
import com.htoyama.likit.ui.search.SearchScope
import com.htoyama.likit.ui.search.assist.Assist
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@SearchScope
internal class SearchAssistAction @Inject constructor(
    private val userRepository: UserRepository,
    private val tagRepository: TagRepository
) {

  fun getAssist(query: String): Single<Assist> {
    val tagQuery: Single<List<Tag>> = tagRepository.findByNameContaining(query)
        .subscribeOn(Schedulers.io())

    val userQuery: Single<List<User>> = userRepository.findByNameContaining(query)
        .subscribeOn(Schedulers.io())

    return tagQuery.zipWith(userQuery, BiFunction<List<Tag>, List<User>, Assist> { tags, users ->
      Assist.from(tags, users)
    })
  }
}