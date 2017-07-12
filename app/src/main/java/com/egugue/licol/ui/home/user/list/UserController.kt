package com.egugue.licol.ui.home.user.list

import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.egugue.licol.domain.user.User
import com.egugue.licol.ui.common.recyclerview.ProgressModel

class UserController : EpoxyController() {

  var userClickListener: ((User) -> Unit)? = null

  private val progressModel: ProgressModel = ProgressModel()
  private var userList: List<User> = emptyList()
  private var requireLoadingMore: Boolean = true

  fun setData(l: List<User>) {
    userList = l
  }

  fun setLoadingMoreVisibility(requireLoadingMore: Boolean) {
    this.requireLoadingMore = requireLoadingMore
  }

  override fun buildModels() {
    userList.forEach { user ->
      UserModel(user)
          .setOnItemClickListener(View.OnClickListener { userClickListener?.invoke(user) })
          .id(user.id)
          .addTo(this)
    }

    progressModel
        .id(-1)
        .addIf(requireLoadingMore, this)
  }
}
