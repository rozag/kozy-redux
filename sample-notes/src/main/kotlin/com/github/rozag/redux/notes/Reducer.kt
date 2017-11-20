package com.github.rozag.redux.notes

import com.github.rozag.redux.notes.screen.edit.EditAction
import com.github.rozag.redux.notes.screen.edit.editReducer
import com.github.rozag.redux.notes.screen.list.ListAction
import com.github.rozag.redux.notes.screen.list.listReducer

fun rootReducer(state: State, action: Action): State = when (action) {
    is Action.TearDown -> State.EMPTY
    is Action.Screen -> when (action) {
        is ListAction -> State(
                listReducer(state.listState, action),
                state.editState
        )
        is EditAction -> State(
                state.listState,
                editReducer(state.editState, action)
        )
        else -> throw NotImplementedError("Never happens")
    }
}