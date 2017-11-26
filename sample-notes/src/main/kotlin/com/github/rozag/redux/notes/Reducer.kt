package com.github.rozag.redux.notes

import com.github.rozag.redux.notes.router.routerReducer
import com.github.rozag.redux.notes.screen.edit.EditAction
import com.github.rozag.redux.notes.screen.edit.editReducer
import com.github.rozag.redux.notes.screen.list.ListAction
import com.github.rozag.redux.notes.screen.list.ListState
import com.github.rozag.redux.notes.screen.list.listReducer

fun rootReducer(state: AppState, action: NotesAction): AppState = when (action) {
    is NotesAction.FirstLaunch -> firstLaunchReducer(state, action)
    is NotesAction.TearDown -> AppState.EMPTY
    is NotesAction.Screen -> when (action) {
        is ListAction -> AppState(
                state.routerState,
                listReducer(state.listState, action),
                state.editState
        )
        is EditAction -> AppState(
                state.routerState,
                state.listState,
                editReducer(state.editState, action)
        )
        else -> throw NotImplementedError("Never happens")
    }
    is NotesAction.RouterAction -> AppState(
            routerReducer(state.routerState, action),
            state.listState,
            state.editState
    )
}

fun firstLaunchReducer(state: AppState, action: NotesAction.FirstLaunch): AppState = when (action) {
    is NotesAction.FirstLaunch.Started -> AppState(
            state.routerState,
            ListState(true, false, state.listState.notes),
            state.editState
    )
    is NotesAction.FirstLaunch.Complete -> AppState(
            state.routerState,
            ListState(false, false, action.notes),
            state.editState
    )
}