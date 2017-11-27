package com.github.rozag.redux.notes.screen.list

import com.github.rozag.redux.notes.NotesAction
import com.github.rozag.redux.notes.model.Note

sealed class ListAction : NotesAction.Screen() {

    sealed class LoadNotes : ListAction() {
        class Started : LoadNotes()
        data class Complete(val notes: List<Note>) : LoadNotes()
        class Error : LoadNotes()
    }

    class ErrorShown : ListAction()

    data class Create(val note: Note) : ListAction()

    data class Edit(val note: Note) : ListAction()

    data class DismissNote(val index: Int) : ListAction()

    class NoteDeleted : ListAction()

    class TearDown : ListAction()

}