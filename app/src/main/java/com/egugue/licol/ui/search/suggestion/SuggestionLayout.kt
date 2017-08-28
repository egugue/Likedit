package com.egugue.licol.ui.search.suggestion

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.egugue.licol.R
import com.egugue.licol.application.search.Suggestions
import com.egugue.licol.domain.user.User
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.search_user_suggestion_item.user_avatar
import kotlinx.android.synthetic.main.search_user_suggestion_item.user_name
import kotlinx.android.synthetic.main.search_user_suggestion_item.user_screen_name

class SuggestionLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

  init {
    orientation = VERTICAL
    dividerDrawable = context.getDrawable(R.drawable.divider)
    showDividers = SHOW_DIVIDER_MIDDLE
  }

  var binded: Suggestions = Suggestions.empty()
  var itemClickListener: ((User) -> Unit)? = null

  fun set(suggestions: Suggestions) {
    if (suggestions == binded) {
      return
    }
    binded = suggestions
    removeAllViews()

    if (suggestions.userList.isEmpty()) {
      return
    }

    val inf = LayoutInflater.from(context)
    suggestions.userList.forEach {
      val child = inf.inflate(R.layout.search_user_suggestion_item, this, false) as ViewGroup
      UserViewHolder(child)
          .bind(it, itemClickListener)
      addView(child)
    }
  }

  class UserViewHolder(override val containerView: View?): LayoutContainer {

    fun bind(user: User, listener: ((User) -> Unit)?) {
      //TODO
      containerView?.setOnClickListener { listener?.invoke(user) }

      Picasso.with(user_avatar.context)
          .load(user.avatorUrl)
          .into(user_avatar)

      user_name.text = user.name
      user_screen_name.text = "@${user.screenName}"
    }
  }
}