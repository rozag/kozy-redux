package com.github.rozag.redux.notes

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.notes.model.Note

sealed class Action : ReduxAction {
    sealed class FirstLaunch : Action() {
        class Started : FirstLaunch()
        data class Complete(val notes: List<Note>) : FirstLaunch()
    }
    class TearDown : Action()
    open class Screen : Action()
}