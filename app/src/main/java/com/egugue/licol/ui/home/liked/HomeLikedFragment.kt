package com.egugue.licol.ui.home.liked

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.egugue.licol.R
import com.egugue.licol.application.likedtweet.LikedTweetAppService
import com.egugue.licol.common.extensions.openLink
import com.egugue.licol.common.extensions.toast
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.user.User
import com.egugue.licol.ui.common.customtabs.CustomTabActivityHelper
import com.egugue.licol.ui.common.recyclerview.DividerItemDecoration
import com.egugue.licol.ui.home.HomeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 *
 */
class HomeLikedFragment : Fragment() {

  companion object {
    fun new(): HomeLikedFragment = HomeLikedFragment()
  }

  lateinit private var listView: RecyclerView
  @Inject lateinit var listController: LikedTweetListController
  @Inject lateinit var likedTweetAppService: LikedTweetAppService
  @Inject lateinit var customTabActivityHelper: CustomTabActivityHelper

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    (activity as HomeActivity)
        .component
        .inject(this)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    listView = inflater!!.inflate(R.layout.fragment_home_liked, container, false) as RecyclerView
    return listView
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initListView()

    likedTweetAppService.getAllLikedTweets(1, 200) // TODO
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { payloads ->
              listController.addData(payloads)
            }
        )
  }

  private fun initListView() {
    listView.adapter = listController.adapter
    listView.layoutManager = LinearLayoutManager(activity)
    listView.addItemDecoration(DividerItemDecoration(activity))

    listController.callbacks = object : LikedTweetListController.AdapterCallbacks {
      override fun onTweetLinkClicked(url: String) {
        openLink(url, customTabActivityHelper.session)
      }

      override fun onWholeTweetClicked(likedTweet: LikedTweet, user: User) {
        toast("onWhalte clicked")
        Timber.d("onWhalte clicked")
      }

      override fun onTweetUserAvatarClicked(user: User) {
        toast("onUserAvatar clicked")
        Timber.d("onUserAvatar clicked")
      }

      override fun onTweetPhotoClicked(photo: Photo) {
        toast("onPhoto clicked")
        Timber.d("onPhoto clicked")
      }
    }
  }
}