package com.github.rozag.redux.notes.screen.list

import android.content.res.Resources
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class StatusBarMarginItemDecoration(private val resources: Resources) : RecyclerView.ItemDecoration() {

    private val topMargin: Int

    init {
        val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
        topMargin = if (resId > 0) resources.getDimensionPixelSize(resId) else 0
    }

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        outRect.top = if (parent.getChildAdapterPosition(view) == 0) topMargin else 0
    }

}