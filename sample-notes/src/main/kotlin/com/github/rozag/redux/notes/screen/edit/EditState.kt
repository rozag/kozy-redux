package com.github.rozag.redux.notes.screen.edit

import com.github.rozag.redux.notes.model.Note

data class EditState(val note: Note) {
    companion object {
        val EMPTY: EditState = EditState(Note.EMPTY)
    }
}