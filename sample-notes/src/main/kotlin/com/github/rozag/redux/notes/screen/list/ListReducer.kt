@file:Suppress("UNUSED_PARAMETER")

package com.github.rozag.redux.notes.screen.list

import com.github.rozag.redux.notes.AppState
import com.github.rozag.redux.notes.model.Note
import com.github.rozag.redux.notes.screen.edit.EditState

fun listReducer(state: AppState, action: ListAction): AppState = when (action) {
    is ListAction.LoadNotes -> AppState(
            state.routerState,
            loadNotesReducer(state.listState, action),
            state.editState
    )
    is ListAction.ErrorShown -> AppState(
            state.routerState,
            errorShownReducer(state, action),
            state.editState
    )
    is ListAction.Create -> AppState(
            state.routerState,
            createReducer(state, action),
            EditState(action.note)
    )
    is ListAction.Edit -> AppState(
            state.routerState,
            state.listState,
            EditState(action.note)
    )
    is ListAction.DismissNote -> AppState(
            state.routerState,
            dismissNoteReducer(state, action),
            state.editState
    )
    is ListAction.NoteDeleted -> AppState(
            state.routerState,
            noteDeletedReducer(state, action),
            state.editState
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
            state.notes,
            state.noteToDelete
    )
    is ListAction.LoadNotes.Complete -> ListState(
            false,
            false,
            action.notes,
            state.noteToDelete
    )
    is ListAction.LoadNotes.Error -> ListState(
            false,
            true,
            state.notes,
            state.noteToDelete
    )
}

private fun errorShownReducer(state: AppState, action: ListAction.ErrorShown): ListState {
    return ListState(
            state.listState.isLoading,
            false,
            state.listState.notes,
            state.listState.noteToDelete
    )
}

private fun createReducer(state: AppState, action: ListAction.Create): ListState {
    return ListState(
            state.listState.isLoading,
            state.listState.isError,
            state.listState.notes + action.note,
            state.listState.noteToDelete
    )
}

private fun dismissNoteReducer(state: AppState, action: ListAction.DismissNote): ListState {
    return ListState(
            false,
            false,
            state.listState.notes.filterIndexed { index, _ -> index != action.index },
            state.listState.notes[action.index]
    )
}

private fun noteDeletedReducer(state: AppState, action: ListAction.NoteDeleted): ListState {
    return ListState(
            false,
            false,
            state.listState.notes,
            Note.EMPTY
    )
}