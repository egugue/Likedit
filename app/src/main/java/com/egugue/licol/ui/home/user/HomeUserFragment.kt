package com.egugue.licol.ui.home.user

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.egugue.licol.R
import com.egugue.licol.application.user.UserAppService
import com.egugue.licol.common.extensions.LoadMorePredicate
import com.egugue.licol.common.extensions.loadMoreEvent
import com.egugue.licol.ui.home.HomeActivity
import com.egugue.licol.ui.home.user.list.UserController
import com.egugue.licol.ui.usertweet.UserTweetActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.home_user_fragment.home_user_list
import kotlinx.android.synthetic.main.home_user_fragment.home_user_state_layout
import javax.inject.Inject

/**
 * A fragment showing user's list
 */
class HomeUserFragment : RxFragment() {

  companion object {
    /** Create a [HomeUserFragment] */
    fun new(): HomeUserFragment {
      return HomeUserFragment()
    }
  }

  @Inject lateinit var appService: UserAppService

  private val store: Store by lazy { ViewModelProviders.of(activity)[Store::class.java] }
  private val actions: Actions by lazy { Actions(appService, store) }

  private val perPage = 20

  override fun onAttach(context: Context) {
    super.onAttach(context)
    (activity as HomeActivity).component
        .inject(this)
  }

  override fun onCreateView(inf: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View
      = inf.inflate(R.layout.home_user_fragment, container, false)

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    home_user_state_layout.showProgress()
    initListView()
    initErrorView()
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    if (store.requireData()) {
      actions.fetchMoreUserList(store.nextPage(), perPage)
    }
  }

  private fun initErrorView() {
    store.error
        .bindToLifecycle(this)
        .subscribe {
          //TODO
          home_user_state_layout.showError()
        }
  }

  private fun initListView() {
    val listView = home_user_list
    val listController = UserController()
    val layoutManager = LinearLayoutManager(activity)
    listView.adapter = listController.adapter
    listView.layoutManager = layoutManager
    listView.addItemDecoration(DividerItemDecoration(activity, layoutManager.orientation))

    store.isLoadingMore
        .bindToLifecycle(this)
        .subscribe {
          listController.setLoadingMoreVisibility(it)
          listController.requestModelBuild()
        }

    store.listData
        .bindToLifecycle(this)
        .subscribe {
          if (it.isEmpty()) {
            home_user_state_layout.showEmptyState()
          } else {
            listController.setData(it)
            listController.setLoadingMoreVisibility(false)
            listController.requestModelBuild()
            home_user_state_layout.showContent()
          }
        }

    listView.loadMoreEvent(object : LoadMorePredicate {
      override fun isLoading(): Boolean = store.isLoadingMore.value
      override fun hasLoadedItems(): Boolean = store.hasLoadCompleted()
    })
        .bindToLifecycle(this)
        .subscribe {
          actions.fetchMoreUserList(store.nextPage(), perPage)
        }

    listController.userClickListener = { startActivity(UserTweetActivity.createIntent(activity, it)) }
  }
}