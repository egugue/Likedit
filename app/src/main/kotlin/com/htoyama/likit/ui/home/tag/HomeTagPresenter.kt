package com.htoyama.likit.ui.home.tag

import com.htoyama.likit.application.tag.TagAppService
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.ui.home.HomeScope
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

@HomeScope
class HomeTagPresenter
  @Inject internal constructor(private val tagAppService: TagAppService) {

  interface View {
    fun showProgress()
    fun hideProgress()
    fun goToTagTweetSelectScreen(tag: Tag)
  }

  var view: View? = null

  fun registerNewTag(tagName: String) {
    view?.showProgress()

    tagAppService.registerNewTag(tagName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { tag ->
              view?.hideProgress()
              view?.goToTagTweetSelectScreen(tag)
            },
            { throwable -> view?.hideProgress() }
        )
  }

}