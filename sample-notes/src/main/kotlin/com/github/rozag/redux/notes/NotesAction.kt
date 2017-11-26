package com.github.rozag.redux.notes

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.notes.model.Note

sealed class NotesAction : ReduxAction {

    sealed class FirstLaunch : NotesAction() {
        class Started : FirstLaunch()
        data class Complete(val notes: List<Note>) : FirstLaunch()
    }

    class TearDown : NotesAction()

    open class Screen : NotesAction()

    open class RouterAction : NotesAction()

}