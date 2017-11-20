package com.github.rozag.redux.notes.screen.list

fun listReducer(state: ListState, action: ListAction): ListState = when (action) {
    is ListAction.LoadNotes.Started -> ListState(
            true,
            false,
            state.notes
    )
    is ListAction.LoadNotes.Success -> ListState(
            false,
            false,
            action.notes
    )
    is ListAction.LoadNotes.Error -> ListState(
            false,
            true,
            state.notes
    )
    is ListAction.TearDown -> ListState.EMPTY
}