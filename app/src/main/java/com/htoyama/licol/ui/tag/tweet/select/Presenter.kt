package com.htoyama.licol.ui.tag.tweet.select

import com.htoyama.licol.application.likedtweet.LikedTweetAppService
import com.htoyama.licol.domain.likedtweet.LikedTweet
import com.htoyama.licol.ui.common.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@TagTweetSelectScope
internal class Presenter @Inject constructor(
  private val appService: LikedTweetAppService
) : BasePresenter<Presenter.View>() {

  interface View {
    fun showProgress()
    fun showNextLikedList(next: List<LikedTweet>)
  }

  private var page = 1

  fun loadNextLikedList() {
    view?.showProgress()

    appService.find(page)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(
          { likedList ->
            view?.showNextLikedList(likedList)
            page++
          },
           { throwable ->

          }
      )
  }

}