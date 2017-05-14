package com.htoyama.licol.ui.home.user

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.htoyama.licol.R
import com.htoyama.licol.application.user.UserAppService
import com.htoyama.licol.common.extensions.LoadMoreListener
import com.htoyama.licol.common.extensions.addOnLoadMoreListener
import com.htoyama.licol.common.extensions.observeOnMain
import com.htoyama.licol.common.extensions.subscribeOnIo
import com.htoyama.licol.ui.common.StateLayout
import com.htoyama.licol.ui.home.HomeActivity
import com.htoyama.licol.ui.home.user.list.UserController
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
  @Inject lateinit var userController: UserController

  private var page: Int = 1
  private var isLoading = false
  private var hasLoadedItems = false
  private val stateLayout: StateLayout by bindView(R.id.home_user_state_layout)
  private val listView: RecyclerView by bindView(R.id.home_user_list)
  private val errorView: TextView by bindView(R.id.home_user_error_state)

  override fun onAttach(context: Context) {
    super.onAttach(context)
    (activity as HomeActivity).component
        .inject(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val root = inflater.inflate(R.layout.home_user_fragment, container, false)
    return root
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initListView()
    getMoreUserList()
  }

  private fun initListView() {
    listView.adapter = userController.adapter
    listView.layoutManager = LinearLayoutManager(activity)
    userController.requestModelBuild()

    listView.addOnLoadMoreListener(object : LoadMoreListener {
      override fun onLoadMore() = getMoreUserList()
      override fun isLoading(): Boolean = isLoading
      override fun hasLoadedItems(): Boolean = hasLoadedItems
    })
  }

  private fun getMoreUserList() {
    appService.getAllUsers(page = page, perPage = 20)
        .bindToLifecycle(this)
        .subscribeOnIo()
        .observeOnMain()
        .doOnSubscribe {
          isLoading = true
          userController.setLoadingMoreVisibility(true)
        }
        .doOnEach { isLoading = false }
        .subscribe(
            { userList ->
              userController.setLoadingMoreVisibility(false)

              if (userList.isEmpty()) {
                hasLoadedItems = true
                if (page == 1) {
                  stateLayout.showEmptyState()
                }
              } else {
                page++
                userController.addData(userList)
                stateLayout.showContent()
              }
            },
            { error ->
              error.printStackTrace()
            }
        )
  }
}