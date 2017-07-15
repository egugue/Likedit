package com.egugue.licol.ui.usertweet

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.egugue.licol.App
import com.egugue.licol.R
import com.egugue.licol.application.likedtweet.LikedTweetAppService
import com.egugue.licol.common.extensions.*
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.user.User
import com.egugue.licol.ui.common.StateLayout
import com.egugue.licol.ui.common.base.BaseActivity
import com.egugue.licol.ui.common.customtabs.CustomTabActivityHelper
import com.egugue.licol.ui.common.recyclerview.DividerItemDecoration
import com.egugue.licol.ui.home.liked.LikedTweetListController
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import javax.inject.Inject

/**
 * An activity which shows liked tweets of a specific user.
 */
class UserTweetActivity : BaseActivity() {

  companion object {

    /**
     * Create an intent with given args
     */
    fun createIntent(context: Context, user: User): Intent {
      val intent = Intent(context, UserTweetActivity::class.java)
      intent.putExtra("userId", user.id)
      intent.putExtra("userName", user.name)
      return intent
    }
  }

  private val perPage = 20
  private val userId: Long by lazy { intent.extras.getLong("userId") }

  @Inject lateinit var likedTweetAppService: LikedTweetAppService
  @Inject lateinit var customTabHelper: CustomTabActivityHelper

  private val store: Store by lazy { ViewModelProviders.of(this)[Store::class.java] }
  private val actions: Actions by lazy { Actions(likedTweetAppService, store) }

  lateinit var listController: LikedTweetListController

  @BindView(R.id.toolbar)
  lateinit var toolbar: Toolbar

  @BindView(R.id.state_layout)
  lateinit var stateLayout: StateLayout

  @BindView(R.id.liked_tweet_list)
  lateinit var recyclerView: RecyclerView

  @BindView(R.id.home_user_error_state)
  lateinit var errorView: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.user_tweet_activity)
    ButterKnife.bind(this)

    DaggerComponent.builder()
        .appComponent(App.component(this))
        .build()
        .inject(this)

    stateLayout.showProgress()
    initToolbar()
    initListView()
    initErrorView()
    customTabHelper.setUpCustomTabService(this)

    if (store.requireData()) {
      actions.loadListItem(userId, store.nextPage(), perPage)
    }
  }

  private fun initToolbar() {
    toolbar.title = intent.extras.getString("userName")
    initBackToolbar(toolbar)
  }

  private fun initErrorView() {
    store.error
        .bindToLifecycle(this)
        .subscribe {
          //TODO
          stateLayout.showError()
        }
  }

  private fun initListView() {
    listController = LikedTweetListController()
    recyclerView.adapter = listController.adapter
    recyclerView.addItemDecoration(DividerItemDecoration(this))
    recyclerView.loadMoreEvent(object : LoadMorePredicate {
      override fun isLoading(): Boolean = store.isLoadingMore.value
      override fun hasLoadedItems(): Boolean = store.hasLoadCompleted()
    })
        .bindToLifecycle(this)
        .subscribe { actions.loadListItem(userId, store.nextPage(), perPage) }

    store.listData
        .bindToLifecycle(this)
        .subscribe {
          if (it.isEmpty()) {
            stateLayout.showEmptyState()
          } else {
            listController.setData(it)
            listController.setLoadingMoreVisibility(false)
            listController.requestModelBuild()
            stateLayout.showContent()
          }
        }

    store.isLoadingMore
        .bindToLifecycle(this)
        .subscribe {
          listController.setLoadingMoreVisibility(it)
          listController.requestModelBuild()
        }

    listController.callbacks = object : LikedTweetListController.AdapterCallbacks {
      override fun onTweetLinkClicked(url: String) =
          openLink(url, customTabHelper.session)

      override fun onWholeTweetClicked(likedTweet: LikedTweet, user: User) {
        toast("on whole tweet clicked")
      }

      override fun onTweetUserAvatarClicked(user: User) { /* do nothing*/
      }

      override fun onTweetPhotoClicked(photo: Photo) {
        toast("on photo clicked")
      }
    }
  }
}
