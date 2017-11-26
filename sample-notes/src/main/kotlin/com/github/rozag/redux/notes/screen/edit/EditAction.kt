package com.github.rozag.redux.notes.screen.edit

import com.github.rozag.redux.notes.NotesAction
import com.github.rozag.redux.notes.model.Note

sealed class EditAction : NotesAction.Screen() {

    data class NoteUpdated(val note: Note) : EditAction()

    class TearDown : EditAction()

}