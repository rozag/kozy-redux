package com.github.rozag.redux.notes.screen.list

import android.support.v7.util.DiffUtil
import com.github.rozag.redux.notes.model.Note

class ListDiffUtilCallback(
        private val oldNotes: List<Note>,
        private val newNotes: List<Note>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldNotes.size

    override fun getNewListSize(): Int = newNotes.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
            = oldNotes[oldItemPosition].id == newNotes[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
            = oldNotes[oldItemPosition] == newNotes[newItemPosition]

}