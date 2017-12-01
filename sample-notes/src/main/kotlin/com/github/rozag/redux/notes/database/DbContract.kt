package com.github.rozag.redux.notes.database

object DbContract {
    const val name = "notes.sqlite"
    const val version = 1

    object Table {
        object Notes {
            const val name = "notes"

            object Column {
                const val id = "id"
                const val title = "title"
                const val body = "body"
                const val isTodo = "is_todo"
            }
        }

        object TodoItems {
            const val name = "todo_items"

            object Column {
                const val id = "id"
                const val noteId = "note_id"
                const val text = "text"
                const val done = "done"
            }
        }
    }
}