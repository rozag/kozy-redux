package com.github.rozag.redux.notes.router

import com.github.rozag.redux.notes.NotesAction

fun routerReducer(state: RouterState, action: NotesAction.RouterAction): RouterState = when (action) {
    is RouterAction.Open -> openReducer(state, action)
    is RouterAction.Shown -> shownReducer(state, action)
    else -> throw NotImplementedError("Never happens")
}

private fun openReducer(state: RouterState, action: RouterAction.Open): RouterState = when (action) {
    is RouterAction.Open.List -> RouterState(state.currentScreen, RouterState.Screen.LIST)
    is RouterAction.Open.Edit -> RouterState(state.currentScreen, RouterState.Screen.EDIT)
}

private fun shownReducer(state: RouterState, action: RouterAction.Shown): RouterState = when (action) {
    is RouterAction.Shown.List -> RouterState(RouterState.Screen.LIST, RouterState.Screen.NONE)
    is RouterAction.Shown.Edit -> RouterState(RouterState.Screen.EDIT, RouterState.Screen.NONE)
}