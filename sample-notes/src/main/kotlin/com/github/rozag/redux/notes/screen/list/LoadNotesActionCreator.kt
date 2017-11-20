package com.github.rozag.redux.notes.screen.list

import com.github.rozag.redux.notes.ActionCreator
import com.github.rozag.redux.notes.Store
import com.github.rozag.redux.notes.repository.NotesRepository

class LoadNotesActionCreator(
        private val store: Store,
        private val repo: NotesRepository
) : ActionCreator {

    override fun createAndDispatch() {
        store.dispatch(ListAction.LoadNotes.Started())
        // TODO: async loading
    }

}