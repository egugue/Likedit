package com.egugue.licol.common.extensions

import android.app.Activity
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.widget.Toast


fun Activity.toast(message: String)
    = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

@ColorInt
fun Activity.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)

