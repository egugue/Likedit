package com.htoyama.likit.ui.search

import com.htoyama.likit.ui.common.base.BasePresenter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

@SearchScope
internal class Presenter @Inject internal constructor(
    private val searchAssistAction: SearchAssistAction
) : BasePresenter<Presenter.View>() {

  internal interface View {
    fun showAssist(assist: Assist)
  }

  fun loadAssist(query: String) {
    val sub = searchAssistAction.getAssist(query)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { assist -> view?.showAssist(assist) },
            { throwable ->

            }
        )

    subs.add(sub)
  }

}