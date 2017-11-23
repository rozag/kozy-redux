package com.github.rozag.redux.notes

import com.github.rozag.redux.core.ReduxState
import com.github.rozag.redux.notes.screen.edit.EditState
import com.github.rozag.redux.notes.screen.list.ListState

data class AppState(
        val listState: ListState,
        val editState: EditState
) : ReduxState {
    companion object {
        val EMPTY: AppState = AppState(ListState.EMPTY, EditState.EMPTY)
    }
}