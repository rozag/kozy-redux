package com.github.rozag.redux.notes.router

import android.content.Context
import com.github.rozag.redux.notes.NotesStore
import com.github.rozag.redux.notes.screen.edit.EditActivity
import com.github.rozag.redux.notes.screen.list.ListActivity
import com.github.rozag.redux.notes.start

class Router(private val store: NotesStore) {

    fun showScreenIfNeeded(activityContext: Context, screen: Screen) {
        when (screen) {
            Screen.LIST -> {
                activityContext.start(ListActivity::class.java)
                store.dispatch(RouterAction.Shown(Screen.LIST))
            }
            Screen.EDIT -> {
                activityContext.start(EditActivity::class.java)
                store.dispatch(RouterAction.Shown(Screen.EDIT))
            }
            Screen.NONE -> Unit
        }
    }

}