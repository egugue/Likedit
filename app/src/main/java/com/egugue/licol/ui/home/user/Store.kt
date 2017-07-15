package com.egugue.licol.ui.home.user

import android.arch.lifecycle.ViewModel
import com.egugue.licol.common.extensions.hasNotValue
import com.egugue.licol.domain.user.User
import com.jakewharton.rxrelay2.BehaviorRelay

class Store : ViewModel() {
  private var hasFirstLoadingCompleted: Boolean = false
  private var hasLoadCompleted: Boolean = false
  private var nextPage: Int = 1

  val listData: BehaviorRelay<List<User>> = BehaviorRelay.create()
  val error: BehaviorRelay<String> = BehaviorRelay.create()
  val isLoadingMore: BehaviorRelay<Boolean> = BehaviorRelay.createDefault(false)

  fun requireData(): Boolean = listData.hasNotValue()
  fun hasLoadCompleted(): Boolean = hasLoadCompleted
  fun nextPage(): Int = nextPage

  fun acceptListData(additional: List<User>) {
    nextPage++

    if (additional.isEmpty()) {
      hasLoadCompleted = true
      return
    }

    val stored = listData.value ?: emptyList()
    val newly = stored + additional
    listData.accept(newly)
  }

  fun acceptError(error: Throwable) {
    this.error.accept(error.message ?: "")
  }

  fun acceptLoading(isLoading: Boolean) {
    if (!hasFirstLoadingCompleted && !isLoading) {
      hasFirstLoadingCompleted = true
      return
    }

    if (hasFirstLoadingCompleted) {
      isLoadingMore.accept(isLoading)
    }
  }
}
