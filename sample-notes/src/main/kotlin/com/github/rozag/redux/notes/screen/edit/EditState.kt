package com.github.rozag.redux.notes.screen.edit

data class EditState(val tmp: Int) {
    companion object {
        val EMPTY: EditState = EditState(0)
    }
}