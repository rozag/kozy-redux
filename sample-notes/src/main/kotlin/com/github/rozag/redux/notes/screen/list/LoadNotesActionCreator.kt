package com.github.rozag.redux.notes.screen.list

import com.github.rozag.redux.notes.ActionCreator
import com.github.rozag.redux.notes.Store

class LoadNotesActionCreator(
        private val store: Store
) : ActionCreator {

    override fun createAndDispatch() {
        store.dispatch(ListAction.LoadNotes.Started())
        // TODO: async loading
    }

}