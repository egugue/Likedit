package com.egugue.licol.ui.home.user

import com.egugue.licol.application.user.UserAppService
import com.egugue.licol.common.extensions.observeOnMain
import com.egugue.licol.common.extensions.subscribeOnIo

class Actions constructor(
    private val userAppService: UserAppService,
    private val store: Store
) {

  fun fetchMoreUserList(page: Int, perPage: Int) {
    userAppService.getAllUsers(page, perPage)
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