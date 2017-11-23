package com.github.rozag.redux.notes.repo

import com.github.rozag.redux.notes.model.Note

interface NotesRepo {
    fun getNotes(): List<Note>
    fun addNote(note: Note)
    fun updateNote(note: Note)
    fun deleteNote(note: Note)
}