package com.egugue.licol.ui.usertweet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import butterknife.bindView
import com.egugue.licol.App
import com.egugue.licol.R
import com.egugue.licol.application.likedtweet.LikedTweetAppService
import com.egugue.licol.common.extensions.*
import com.egugue.licol.domain.likedtweet.LikedTweet
import com.egugue.licol.domain.tweet.media.Photo
import com.egugue.licol.domain.user.User
import com.egugue.licol.ui.common.StateLayout
import com.egugue.licol.ui.common.activity.BaseActivity
import com.egugue.licol.ui.common.recyclerview.DividerItemDecoration
import com.egugue.licol.ui.home.liked.LikedTweetListController
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import timber.log.Timber
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

  private val toolbar: Toolbar by bindView(R.id.toolbar)
  private val stateLayout: StateLayout by bindView(R.id.state_layout)
  private val recyclerView: RecyclerView by bindView(R.id.liked_tweet_list)
  private val userId: Long by lazy { intent.extras.getLong("userId") }

  private var isLoading = false
  private var hasLoadedItems = false
  private var page = 1

  @Inject lateinit var likedTweetAppService: LikedTweetAppService
  @Inject lateinit var listController: LikedTweetListController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.user_tweet_activity)

    DaggerComponent.builder()
        .appComponent(App.component(this))
        .build()
        .inject(this)

    initToolbar()
    initListView()
    loadMoreLikedTweet()
  }

  private fun initToolbar() {
    toolbar.title = intent.extras.getString("userName")
    initBackToolbar(toolbar)
  }

  private fun initListView() {
    recyclerView.adapter = listController.adapter
    recyclerView.addItemDecoration(DividerItemDecoration(this))
    recyclerView.addOnLoadMoreListener(object : LoadMoreListener {
      override fun onLoadMore() = loadMoreLikedTweet()
      override fun isLoading(): Boolean = isLoading
      override fun hasLoadedItems(): Boolean = hasLoadedItems
    })

    listController.callbacks = object : LikedTweetListController.AdapterCallbacks {
      override fun onTweetLinkClicked(url: String) {
        toast("url clicked $url")
      }

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

  private fun loadMoreLikedTweet() {
    assert(userId != -1L)

    likedTweetAppService.getAllLikedTweetsByUserId(userId, page, 10)
        .bindToLifecycle(this)
        .subscribeOnIo()
        .observeOnMain()
        .doOnSubscribe {
          isLoading = true
          if (page == 1) {
            stateLayout.showProgress()
          } else {
            listController.setLoadingMoreVisibility(true)
          }
        }
        .doOnEach { isLoading = false }
        .subscribe(
            { payloadList ->
              listController.setLoadingMoreVisibility(false)

              if (payloadList.isEmpty()) {
                hasLoadedItems = true
                if (page == 1) {
                  stateLayout.showEmptyState()
                }
              } else {
                page++
                listController.addData(payloadList)
                stateLayout.showContent()
              }
            },
            { error ->
              //TODO
              Timber.e(error)
            }
        )
  }
}
