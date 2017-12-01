package com.github.rozag.redux.notes.database

import android.arch.persistence.room.*
import android.database.Cursor

@Dao
interface NotesDao {

    @Query(value = "SELECT * FROM ${DbContract.Table.Notes.name}")
    fun getNotes(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(note: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNotes(notes: List<NoteEntity>)

    @Update
    fun updateNote(note: NoteEntity)

    @Delete
    fun deleteNote(note: NoteEntity)

}