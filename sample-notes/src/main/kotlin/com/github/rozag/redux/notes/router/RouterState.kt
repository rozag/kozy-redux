package com.github.rozag.redux.notes.router

data class RouterState(
        val currentScreen: Screen,
        val screenToOpen: Screen
) {

    enum class Screen {
        None,
        List,
        Edit
    }

    companion object {
        val EMPTY: RouterState = RouterState(Screen.None, Screen.None)
    }

}