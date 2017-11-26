package com.github.rozag.redux.notes

import android.content.Context
import android.content.Intent
import android.content.res.Resources

fun Resources.statusBarHeight(): Int {
    val resId = getIdentifier("status_bar_height", "dimen", "android")
    return if (resId > 0) getDimensionPixelSize(resId) else 0
}

fun <T> Context.start(cls: Class<T>) {
    startActivity(Intent(this, cls))
}