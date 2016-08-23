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
import com.htoyama.likit.application.tag.TagTweetCountDto
import com.htoyama.likit.domain.tag.Tag
import com.htoyama.likit.domain.tag.TagRepository
import com.htoyama.likit.ui.home.HomeActivity
import com.htoyama.likit.common.extensions.toast
import com.htoyama.likit.ui.common.StateLayout
import javax.inject.Inject

/**
 *
 */
class HomeTagFragment : Fragment(), TagCreateDialogFragment.OnClickListener, HomeTagPresenter.View {

  companion object {
    fun new() = HomeTagFragment()
  }

  @Inject lateinit internal var presenter: HomeTagPresenter
  @Inject lateinit var tagRepository: TagRepository

  private val adapter: ListAdapter = ListAdapter()
  lateinit private var listView: RecyclerView
  lateinit private var emptyState: View
  lateinit private var stateLayout: StateLayout

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    (activity as HomeActivity).component
        .inject(this)
    presenter.setView(this)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    stateLayout = inflater!!.inflate(R.layout.fragment_home_tag, container, false) as StateLayout

    listView = stateLayout.findViewById(R.id.home_tag_list) as RecyclerView
    listView.adapter = adapter
    listView.layoutManager = LinearLayoutManager(context)

    emptyState = stateLayout.findViewById(R.id.home_tag_empty_state)
    return stateLayout
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.loadAllTags()
  }

  override fun onDetach() {
    super.onDetach()
    presenter.unsubscribe();
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
    stateLayout.showProgress()
  }

  override fun showAllTags(tagTweetCountList: List<TagTweetCountDto>) {
    adapter.setItemList(tagList)
    stateLayout.showContent()
  }

  override fun showEmptyState() {
    stateLayout.showEmptyState()
  }

  override fun goToTagTweetSelectScreen(tag: Tag) {
    toast("完了")
  }

}
