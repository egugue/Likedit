package com.htoyama.likit.ui.search.assist

import com.htoyama.likit.ui.common.base.BasePresenter
import com.htoyama.likit.ui.search.SearchScope
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@SearchScope
internal class Presenter @Inject internal constructor(
    private val searchAssistAction: SearchAssistAction
) : BasePresenter<Presenter.View>() {

  internal interface View {
    fun showAssist(assist: Assist)
  }

  fun loadAssist(query: String) {
    val dis = searchAssistAction.getAssist(query)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { assist -> view?.showAssist(assist) },
            { throwable ->

            }
        )

    disposables.add(dis)
  }

}