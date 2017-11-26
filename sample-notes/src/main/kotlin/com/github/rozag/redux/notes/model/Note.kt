package com.github.rozag.redux.notes.model

data class Note(val id: String, val title: String, val body: String) {
    companion object {
        val EMPTY: Note = Note("", "", "")
    }
}