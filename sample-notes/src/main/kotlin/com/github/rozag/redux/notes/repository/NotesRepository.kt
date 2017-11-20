package com.github.rozag.redux.notes.repository

import com.github.rozag.redux.notes.model.Note

interface NotesRepository {
    fun getNotes(): List<Note>
    fun addNote(note: Note)
    fun updateNote(note: Note)
    fun deleteNote(note: Note)
}