package com.github.rozag.redux.notes.database

import android.arch.persistence.room.*

@Dao
interface NotesDao {

    @Query(value = "SELECT * FROM $TABLE_NOTES")
    fun getNotes(): List<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(note: NoteEntity)

    @Update
    fun updateNote(note: NoteEntity)

    @Delete
    fun deleteNote(note: NoteEntity)

}