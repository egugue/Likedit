package com.egugue.licol.ui.search.suggestion

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.egugue.licol.R
import com.egugue.licol.application.search.Suggestions
import com.egugue.licol.domain.user.User
import com.squareup.picasso.Picasso

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

  var itemClickListenr: ((User) -> Unit)? = null

  fun set(suggestions: Suggestions) {
    removeAllViews()

    if (suggestions.userList.isEmpty()) {
      return
    }

    val inf = LayoutInflater.from(context)
    suggestions.userList.map {
      val child = inf.inflate(R.layout.search_user_suggestion_item, this, false) as ViewGroup
      UserViewHolder(child)
          .bind(it, itemClickListenr)
      Log.d("ーーー", "added user")
      addView(child)
    }
  }

  class UserViewHolder(private val itemView: View) {
    @BindView(R.id.user_avatar)
    lateinit var avatarView: ImageView

    @BindView(R.id.user_name)
    lateinit var nameView: TextView

    @BindView(R.id.user_screen_name)
    lateinit var screenNameView: TextView

    init {
      ButterKnife.bind(this, itemView)
    }

    fun bind(user: User, listener: ((User) -> Unit)?) {
      //TODO
      itemView.setOnClickListener { listener?.invoke(user) }

      Picasso.with(avatarView.context)
          .load(user.avatorUrl)
          .into(avatarView)

      nameView.text = user.name
      screenNameView.text = "@${user.screenName}"
    }
  }
}