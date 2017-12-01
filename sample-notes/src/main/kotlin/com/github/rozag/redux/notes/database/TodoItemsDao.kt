package com.github.rozag.redux.notes.database

import android.arch.persistence.room.*

@Dao
interface TodoItemsDao {

    @Query(
            value = "SELECT * " +
                    "FROM ${DbContract.Table.TodoItems.name} " +
                    "WHERE ${DbContract.Table.TodoItems.Column.noteId} = :noteId"
    )
    fun getTodoItems(noteId: String): List<TodoItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTodoItem(todoItem: TodoItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTodoItems(todoItems: List<TodoItemEntity>)

    @Update
    fun updateTodoItems(todoItems: List<TodoItemEntity>)

}