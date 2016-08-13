package com.htoyama.likit.ui.home.tag

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.htoyama.likit.R
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tag.TagRepository
import com.htoyama.likit.ui.home.HomeActivity
import com.htoyama.likit.ui.home.HomePresenter
import com.htoyama.toGone
import com.htoyama.toVisible
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 *
 */
class HomeTagFragment : Fragment(), TagCreateDialogFragment.OnClickListener, HomePresenter.View {

  companion object {
    fun new() = HomeTagFragment()
  }

  @Inject lateinit var presenter: HomePresenter
  @Inject lateinit var tagRepository: TagRepository

  private val adapter: ListAdapter = ListAdapter()
  lateinit private var listView: RecyclerView
  lateinit private var emptyState: View

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    (activity as HomeActivity).component
        .inject(this)
    presenter.view = this
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val rootView = inflater!!.inflate(R.layout.fragment_home_tag, container, false)

    listView = rootView.findViewById(R.id.home_tag_list) as RecyclerView
    listView.adapter = adapter
    listView.layoutManager = LinearLayoutManager(context)

    emptyState = rootView.findViewById(R.id.home_tag_empty_state)
    return rootView
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    tagRepository.findAll()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Subscriber<List<Tag>>() {
          override fun onError(e: Throwable?) {
            e!!.printStackTrace()
          }

          override fun onNext(tagList: List<Tag>) {
            if (tagList.isEmpty()) {
              listView.toGone()
              emptyState.toVisible()
              return
            }

            listView.toVisible()
            emptyState.toGone()
            adapter.setItemList(tagList)
          }

          override fun onCompleted() {
          }
        })
  }

  override fun onDetach() {
    presenter.view = null
    super.onDetach()
  }

  /** Called when FAB on attached Activity is clicked. */
  fun onClickFab() {
    TagCreateDialogFragment.show(this)
  }

  override fun onTagCreateButtonClick(tagName: String) {
    presenter.registerNewTag(tagName)
  }

  override fun showProgress() {
    Toast.makeText(context, "showProgress", Toast.LENGTH_SHORT).show()
  }

  override fun hideProgress() {
    Toast.makeText(context, "hideProgress", Toast.LENGTH_SHORT).show()
  }

  override fun goToTagTweetSelectScreen(tag: Tag) {
    Toast.makeText(context, "goTo", Toast.LENGTH_SHORT).show()
  }

}
