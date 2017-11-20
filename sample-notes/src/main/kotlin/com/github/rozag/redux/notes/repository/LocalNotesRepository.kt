package com.github.rozag.redux.notes.repository

import com.github.rozag.redux.notes.database.NoteEntity
import com.github.rozag.redux.notes.database.NotesDao
import com.github.rozag.redux.notes.model.Note

class LocalNotesRepository(private val notesDao: NotesDao) : NotesRepository {

    override fun getNotes(): List<Note> = notesDao.getNotes().map(::noteFromEntity)

    override fun addNote(note: Note) = notesDao.addNote(entityFromNote(note))

    override fun updateNote(note: Note) = notesDao.updateNote(entityFromNote(note))

    override fun deleteNote(note: Note) = notesDao.deleteNote(entityFromNote(note))

}

private fun noteFromEntity(entity: NoteEntity): Note = Note(entity.id, entity.title, entity.body)

private fun entityFromNote(note: Note): NoteEntity = NoteEntity(note.id, note.title, note.body)