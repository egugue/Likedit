package com.egugue.licol.ui.home.liked

import com.airbnb.epoxy.EpoxyController
import com.egugue.licol.application.likedtweet.LikedTweetPayload
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.user.User
import com.egugue.licol.ui.common.recyclerview.ProgressModel
import com.egugue.licol.ui.common.tweet.LikedTweetView
import javax.inject.Inject

/**
 * An [EpoxyController] which handles [LikedTweetPayload]
 */
class LikedTweetListController @Inject constructor() : EpoxyController() {

  private val progressModel = ProgressModel()

  private var tweetUserList: List<LikedTweetPayload> = emptyList()
  private var requireLoadingMore: Boolean = true
  var callbacks: AdapterCallbacks? = null

  fun addData(l: List<LikedTweetPayload>) {
    tweetUserList = ArrayList(tweetUserList + l)
    requestModelBuild()
  }

  fun setLoadingMoreVisibility(requireLoadingMore: Boolean) {
    this.requireLoadingMore = requireLoadingMore
    requestModelBuild()
  }

  override fun buildModels() {
    tweetUserList.forEach { (likedTweet, user) ->
      LikedTweetModel(likedTweet, user)
          .setOnItemClickListener(object : LikedTweetView.OnItemClickListener {
            override fun onLinkClicked(url: String) {
              callbacks?.onTweetLinkClicked(url)
            }

            override fun onWholeClicked(likedTweet: LikedTweet, user: User) {
              callbacks?.onWholeTweetClicked(likedTweet, user)
            }

            override fun onUserAvatarClicked(user: User) {
              callbacks?.onTweetUserAvatarClicked(user)
            }

            override fun onPhotoClicked(photo: Photo) {
              callbacks?.onTweetPhotoClicked(photo)
            }
          })
          .id(likedTweet.id)
          .addTo(this)
    }

    progressModel
        .id(-1)
        .addIf(requireLoadingMore, this)
  }

  /** A listener to be invoked when each item is clicked */
  interface AdapterCallbacks {

    /** Invoked when a link is clicked */
    fun onTweetLinkClicked(url: String)

    /** Invoked when the whole view is clicked */
    fun onWholeTweetClicked(likedTweet: LikedTweet, user: User)

    /** Invoked when an user avatar image is clicked */
    fun onTweetUserAvatarClicked(user: User)

    /** Invoked when an photo is clicked */
    fun onTweetPhotoClicked(photo: Photo)
  }
}