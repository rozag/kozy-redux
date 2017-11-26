package com.github.rozag.redux.notes.router

import android.content.Context
import android.content.Intent
import com.github.rozag.redux.notes.NotesStore
import com.github.rozag.redux.notes.screen.edit.EditActivity
import com.github.rozag.redux.notes.screen.list.ListActivity

private fun <T> Context.start(cls: Class<T>) {
    startActivity(Intent(this, cls))
}

class Router(private val store: NotesStore) {

    fun showScreenIfNeeded(activityContext: Context, screen: RouterState.Screen) {
        when (screen) {
            RouterState.Screen.List -> {
                activityContext.start(ListActivity::class.java)
                store.dispatch(RouterAction.Shown.List())
            }
            RouterState.Screen.Edit -> {
                activityContext.start(EditActivity::class.java)
                store.dispatch(RouterAction.Shown.Edit())
            }
            RouterState.Screen.None -> Unit
        }
    }

}