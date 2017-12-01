package com.github.rozag.redux.notes.database

import android.arch.persistence.room.*

@Entity(
        tableName = DbContract.Table.TodoItems.name,
        indices = arrayOf(Index(value = DbContract.Table.TodoItems.Column.noteId, unique = false)),
        foreignKeys = arrayOf(ForeignKey(
                entity = NoteEntity::class,
                parentColumns = arrayOf(DbContract.Table.Notes.Column.id),
                childColumns = arrayOf(DbContract.Table.TodoItems.Column.id),
                onDelete = ForeignKey.CASCADE
        ))
)
data class TodoItemEntity(
        @PrimaryKey @ColumnInfo(name = DbContract.Table.TodoItems.Column.id) var id: String = "",
        @ColumnInfo(name = DbContract.Table.TodoItems.Column.noteId) var noteId: String = "",
        @ColumnInfo(name = DbContract.Table.TodoItems.Column.text) var text: String = "",
        @ColumnInfo(name = DbContract.Table.TodoItems.Column.done) var done: Boolean = false
)