package com.egugue.licol.common.extensions

import android.support.v4.app.Fragment
import android.widget.Toast

fun Fragment.toast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
