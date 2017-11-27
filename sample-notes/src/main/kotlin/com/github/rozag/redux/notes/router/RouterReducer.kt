@file:Suppress("UNUSED_PARAMETER")

package com.github.rozag.redux.notes.router

import com.github.rozag.redux.notes.NotesAction

fun routerReducer(state: RouterState, action: NotesAction.RouterAction): RouterState = when (action) {
    is RouterAction.Open -> RouterState(state.currentScreen, action.screen)
    is RouterAction.Shown -> RouterState(action.screen, Screen.NONE)
    is RouterAction.Closed -> RouterState(
            if (action.screen == Screen.EDIT) Screen.LIST else Screen.NONE,
            Screen.NONE
    )
    else -> throw NotImplementedError("Never happens")
}