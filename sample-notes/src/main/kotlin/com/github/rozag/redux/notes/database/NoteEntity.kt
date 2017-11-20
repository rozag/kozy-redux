package com.github.rozag.redux.notes.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
        tableName = TABLE_NOTES,
        indices = arrayOf(Index(value = COLUMN_ID, unique = true))
)
data class NoteEntity(
        @PrimaryKey @ColumnInfo(name = COLUMN_ID) val id: String,
        @ColumnInfo(name = COLUMN_TITLE) val title: String,
        @ColumnInfo(name = COLUMN_BODY) val body: String
)