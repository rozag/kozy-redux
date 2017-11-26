package com.github.rozag.redux.notes.screen.edit

import com.github.rozag.redux.notes.AppState
import com.github.rozag.redux.notes.model.Note
import com.github.rozag.redux.notes.screen.list.ListState

fun editReducer(state: AppState, action: EditAction): AppState = when (action) {
    is EditAction.NoteUpdated -> AppState(
            state.routerState,
            ListState(
                    state.listState.isLoading,
                    state.listState.isError,
                    updateNoteInList(state.listState.notes, action.note)
            ),
            EditState.EMPTY
    )
    is EditAction.TearDown -> AppState(
            state.routerState,
            state.listState,
            EditState.EMPTY
    )
}

private fun updateNoteInList(notes: List<Note>, updatedNote: Note): List<Note> {
    return notes.map { note ->
        if (note.id == updatedNote.id) {
            updatedNote
        } else {
            note
        }
    }.toList()
}