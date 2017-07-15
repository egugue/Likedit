package com.egugue.licol.ui.home.liked

import com.egugue.licol.application.likedtweet.LikedTweetAppService
import com.egugue.licol.common.extensions.observeOnMain
import com.egugue.licol.common.extensions.subscribeOnIo

class Actions(
    private val service: LikedTweetAppService,
    private val store: Store
) {

  fun loadListItem(page: Int, perPage: Int) {
    service.getAllLikedTweets(page, perPage)
        .subscribeOnIo()
        .observeOnMain()
        .doOnSubscribe { store.acceptLoading(true) }
        .doOnEach { store.acceptLoading(false) }
        .subscribe(
            { store.acceptListData(it) },
            { store.acceptError(it) }
        )
  }
}