package com.github.rozag.redux.notes.screen.list

import android.content.res.Resources
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.github.rozag.redux.notes.statusBarHeight

class StatusBarMarginItemDecoration(resources: Resources) : RecyclerView.ItemDecoration() {

    private val topMargin = resources.statusBarHeight()

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        outRect.top = if (parent.getChildAdapterPosition(view) == 0) topMargin else 0
    }

}