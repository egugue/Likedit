package com.egugue.licol.ui.home.user

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.egugue.licol.R
import com.egugue.licol.application.user.UserAppService
import com.egugue.licol.common.extensions.*
import com.egugue.licol.ui.common.StateLayout
import com.egugue.licol.ui.common.recyclerview.DividerItemDecoration
import com.egugue.licol.ui.home.HomeActivity
import com.egugue.licol.ui.home.user.list.UserController
import com.egugue.licol.ui.usertweet.UserTweetActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import timber.log.Timber
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
  @Inject lateinit var listController: UserController

  private var page: Int = 1
  private var isLoading = false
  private var hasLoadedItems = false

  @BindView(R.id.home_user_state_layout) lateinit var stateLayout: StateLayout
  @BindView(R.id.home_user_list) lateinit var listView: RecyclerView
  @BindView(R.id.home_user_error_state) lateinit var errorView: TextView

  override fun onAttach(context: Context) {
    super.onAttach(context)
    (activity as HomeActivity).component
        .inject(this)
  }

  override fun onCreateView(inf: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    val view = inf.inflate(R.layout.home_user_fragment, container, false)
    ButterKnife.bind(this, view)
    return view
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initListView()
    getMoreUserList()
  }

  private fun initListView() {
    listView.adapter = listController.adapter
    listView.layoutManager = LinearLayoutManager(activity)
    listView.addItemDecoration(DividerItemDecoration(activity))

    listView.loadMoreEvent(object : LoadMorePredicate {
      override fun isLoading(): Boolean = isLoading
      override fun hasLoadedItems(): Boolean = hasLoadedItems
    })
        .bindToLifecycle(this)
        .subscribe { getMoreUserList() }

    listController.userClickListener = { startActivity(UserTweetActivity.createIntent(activity, it)) }
  }

  private fun getMoreUserList() {
    appService.getAllUsers(page = page, perPage = 20)
        .subscribeOnIo()
        .observeOnMain()
        .bindToLifecycle(this)
        .doOnSubscribe {
          isLoading = true
          if (page == 1) {
            stateLayout.showProgress()
          } else {
            listController.setLoadingMoreVisibility(true)
            listController.requestModelBuild()
          }
        }
        .doOnEach { isLoading = false }
        .subscribe(
            { userList ->

              if (userList.isEmpty()) {
                hasLoadedItems = true
                if (page == 1) {
                  stateLayout.showEmptyState()
                } else {
                  listController.setLoadingMoreVisibility(false)
                  listController.requestModelBuild()
                }
              } else {
                page++
                listController.setLoadingMoreVisibility(false)
                listController.addData(userList)
                listController.requestModelBuild()
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