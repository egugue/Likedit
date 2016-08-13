package com.htoyama.likit.ui.home.tag

import com.htoyama.likit.application.tag.TagAppService
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.ui.common.base.BasePresenter
import com.htoyama.likit.ui.home.HomeScope
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

@HomeScope
class HomeTagPresenter @Inject internal constructor(
    private val tagAppService: TagAppService
) : BasePresenter<HomeTagPresenter.View>() {

  interface View {
    fun showProgress()
    fun hideProgress()
    fun showAllTags(tagList: List<Tag>)
    fun showEmptyState()
    fun goToTagTweetSelectScreen(tag: Tag)
  }

  fun loadAllTags() {
    view?.showProgress()

    subs.add(tagAppService.findAll()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { tagList ->
              if (tagList.isEmpty()) {
                view?.showEmptyState()
              } else {
                view?.showAllTags(tagList)
              }
              view?.hideProgress()
            },
            { throwable -> throwable.printStackTrace() }
        ))
  }

  fun registerNewTag(tagName: String) {
    subs.add(tagAppService.registerNewTag(tagName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { tag ->
              view?.goToTagTweetSelectScreen(tag)
            },
            { throwable -> view?.hideProgress() }
        ))
  }

}