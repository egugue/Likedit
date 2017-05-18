package com.egugue.licol.ui.home.user

import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.bindView
import com.egugue.licol.R
import com.egugue.licol.application.user.UserAppService
import com.egugue.licol.common.extensions.LoadMoreListener
import com.egugue.licol.common.extensions.addOnLoadMoreListener
import com.egugue.licol.common.extensions.observeOnMain
import com.egugue.licol.common.extensions.subscribeOnIo
import com.egugue.licol.domain.user.User
import com.egugue.licol.ui.common.BaseFragment
import com.egugue.licol.ui.common.StateLayout
import com.egugue.licol.ui.home.HomeActivity
import com.egugue.licol.ui.home.user.list.UserController
import com.trello.rxlifecycle2.components.support.RxFragment
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import javax.inject.Inject

/**
 * A fragment showing user's list
 */
class HomeUserFragment : BaseFragment() {

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
  lateinit private var viewModel: HomeUserViewModel

  override fun onAttach(context: Context) {
    super.onAttach(context)
    (activity as HomeActivity).component
        .inject(this)

    this.viewModel = ViewModelProviders.of(this).get(HomeUserViewModel::class.java)
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
    viewModel.userList.observe(this) { userList -> userList.toString() }
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

fun <T> LiveData<T>.observe(o: LifecycleRegistryOwner, f: (T?) -> Unit)
    = observe(o,  Observer<T> { f.invoke(it)})
