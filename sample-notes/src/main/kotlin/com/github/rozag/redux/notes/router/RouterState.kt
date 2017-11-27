package com.github.rozag.redux.notes.router

data class RouterState(
        val currentScreen: Screen,
        val screenToOpen: Screen
) {
    companion object {
        val EMPTY: RouterState = RouterState(Screen.NONE, Screen.NONE)
    }
}