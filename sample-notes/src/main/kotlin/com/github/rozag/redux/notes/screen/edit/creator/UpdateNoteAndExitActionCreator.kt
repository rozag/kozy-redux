package com.github.rozag.redux.notes.screen.edit.creator

import com.github.rozag.redux.notes.NotesStore
import com.github.rozag.redux.notes.model.Note
import com.github.rozag.redux.notes.repo.NotesRepo
import com.github.rozag.redux.notes.router.RouterAction
import com.github.rozag.redux.notes.router.Screen
import com.github.rozag.redux.notes.screen.edit.EditAction
import timber.log.Timber

class UpdateNoteAndExitActionCreator(
        private val store: NotesStore,
        private val repo: NotesRepo
) {

    fun createAndDispatch(title: String, body: String) {
        val oldNote = store.getState().editState.note
        val updatedNote = Note(oldNote.id, title, body)
        repo.updateNote(
                note = updatedNote,
                onComplete = { note ->
                    store.dispatch(EditAction.NoteUpdated(note))
                    store.dispatch(EditAction.TearDown())
                    store.dispatch(RouterAction.Closed(Screen.EDIT))
                },
                onError = { throwable -> Timber.e(throwable) }
        )
    }

}