package com.egugue.licol.ui.search

import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.egugue.licol.application.search.Suggestions
import javax.inject.Inject

/**
 * An [EpoxyController] handling search's [Suggestions]
 */
class SuggestionListController @Inject constructor()
  : EpoxyController() {

  private var suggestions: Suggestions = Suggestions.empty()

  /** A listener which is invoked when user suggestion view is clicked */
  var userClickListener: View.OnClickListener? = null

  /**
   * Replace the attached [Suggestions] in this controller with new [Suggestions]
   */
  fun replaceWith(s: Suggestions) {
    suggestions = s
    requestModelBuild()
  }

  override fun buildModels() {
    suggestions.userList.forEach {
      UserSuggestionEpoxyModel(it)
          .setOnItemClickListener(userClickListener)
          .id(it.id)
          .addTo(this)
    }
  }
}