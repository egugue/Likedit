package com.egugue.licol.ui.usertweet

import com.egugue.licol.application.likedtweet.LikedTweetAppService
import com.egugue.licol.common.extensions.observeOnMain
import com.egugue.licol.common.extensions.subscribeOnIo

class Actions(
    private val service: LikedTweetAppService,
    private val store: Store
) {

  fun loadListItem(userId: Long, page: Int, perPage: Int) {
    service.getAllLikedTweetsByUserId(userId, page, perPage)
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
