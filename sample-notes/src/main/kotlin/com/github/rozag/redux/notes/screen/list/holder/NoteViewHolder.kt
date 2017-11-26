package com.github.rozag.redux.notes.screen.list.holder

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class NoteViewHolder(
        itemView: View,
        onItemClickListener: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener { onItemClickListener(adapterPosition) }
    }

}