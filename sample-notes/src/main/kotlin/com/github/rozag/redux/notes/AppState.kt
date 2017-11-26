package com.github.rozag.redux.notes

import com.github.rozag.redux.core.ReduxState
import com.github.rozag.redux.notes.router.RouterState
import com.github.rozag.redux.notes.screen.edit.EditState
import com.github.rozag.redux.notes.screen.list.ListState

data class AppState(
        val routerState: RouterState,
        val listState: ListState,
        val editState: EditState
) : ReduxState {
    companion object {
        val EMPTY: AppState = AppState(RouterState.EMPTY, ListState.EMPTY, EditState.EMPTY)
    }
}