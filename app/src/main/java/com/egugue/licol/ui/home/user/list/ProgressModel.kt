package com.egugue.licol.ui.home.user.list

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.egugue.licol.R

/**
 * A [EpoxyModel] representing loading
 */
class ProgressModel : EpoxyModelWithHolder<ProgressModel.Holder>() {

  override fun createNewHolder(): Holder = Holder()

  override fun getDefaultLayout(): Int = R.layout.centered_progress_bar

  class Holder : EpoxyHolder() {
    override fun bindView(itemView: View) {}
  }
}