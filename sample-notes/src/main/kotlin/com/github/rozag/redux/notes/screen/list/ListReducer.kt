package com.github.rozag.redux.notes.screen.list

fun listReducer(state: ListState, action: ListAction): ListState = when (action) {
    is ListAction.LoadNotes -> loadNotesReducer(state, action)
    is ListAction.TearDown -> ListState.EMPTY
}

fun loadNotesReducer(state: ListState, action: ListAction.LoadNotes): ListState = when (action) {
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