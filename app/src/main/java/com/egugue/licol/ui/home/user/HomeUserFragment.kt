package com.egugue.licol.ui.home.user

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.egugue.licol.R
import com.egugue.licol.application.user.UserAppService
import com.egugue.licol.common.extensions.*
import com.egugue.licol.ui.common.StateLayout
import com.egugue.licol.ui.home.HomeActivity
import com.egugue.licol.ui.home.user.list.UserController
import com.egugue.licol.ui.usertweet.UserTweetActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
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

  lateinit private var store: Store
  private val actions: Actions by lazy { Actions(appService, store) }

  private val perPage = 20
  lateinit private var unbinder: Unbinder

  @BindView(R.id.home_user_state_layout)
  lateinit var stateLayout: StateLayout

  @BindView(R.id.home_user_list)
  lateinit var listView: RecyclerView

  @BindView(R.id.home_user_error_state)
  lateinit var errorView: TextView

  override fun onAttach(context: Context) {
    super.onAttach(context)
    (activity as HomeActivity).component
        .inject(this)

    store = ViewModelProviders.of(activity)[Store::class.java]
  }

  override fun onCreateView(inf: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    val view = inf.inflate(R.layout.home_user_fragment, container, false)
    unbinder = ButterKnife.bind(this, view)
    return view
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    stateLayout.showProgress()
    initListView()
    initErrorView()
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    if (store.requireData()) {
      actions.fetchMoreUserList(store.nextPage(), perPage)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    unbinder.unbind()
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
            stateLayout.showEmptyState()
          } else {
            listController.setData(it)
            listController.setLoadingMoreVisibility(false)
            listController.requestModelBuild()
            stateLayout.showContent()
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