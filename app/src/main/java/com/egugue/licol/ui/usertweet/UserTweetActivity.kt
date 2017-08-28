package com.egugue.licol.ui.usertweet

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.egugue.licol.App
import com.egugue.licol.R
import com.egugue.licol.application.likedtweet.LikedTweetAppService
import com.egugue.licol.common.extensions.LoadMorePredicate
import com.egugue.licol.common.extensions.loadMoreEvent
import com.egugue.licol.common.extensions.openLink
import com.egugue.licol.common.extensions.toast
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.user.User
import com.egugue.licol.ui.common.base.BaseActivity
import com.egugue.licol.ui.common.customtabs.CustomTabActivityHelper
import com.egugue.licol.ui.home.liked.LikedTweetListController
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.user_tweet_activity.liked_tweet_list
import kotlinx.android.synthetic.main.user_tweet_activity.state_layout
import kotlinx.android.synthetic.main.user_tweet_activity.toolbar
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
  private val userName: String by lazy { intent.extras.getString("userName") }

  @Inject lateinit var likedTweetAppService: LikedTweetAppService
  @Inject lateinit var customTabHelper: CustomTabActivityHelper

  private val store: Store by lazy { ViewModelProviders.of(this)[Store::class.java] }
  private val actions: Actions by lazy { Actions(likedTweetAppService, store) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.user_tweet_activity)

    DaggerComponent.builder()
        .appComponent(App.component(this))
        .build()
        .inject(this)

    state_layout.showProgress()
    initToolbar()
    initListView()
    initErrorView()
    customTabHelper.setUpCustomTabService(this)

    if (store.requireData()) {
      actions.loadListItem(userId, store.nextPage(), perPage)
    }
  }

  private fun initToolbar() {
    toolbar.title = userName
    initBackToolbar(toolbar)
  }

  private fun initErrorView() {
    store.error
        .bindToLifecycle(this)
        .subscribe {
          //TODO
          state_layout.showError()
        }
  }

  private fun initListView() {
    val layoutManager = LinearLayoutManager(this)
    val listController = LikedTweetListController()
    liked_tweet_list.adapter = listController.adapter
    liked_tweet_list.layoutManager = layoutManager
    liked_tweet_list.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))

    liked_tweet_list.loadMoreEvent(object : LoadMorePredicate {
      override fun isLoading(): Boolean = store.isLoadingMore.value
      override fun hasLoadedItems(): Boolean = store.hasLoadCompleted()
    })
        .bindToLifecycle(this)
        .subscribe { actions.loadListItem(userId, store.nextPage(), perPage) }

    store.listData
        .bindToLifecycle(this)
        .subscribe {
          if (it.isEmpty()) {
            state_layout.showEmptyState()
          } else {
            listController.setData(it)
            listController.setLoadingMoreVisibility(false)
            listController.requestModelBuild()
            state_layout.showContent()
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
