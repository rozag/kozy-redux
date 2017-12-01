package com.github.rozag.redux.notes.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
        tableName = DbContract.Table.Notes.name,
        indices = arrayOf(Index(value = DbContract.Table.Notes.Column.id, unique = true))
)
data class NoteEntity(
        @PrimaryKey @ColumnInfo(
                name = DbContract.Table.Notes.Column.id,
                typeAffinity = ColumnInfo.TEXT
        )
        var id: String = "",

        @ColumnInfo(
                name = DbContract.Table.Notes.Column.title,
                typeAffinity = ColumnInfo.TEXT
        )
        var title: String = "",

        @ColumnInfo(
                name = DbContract.Table.Notes.Column.body,
                typeAffinity = ColumnInfo.TEXT
        )
        var body: String = "",

        @ColumnInfo(
                name = DbContract.Table.Notes.Column.isTodo,
                typeAffinity = ColumnInfo.INTEGER
        )
        var isTodo: Int = 0
)