package com.github.rozag.redux.notes.screen.list.creator

import com.github.rozag.redux.notes.NotesStore
import com.github.rozag.redux.notes.model.Note
import com.github.rozag.redux.notes.repo.NotesRepo
import com.github.rozag.redux.notes.screen.list.ListAction
import timber.log.Timber

class DeleteNoteActionCreator(
        private val store: NotesStore,
        private val repo: NotesRepo
) {

    fun createAndDispatch(noteToDelete: Note) {
        repo.deleteNote(
                note = noteToDelete,
                onComplete = { store.dispatch(ListAction.NoteDeleted()) },
                onError = { throwable -> Timber.e(throwable) }
        )
    }

}