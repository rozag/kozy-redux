package com.github.rozag.redux.notes.screen.list

import com.github.rozag.redux.notes.AppState
import com.github.rozag.redux.notes.screen.edit.EditState

fun listReducer(state: AppState, action: ListAction): AppState = when (action) {
    is ListAction.LoadNotes -> AppState(
            state.routerState,
            loadNotesReducer(state.listState, action),
            state.editState
    )
    is ListAction.ErrorShown -> AppState(
            state.routerState,
            ListState(
                    state.listState.isLoading,
                    false,
                    state.listState.notes
            ),
            state.editState
    )
    is ListAction.Create -> AppState(
            state.routerState,
            ListState(
                    state.listState.isLoading,
                    state.listState.isError,
                    state.listState.notes + action.note
            ),
            EditState(action.note)
    )
    is ListAction.Edit -> AppState(
            state.routerState,
            state.listState,
            EditState(action.note)
    )
    is ListAction.TearDown -> AppState(
            state.routerState,
            ListState.EMPTY,
            state.editState
    )
}

private fun loadNotesReducer(state: ListState, action: ListAction.LoadNotes): ListState = when (action) {
    is ListAction.LoadNotes.Started -> ListState(
            state.notes.isEmpty(),
            false,
            state.notes
    )
    is ListAction.LoadNotes.Complete -> ListState(
            false,
            false,
            action.notes
    )
    is ListAction.LoadNotes.Error -> ListState(
            false,
            true,
            state.notes
    )
}