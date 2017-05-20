package com.egugue.licol.ui.home.liked

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.egugue.licol.R
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.user.User
import com.egugue.licol.ui.common.tweet.LikedTweetView
import timber.log.Timber

/**
 * An [EpoxyModel] which has [LikedTweet] and [User]
 */
class LikedTweetModel (
    private val likedTweet: LikedTweet,
    private val user: User
) : EpoxyModelWithHolder<LikedTweetModel.Holder>(), LikedTweetView.OnItemClickListener {

  override fun getDefaultLayout(): Int = R.layout.liked_tweet_view
  override fun createNewHolder(): Holder = Holder()

  override fun bind(holder: Holder) {
    holder.tweetView.bindItem(likedTweet, user)
    holder.tweetView.listener = this
  }

  override fun onLinkClicked(url: String) {
    Timber.d("url $url")
  }

  override fun onWholeClicked(likedTweet: LikedTweet, user: User) {
    Timber.d("on WHole")
  }

  override fun onUserAvatarClicked(user: User) {
    Timber.d("on user")
  }

  override fun onPhotoClicked(photo: Photo) {
    Timber.d("on photo")
  }

  class Holder : EpoxyHolder() {
    lateinit var tweetView: LikedTweetView

    override fun bindView(itemView: View) {
      tweetView = itemView as LikedTweetView
    }
  }
}