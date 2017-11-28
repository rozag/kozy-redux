package com.github.rozag.redux.notes.repo

import com.github.rozag.kueue.Kueue
import com.github.rozag.redux.notes.database.NoteEntity
import com.github.rozag.redux.notes.database.NotesDao
import com.github.rozag.redux.notes.model.Note

class LocalNotesRepo(
        private val notesDao: NotesDao,
        private val taskQueue: Kueue
) : NotesRepo {

    override fun getNotes(onComplete: (List<Note>) -> Unit, onError: (Throwable) -> Unit) {
        taskQueue.fromCallable { notesDao.getNotes().map(::noteFromEntity) }
                .onComplete(onComplete)
                .onError(onError)
                .go()
    }

    override fun addNote(note: Note, onComplete: (Note) -> Unit, onError: (Throwable) -> Unit) {
        taskQueue.fromCallable {
            notesDao.addNote(entityFromNote(note))
            note
        }
                .onComplete(onComplete)
                .onError(onError)
                .go()
    }

    override fun addNotes(notes: List<Note>, onComplete: (List<Note>) -> Unit, onError: (Throwable) -> Unit) {
        taskQueue.fromCallable { notesDao.addNotes(notes.map(::entityFromNote)); notes }
                .onComplete(onComplete)
                .onError(onError)
                .go()
    }

    override fun updateNote(note: Note, onComplete: (Note) -> Unit, onError: (Throwable) -> Unit) {
        taskQueue.fromCallable {
            notesDao.updateNote(entityFromNote(note))
            note
        }
                .onComplete(onComplete)
                .onError(onError)
                .go()
    }

    override fun deleteNote(note: Note, onComplete: () -> Unit, onError: (Throwable) -> Unit) {
        taskQueue.fromCallable { notesDao.deleteNote(entityFromNote(note)) }
                .onComplete { onComplete() }
                .onError(onError)
                .go()
    }

}

private fun noteFromEntity(entity: NoteEntity): Note = Note(entity.id, entity.title, entity.body)

private fun entityFromNote(note: Note): NoteEntity = NoteEntity(note.id, note.title, note.body)