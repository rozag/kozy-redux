package com.github.rozag.redux.notes.screen.list.holder

import android.view.View
import android.widget.TextView
import com.github.rozag.redux.notes.R
import com.github.rozag.redux.notes.model.Note
import com.github.rozag.redux.notes.resources.ResProvider

class TextViewHolder(
        itemView: View,
        onItemClickListener: (Int) -> Unit,
        private val resProvider: ResProvider
) : NoteViewHolder(itemView, onItemClickListener) {

    private val titleTextView: TextView = itemView.findViewById(R.id.note_title_tv)
    private val bodyTextView: TextView = itemView.findViewById(R.id.note_body_tv)

    fun bindText(note: Note) {
        titleTextView.text = if (note.title.isEmpty()) {
            resProvider.getString(R.string.empty_title)
        } else {
            note.title
        }
        bodyTextView.text = when (note) {
            is Note.Text -> if (note.body.isEmpty()) {
                resProvider.getString(R.string.empty_body)
            } else {
                note.body
            }
            is Note.Todo -> note.itemsAsBody
        }
    }
}