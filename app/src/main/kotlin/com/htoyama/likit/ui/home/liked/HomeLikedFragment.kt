package com.htoyama.likit.ui.home.liked

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.htoyama.likit.R

/**
 *
 */
class HomeLikedFragment : Fragment() {

  companion object {
    fun new(): HomeLikedFragment = HomeLikedFragment()
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater!!.inflate(R.layout.fragment_home_liked, container, false)
  }

}
