package com.github.rozag.redux.notes.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(
        entities = arrayOf(NoteEntity::class),
        version = DbContract.version,
        exportSchema = true
)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}