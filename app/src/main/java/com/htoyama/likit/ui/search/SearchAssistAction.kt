package com.htoyama.likit.ui.search

import com.htoyama.likit.data.tag.TagRealmDao
import com.htoyama.likit.data.user.UserRealmDao
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.user.User
import rx.Observable
import javax.inject.Inject

/**
 * Created by toyamaosamuyu on 2016/09/19.
 */
internal class SearchAssistAction @Inject constructor(
    private val realmUserRealmDao: UserRealmDao,
    private val tagRealmDao: TagRealmDao
){

  fun search(query: String): Observable<Dto> {

  }


  data class Dto(
      val userList: List<User>,
      val tagList: List<Tag>
  )

}