package com.github.rozag.redux.notes.screen.list

import android.os.Bundle
import com.github.rozag.redux.notes.R
import com.github.rozag.redux.notes.ReduxActivity
import com.github.rozag.redux.notes.State

class ListActivity : ReduxActivity() {

    override val layoutResourceId = R.layout.activity_main
    override val toolbarTitleId = R.string.app_name
    override val displayHomeAsUp = false
    override val homeButtonEnabled = false

    private val loadNotesActionCreator = LoadNotesActionCreator(store)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadNotesActionCreator.createAndDispatch()
    }

    override fun onStop() {
        super.onStop()
        store.dispatch(ListAction.TearDown())
    }

    override fun onNewState(state: State) {
        // TODO
    }

}