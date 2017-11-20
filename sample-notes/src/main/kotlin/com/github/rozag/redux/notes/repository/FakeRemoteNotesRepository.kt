package com.github.rozag.redux.notes.repository

import com.github.rozag.redux.notes.model.Note

class FakeRemoteNotesRepository(private val sleepMillis: Long = 500) : NotesRepository {

    override fun getNotes(): List<Note> {
        Thread.sleep(sleepMillis)
        return emptyList()
    }

    override fun addNote(note: Note) {
        Thread.sleep(sleepMillis)
    }

    override fun updateNote(note: Note) {
        Thread.sleep(sleepMillis)
    }

    override fun deleteNote(note: Note) {
        Thread.sleep(sleepMillis)
    }

}