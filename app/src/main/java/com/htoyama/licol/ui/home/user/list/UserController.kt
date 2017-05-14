package com.htoyama.licol.ui.home.user.list

import com.airbnb.epoxy.EpoxyController
import com.htoyama.licol.domain.user.User
import javax.inject.Inject

class UserController @Inject constructor() : EpoxyController() {
  private val progressModel: ProgressModel = ProgressModel()

  private var userList: List<User> = emptyList()
  private var requireLoadingMore: Boolean = true

  fun addData(l: List<User>) {
    userList = ArrayList(userList + l)
    requestModelBuild()
  }

  fun setLoadingMoreVisibility(requireLoadingMore: Boolean) {
    this.requireLoadingMore = requireLoadingMore
    requestModelBuild()
  }

  override fun buildModels() {
    userList.forEach {
      UserModel(it)
          .id(it.id)
          .addTo(this)
    }

    progressModel
        .id(-1)
        .addIf(requireLoadingMore, this)
  }
}
