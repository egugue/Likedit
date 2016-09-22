package com.htoyama.likit.ui.home.tag

import com.htoyama.likit.application.tag.TagAppService
import com.htoyama.likit.application.tag.TagTweetCountDto
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.ui.common.base.BasePresenter
import com.htoyama.likit.ui.home.HomeScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HomeScope
class HomeTagPresenter @Inject internal constructor(
    private val tagAppService: TagAppService
) : BasePresenter<HomeTagPresenter.View>() {

  interface View {
    fun showProgress()
    fun showAllTags(tagTweetCountList: List<TagTweetCountDto>)
    fun showEmptyState()
    fun goToTagTweetSelectScreen(tag: Tag)
  }

  fun loadAllTags() {
    view?.showProgress()

    disposables.add(tagAppService.findAllWithTweetCount()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { tagList ->
              if (tagList.isEmpty()) {
                view?.showEmptyState()
              } else {
                view?.showAllTags(tagList)
              }
            },
            { throwable -> throwable.printStackTrace() }
        ))
  }

  fun registerNewTag(tagName: String) {
    disposables.add(tagAppService.registerNewTag(tagName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { tag ->
              view?.goToTagTweetSelectScreen(tag)
            },
            { throwable -> throwable?.printStackTrace() }
        ))
  }

}