package com.github.rozag.redux.notes.router

data class RouterState(
        val currentScreen: Screen,
        val screenToOpen: Screen
) {

    enum class Screen {
        NONE,
        LIST,
        EDIT
    }

    companion object {
        val EMPTY: RouterState = RouterState(Screen.NONE, Screen.NONE)
    }

}