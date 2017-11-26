package com.github.rozag.redux.notes.screen.list.holder

import android.view.View
import android.widget.TextView
import com.github.rozag.redux.notes.R
import com.github.rozag.redux.notes.model.Note

class TextViewHolder(
        itemView: View,
        onItemClickListener: (Int) -> Unit
) : NoteViewHolder(itemView, onItemClickListener) {

    private val titleTextView: TextView = itemView.findViewById(R.id.note_title_tv)
    private val bodyTextView: TextView = itemView.findViewById(R.id.note_body_tv)

    fun bindText(note: Note) {
        titleTextView.text = note.title
        bodyTextView.text = note.body
    }

}