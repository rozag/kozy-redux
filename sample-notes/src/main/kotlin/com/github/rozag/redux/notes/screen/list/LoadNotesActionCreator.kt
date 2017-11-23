package com.github.rozag.redux.notes.screen.list

import com.github.rozag.kueue.Kueue
import com.github.rozag.redux.notes.ActionCreator
import com.github.rozag.redux.notes.Store
import com.github.rozag.redux.notes.repository.NotesRepository
import timber.log.Timber

class LoadNotesActionCreator(
        private val queue: Kueue,
        private val store: Store,
        private val repo: NotesRepository
) : ActionCreator {

    override fun createAndDispatch() {
        store.dispatch(ListAction.LoadNotes.Started())
        queue.fromCallable { repo.getNotes() }
                .onComplete { notes ->
                    store.dispatch(ListAction.LoadNotes.Success(notes))
                }
                .onError { throwable ->
                    Timber.e(throwable)
                    store.dispatch(ListAction.LoadNotes.Error())
                }
                .go()
    }

}