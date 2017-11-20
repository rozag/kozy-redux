package com.github.rozag.redux.notes.screen.list

import com.github.rozag.redux.notes.ActionCreator
import com.github.rozag.redux.notes.Store
import com.github.rozag.redux.notes.repository.NotesRepository
import java.util.concurrent.Executor

class LoadNotesActionCreator(
        private val workExecutor: Executor,
        private val callbackExecutor: Executor,
        private val store: Store,
        private val repo: NotesRepository
) : ActionCreator {

    override fun createAndDispatch() {
        store.dispatch(ListAction.LoadNotes.Started())
        workExecutor.execute {
            val notes = repo.getNotes()
            callbackExecutor.execute {
                store.dispatch(ListAction.LoadNotes.Success(notes))
            }
        }
    }

}