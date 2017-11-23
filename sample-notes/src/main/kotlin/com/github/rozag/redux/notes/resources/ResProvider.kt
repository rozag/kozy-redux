package com.github.rozag.redux.notes.resources

import android.support.annotation.StringRes

interface ResProvider {
    fun getString(@StringRes stringId: Int): String
}