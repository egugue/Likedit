package com.htoyama.likit.ui.home.tag

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.htoyama.likit.R

/**
 *
 */
class HomeTagFragment : Fragment() {

  companion object {
    fun new(): HomeTagFragment = HomeTagFragment()
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater!!.inflate(R.layout.fragment_home_tag, container, false)
  }

}
