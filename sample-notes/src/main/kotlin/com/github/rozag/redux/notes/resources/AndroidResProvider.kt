package com.github.rozag.redux.notes.resources

import android.content.Context

class AndroidResProvider(private val context: Context) : ResProvider {

    override fun getString(stringId: Int): String = context.getString(stringId)

}