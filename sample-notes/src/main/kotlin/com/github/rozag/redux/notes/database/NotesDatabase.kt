package com.github.rozag.redux.notes.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(NotesDao::class), version = DB_VERSION)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}