package com.htoyama.likit.ui.home.tag

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.htoyama.likit.R
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

/**
 * A [DialogFragment] that has responsi
 */
class TagCreateDialogFragment : DialogFragment() {

  companion object {
    fun show(fragment: Fragment) {
      val f = TagCreateDialogFragment()
      f.setTargetFragment(fragment, 0)
      f.show(fragment.fragmentManager, TagCreateDialogFragment::class.java.simpleName)
    }
  }

  interface OnClickListener {
    fun onTagCreateButtonClick(tagName: String)
  }

  private var dis: Disposable = Disposables.empty()
  private lateinit var inputLayout: TextInputLayout

  private val tagNameMaxCount: Int by lazy { resources.getInteger(R.integer.tag_name_max_count) }
  private val countOverErrorMessage: String by lazy {
    resources.getString(R.string.tag_create_count_over_error)
  }
  private val dialogTitle: String by lazy { resources.getString(R.string.tag_create_dialog_title) }
  private val dialogPositive: String by lazy {
    resources.getString(R.string.tag_create_dialog_positive)
  }
  private val dialogNegative: String by lazy {
    resources.getString(R.string.tag_create_dialog_negative)
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = AlertDialog.Builder(context)
        .setTitle(dialogTitle)
        .setView(createContentView())
        .setPositiveButton(dialogPositive, null)
        .setNegativeButton(dialogNegative, null)
        .create()

    return dialog
  }

  override fun onStart() {
    super.onStart()

    val button = (dialog as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE)
    button?.setOnClickListener {
      val tagName = getTagName()
      val length = tagName.length
      val isNoInput = length <= 0
      val isOverLength = tagNameMaxCount < length

      if (isNoInput) {
        Toast.makeText(context, R.string.tag_create_no_input_error, Toast.LENGTH_SHORT).show()

      } else if (isOverLength) {
        Toast.makeText(context, countOverErrorMessage, Toast.LENGTH_SHORT).show()

      } else {
        (targetFragment as OnClickListener).onTagCreateButtonClick(tagName)
        dismiss()
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    dis.dispose()
  }

  private fun createContentView(): View {
    val view = LayoutInflater.from(context).inflate(R.layout.dialog_tag_create, null)
    inputLayout = view.findViewById(R.id.tag_create_input_layout) as TextInputLayout
    val editText: EditText = view.findViewById(R.id.tag_create_edit_text) as EditText

    val countOverErrorMessage = resources.getString(R.string.tag_create_count_over_error)
    dis = RxTextView.textChangeEvents(editText)
        .map {event ->
          val length = event.text().length
          length > tagNameMaxCount
        }
        .subscribe { isOver ->
          if (isOver) {
            inputLayout.error = countOverErrorMessage
            inputLayout.isErrorEnabled = true
          } else {
            inputLayout.error = ""
            inputLayout.isErrorEnabled = false
          }
        }

    return view
  }

  private fun getTagName() = inputLayout
        .editText
        ?.text
        .toString()

}