package com.github.rozag.redux.notes

import com.github.rozag.redux.core.ReduxAction

sealed class Action : ReduxAction {
    class TearDown : Action()
    open class Screen : Action()
}