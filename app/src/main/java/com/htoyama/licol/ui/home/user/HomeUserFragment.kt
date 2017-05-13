package com.htoyama.licol.ui.home.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htoyama.licol.R
import com.htoyama.licol.ui.home.HomeActivity
import com.trello.rxlifecycle2.components.support.RxFragment

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

  override fun onAttach(context: Context) {
    super.onAttach(context)
    (activity as HomeActivity).component
        .inject(this)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val root = inflater!!.inflate(R.layout.fragment_home_tag, container, false)
    return root
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }
}