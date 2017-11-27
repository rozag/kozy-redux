package com.github.rozag.redux.notes.router

import com.github.rozag.redux.notes.NotesAction

sealed class RouterAction : NotesAction.RouterAction() {
    data class Open(val screen: com.github.rozag.redux.notes.router.Screen) : RouterAction()
    data class Shown(val screen: com.github.rozag.redux.notes.router.Screen) : RouterAction()
    data class Closed(val screen: com.github.rozag.redux.notes.router.Screen) : RouterAction()
}