package com.egugue.licol.ui.home.liked

import com.airbnb.epoxy.EpoxyController
import com.egugue.licol.application.likedtweet.LikedTweetPayload
import com.egugue.licol.ui.common.recyclerview.ProgressModel
import javax.inject.Inject

/**
 * An [EpoxyController] which handles [LikedTweetPayload]
 */
class LikedTweetListController @Inject constructor() : EpoxyController()  {

  private val progressModel = ProgressModel()

  private var tweetUserList: List<LikedTweetPayload> = emptyList()
  private var requireLoadingMore: Boolean = true

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
          .id(likedTweet.id)
          .addTo(this)
    }

    progressModel
        .id(-1)
        .addIf(requireLoadingMore, this)
  }
}