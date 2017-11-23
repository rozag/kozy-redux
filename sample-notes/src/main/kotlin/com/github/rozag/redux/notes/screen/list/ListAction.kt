package com.github.rozag.redux.notes.screen.list

import com.github.rozag.redux.notes.Action
import com.github.rozag.redux.notes.model.Note

sealed class ListAction : Action.Screen() {

    sealed class LoadNotes : ListAction() {
        class Started : LoadNotes()
        data class Complete(val notes: List<Note>) : LoadNotes()
        class Error : LoadNotes()
    }

    class TearDown : ListAction()

}