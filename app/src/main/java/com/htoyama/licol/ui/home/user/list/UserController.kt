package com.htoyama.licol.ui.home.user.list

import com.airbnb.epoxy.EpoxyController
import com.htoyama.licol.domain.user.User
import javax.inject.Inject

class UserController @Inject constructor() : EpoxyController() {
  private var userList: List<User> = emptyList()
  private var requireLoadingMore: Boolean = true

  fun setData(l: List<User>, requireLoadingMore: Boolean) {
    userList = l
    this.requireLoadingMore = requireLoadingMore
    requestModelBuild()
  }

  override fun buildModels() {
    userList.forEach {
      UserModel(it)
          .id(it.id)
          .addTo(this)
    }
  }
}
