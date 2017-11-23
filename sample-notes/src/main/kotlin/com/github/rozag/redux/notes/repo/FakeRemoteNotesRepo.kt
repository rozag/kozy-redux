package com.github.rozag.redux.notes.repo

import com.github.rozag.redux.notes.model.Note

class FakeRemoteNotesRepo(private val sleepMillis: Long = 500) : NotesRepo {

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