package com.github.rozag.redux.notes

import com.github.rozag.redux.notes.screen.edit.EditAction
import com.github.rozag.redux.notes.screen.edit.editReducer
import com.github.rozag.redux.notes.screen.list.ListAction
import com.github.rozag.redux.notes.screen.list.ListState
import com.github.rozag.redux.notes.screen.list.listReducer

fun rootReducer(state: AppState, action: Action): AppState = when (action) {
    is Action.FirstLaunch -> firstLaunchReducer(state, action)
    is Action.TearDown -> AppState.EMPTY
    is Action.Screen -> when (action) {
        is ListAction -> AppState(
                listReducer(state.listState, action),
                state.editState
        )
        is EditAction -> AppState(
                state.listState,
                editReducer(state.editState, action)
        )
        else -> throw NotImplementedError("Never happens")
    }
}

fun firstLaunchReducer(state: AppState, action: Action.FirstLaunch): AppState = when(action) {
    is Action.FirstLaunch.Started -> AppState(
            ListState(true, false, state.listState.notes),
            state.editState
    )
    is Action.FirstLaunch.Complete -> AppState(
            ListState(false, false, action.notes),
            state.editState
    )
}