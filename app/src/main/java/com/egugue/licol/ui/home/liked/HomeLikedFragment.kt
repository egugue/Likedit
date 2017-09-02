package com.egugue.licol.ui.home.liked

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.egugue.licol.R
import com.egugue.licol.application.likedtweet.LikedTweetAppService
import com.egugue.licol.common.extensions.LoadMorePredicate
import com.egugue.licol.common.extensions.loadMoreEvent
import com.egugue.licol.common.extensions.openLink
import com.egugue.licol.common.extensions.toast
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.user.User
import com.egugue.licol.ui.common.customtabs.CustomTabActivityHelper
import com.egugue.licol.ui.home.HomeActivity
import com.egugue.licol.ui.usertweet.UserTweetActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.home_liked_fragment.home_liked_list
import kotlinx.android.synthetic.main.home_liked_fragment.home_liked_state_layout
import timber.log.Timber
import javax.inject.Inject

class HomeLikedFragment : RxFragment() {

  companion object {
    fun new(): HomeLikedFragment = HomeLikedFragment()
  }

  private val perPage = 20
  @Inject lateinit var likedTweetAppService: LikedTweetAppService
  @Inject lateinit var customTabActivityHelper: CustomTabActivityHelper

  private val store: Store by lazy { ViewModelProviders.of(activity)[Store::class.java] }
  private val actions: Actions by lazy { Actions(likedTweetAppService, store) }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    (activity as HomeActivity)
        .component
        .inject(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View
      = inflater.inflate(R.layout.home_liked_fragment, container, false)

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    home_liked_state_layout.showProgress()
    initListView()
    initError()
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    if (store.requireData()) {
      actions.loadListItem(store.nextPage(), perPage)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
  }

  private fun initError() {
    store.error
        .bindToLifecycle(this)
        .subscribe {
          //TODO
          home_liked_state_layout.showError()
        }
  }

  private fun initListView() {
    val layoutManager = LinearLayoutManager(activity)
    val listController = LikedTweetListController()
    home_liked_list.adapter = listController.adapter
    home_liked_list.layoutManager = layoutManager
    home_liked_list.addItemDecoration(DividerItemDecoration(activity, layoutManager.orientation))

    store.listData
        .bindToLifecycle(this)
        .subscribe {
          if (it.isEmpty()) {
            home_liked_state_layout.showEmptyState()
          } else {
            listController.setData(it)
            listController.setLoadingMoreVisibility(false)
            listController.requestModelBuild()
            home_liked_state_layout.showContent()
          }
        }

    store.isLoadingMore
        .bindToLifecycle(this)
        .subscribe {
          listController.setLoadingMoreVisibility(it)
          listController.requestModelBuild()
        }

    home_liked_list.loadMoreEvent(object : LoadMorePredicate {
      override fun isLoading(): Boolean = store.isLoadingMore.value
      override fun hasLoadedItems(): Boolean = store.hasLoadCompleted()
    })
        .bindToLifecycle(this)
        .subscribe {
          actions.loadListItem(store.nextPage(), perPage)
        }


    listController.callbacks = object : LikedTweetListController.AdapterCallbacks {
      override fun onTweetLinkClicked(url: String) =
          openLink(url, customTabActivityHelper.session)

      override fun onWholeTweetClicked(likedTweet: LikedTweet, user: User) {
        toast("onWhalte clicked")
        Timber.d("onWhalte clicked")
      }

      override fun onTweetUserAvatarClicked(user: User) =
          startActivity(UserTweetActivity.createIntent(activity, user))

      override fun onTweetPhotoClicked(photo: Photo) {
        toast("onPhoto clicked")
        Timber.d("onPhoto clicked")
      }
    }
  }
}