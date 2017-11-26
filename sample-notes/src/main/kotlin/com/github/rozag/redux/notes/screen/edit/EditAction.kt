package com.github.rozag.redux.notes.screen.edit

import com.github.rozag.redux.notes.NotesAction

sealed class EditAction : NotesAction.Screen() {

    class TearDown : EditAction()

}