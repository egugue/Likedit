package com.htoyama.likit.ui.home.tag

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog

/**
 * A [DialogFragment] that has responsi
 */
class TagCreateDialogFragment : DialogFragment() {

  companion object {
    fun show(activity: FragmentActivity) {
      val f = TagCreateDialogFragment()
      f.show(activity.supportFragmentManager, TagCreateDialogFragment::class.java.simpleName)
    }
  }

  interface OnClickListener {
    fun onTagCreateButtonClick()
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return AlertDialog.Builder(context)
        .setTitle("Create a tag")
        .setMessage("foo")
        .setPositiveButton("Create", { dialog, which ->
          if (which == DialogInterface.BUTTON_POSITIVE) {
            val activity = activity;
            if (activity is OnClickListener) {
              activity.onTagCreateButtonClick()
            }
          }
        })
        .setNegativeButton("Cancel", null)
        .create()
  }

}