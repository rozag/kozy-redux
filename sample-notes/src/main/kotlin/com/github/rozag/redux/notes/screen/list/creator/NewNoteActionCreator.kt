package com.github.rozag.redux.notes.screen.list.creator

import com.github.rozag.kueue.Kueue
import com.github.rozag.redux.notes.ActionCreator
import com.github.rozag.redux.notes.IdGenerator
import com.github.rozag.redux.notes.NotesStore
import com.github.rozag.redux.notes.model.Note
import com.github.rozag.redux.notes.repo.NotesRepo
import com.github.rozag.redux.notes.router.RouterAction
import com.github.rozag.redux.notes.screen.list.ListAction
import timber.log.Timber

class NewNoteActionCreator(
        private val idGenerator: IdGenerator,
        private val store: NotesStore,
        private val repo: NotesRepo,
        private val taskQueue: Kueue
) : ActionCreator {

    override fun createAndDispatch() {
        taskQueue.fromCallable {
            val note = Note(idGenerator.generateId(), "", "")
            repo.addNote(note)
            note
        }
                .onComplete { note ->
                    store.dispatch(ListAction.Create(note))
                    store.dispatch(RouterAction.Open.Edit())
                }
                .onError { throwable -> Timber.e(throwable) }
                .go()

    }

}