package com.egugue.licol.ui.tag.tweet.select

import com.egugue.licol.application.likedtweet.LikedTweetAppService
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.ui.common.base.BasePresenter
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

    appService.getAllLikedTweets(page)
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