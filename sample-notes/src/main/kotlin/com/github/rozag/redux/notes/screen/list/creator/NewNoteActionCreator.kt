package com.github.rozag.redux.notes.screen.list.creator

import com.github.rozag.redux.notes.IdGenerator
import com.github.rozag.redux.notes.NotesStore
import com.github.rozag.redux.notes.model.Note
import com.github.rozag.redux.notes.repo.NotesRepo
import com.github.rozag.redux.notes.router.RouterAction
import com.github.rozag.redux.notes.router.Screen
import com.github.rozag.redux.notes.screen.list.ListAction
import timber.log.Timber

class NewNoteActionCreator(
        private val idGenerator: IdGenerator,
        private val store: NotesStore,
        private val repo: NotesRepo
) {

    fun createAndDispatch() {
        val newNote = Note(idGenerator.generateId(), "", "")
        repo.addNote(
                note = newNote,
                onComplete = { note ->
                    store.dispatch(ListAction.Create(note))
                    store.dispatch(RouterAction.Open(Screen.EDIT))
                },
                onError = { throwable -> Timber.e(throwable) }
        )
    }

}