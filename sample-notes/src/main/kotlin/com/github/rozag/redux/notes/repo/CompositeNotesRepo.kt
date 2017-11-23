package com.github.rozag.redux.notes.repo

import com.github.rozag.redux.notes.model.Note

class CompositeNotesRepo(
        private val localRepo: NotesRepo,
        private val remoteRepo: NotesRepo
) : NotesRepo {

    override fun getNotes(): List<Note> {
        val remoteNotes = remoteRepo.getNotes()
        val localNotes = localRepo.getNotes()
        return localNotes.union(remoteNotes).toList()
    }

    override fun addNote(note: Note) {
        remoteRepo.addNote(note)
        localRepo.addNote(note)
    }

    override fun updateNote(note: Note) {
        remoteRepo.updateNote(note)
        localRepo.updateNote(note)
    }

    override fun deleteNote(note: Note) {
        remoteRepo.deleteNote(note)
        localRepo.deleteNote(note)
    }

}