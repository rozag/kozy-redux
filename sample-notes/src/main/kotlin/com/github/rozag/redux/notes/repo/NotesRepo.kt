package com.github.rozag.redux.notes.repo

import com.github.rozag.redux.notes.model.Note

interface NotesRepo {
    fun getNotes(onComplete: (List<Note>) -> Unit, onError: (Throwable) -> Unit)
    fun addNote(note: Note, onComplete: (Note) -> Unit, onError: (Throwable) -> Unit)
    fun addNotes(notes: List<Note>, onComplete: (List<Note>) -> Unit, onError: (Throwable) -> Unit)
    fun updateNote(note: Note, onComplete: (Note) -> Unit, onError: (Throwable) -> Unit)
    fun deleteNote(note: Note, onComplete: () -> Unit, onError: (Throwable) -> Unit)
}