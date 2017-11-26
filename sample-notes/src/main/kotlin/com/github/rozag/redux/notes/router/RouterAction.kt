package com.github.rozag.redux.notes.router

import com.github.rozag.redux.notes.NotesAction

sealed class RouterAction : NotesAction.RouterAction() {

    sealed class Open : RouterAction() {
        class List : Open()
        class Edit : Open()
    }

    sealed class Shown : RouterAction() {
        class List : Shown()
        class Edit : Shown()
    }

    sealed class Closed : RouterAction() {
        class List : Closed()
        class Edit : Closed()
    }

}