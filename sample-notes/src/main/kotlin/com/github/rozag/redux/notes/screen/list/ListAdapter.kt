package com.github.rozag.redux.notes.screen.list

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.rozag.redux.notes.R
import com.github.rozag.redux.notes.model.Note
import com.github.rozag.redux.notes.screen.list.holder.NoteViewHolder
import com.github.rozag.redux.notes.screen.list.holder.TextViewHolder

class ListAdapter(private val onNoteClickListener: (Note) -> Unit) : RecyclerView.Adapter<NoteViewHolder>() {

    private var notes: List<Note> = emptyList()

    private val onItemClickListener: (Int) -> Unit = { adapterPosition ->
        onNoteClickListener(notes[adapterPosition])
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        (holder as TextViewHolder).bindText(notes[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_note_item, parent, false)
        return TextViewHolder(itemView, onItemClickListener)
    }

    override fun getItemCount(): Int = notes.size

    fun updateNotes(notes: List<Note>) {
        val oldNotes = this.notes
        this.notes = notes
        val diffResult = DiffUtil.calculateDiff(ListDiffUtilCallback(oldNotes, notes));
        diffResult.dispatchUpdatesTo(this);
    }

}