package com.github.rozag.redux.notes.model

data class TodoItem(
        val id: String,
        val noteId: String,
        val text: String,
        val done: Boolean
)