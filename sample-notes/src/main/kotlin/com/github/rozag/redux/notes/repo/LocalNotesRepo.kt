package com.github.rozag.redux.notes.repo

import com.github.rozag.kueue.Kueue
import com.github.rozag.redux.notes.database.*
import com.github.rozag.redux.notes.model.Note
import com.github.rozag.redux.notes.model.TodoItem

class LocalNotesRepo(
        private val notesDao: NotesDao,
        private val todoItemsDao: TodoItemsDao,
        private val taskQueue: Kueue
) : NotesRepo {

    override fun getNotes(onComplete: (List<Note>) -> Unit, onError: (Throwable) -> Unit) {
        taskQueue.fromCallable {
            val cursor = notesDao.getNotes()
            val idColumnIndex = cursor.getColumnIndex(DbContract.Table.Notes.Column.id)
            val titleColumnIndex = cursor.getColumnIndex(DbContract.Table.Notes.Column.title)
            val bodyColumnIndex = cursor.getColumnIndex(DbContract.Table.Notes.Column.body)
            val isTodoColumnIndex = cursor.getColumnIndex(DbContract.Table.Notes.Column.isTodo)
            val notes = ArrayList<Note>(cursor.count)
            while (cursor.moveToNext()) {
                val id = cursor.getString(idColumnIndex)
                val title = cursor.getString(titleColumnIndex)
                val isTodo = cursor.getInt(isTodoColumnIndex) != 0
                val note = if (isTodo) {
                    val items = todoItemsDao.getTodoItems(noteId = id).map(::itemFromEntity)
                    Note.Todo(id, title, items)
                } else {
                    val body = cursor.getString(bodyColumnIndex)
                    Note.Text(id, title, body)
                }
                notes.add(note)
            }
            notes
        }
                .onComplete(onComplete)
                .onError(onError)
                .go()
    }

    override fun addNote(note: Note, onComplete: (Note) -> Unit, onError: (Throwable) -> Unit) {
        taskQueue.fromCallable {
            notesDao.addNote(entityFromNote(note))
            if (note is Note.Todo) {
                todoItemsDao.addTodoItems(note.items.map(::entityFromItem))
            }
            note
        }
                .onComplete(onComplete)
                .onError(onError)
                .go()
    }

    override fun addNotes(notes: List<Note>, onComplete: (List<Note>) -> Unit, onError: (Throwable) -> Unit) {
        taskQueue.fromCallable {
            notesDao.addNotes(notes.map(::entityFromNote))
            todoItemsDao.addTodoItems(
                    notes.filter { note -> note is Note.Todo }
                            .map { note -> note as Note.Todo }
                            .flatMap { todo -> todo.items }
                            .map(::entityFromItem)
            )
            notes
        }
                .onComplete(onComplete)
                .onError(onError)
                .go()
    }

    override fun updateNote(note: Note, onComplete: (Note) -> Unit, onError: (Throwable) -> Unit) {
        taskQueue.fromCallable {
            notesDao.updateNote(entityFromNote(note))
            if (note is Note.Todo) {
                todoItemsDao.updateTodoItems(note.items.map(::entityFromItem))
            }
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

private fun entityFromNote(note: Note) = when (note) {
    is Note.Text -> NoteEntity(note.id, note.title, note.body, todo = 0)
    is Note.Todo -> NoteEntity(note.id, note.title, body = "", todo = 1)
}

private fun itemFromEntity(entity: TodoItemEntity) = TodoItem(
        entity.id,
        entity.noteId,
        entity.text,
        entity.done != 0
)

private fun entityFromItem(item: TodoItem) = TodoItemEntity(
        item.id,
        item.noteId,
        item.text,
        if (item.done) 1 else 0
)