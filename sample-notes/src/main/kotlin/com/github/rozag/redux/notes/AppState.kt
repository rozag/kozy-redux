package com.github.rozag.redux.notes

import com.github.rozag.redux.core.ReduxState
import com.github.rozag.redux.notes.router.RouterState
import com.github.rozag.redux.notes.screen.edit.EditState
import com.github.rozag.redux.notes.screen.list.ListState

data class AppState(
        val routerState: RouterState,
        val listState: ListState,
        val editState: EditState
) : ReduxState {

    companion object {
        val EMPTY: AppState = AppState(RouterState.EMPTY, ListState.EMPTY, EditState.EMPTY)
    }

    override fun toString(): String {
        val bracesIndentation = "        "
        val notesIndentation = bracesIndentation + "    "
        val notesStringBuilder = StringBuilder()
        listState.notes.forEachIndexed { index, note ->
            if (index == 0) {
                notesStringBuilder.append('[').append('\n')
            }
            notesStringBuilder.append(notesIndentation).append(note).append('\n')
            if (index == listState.notes.lastIndex) {
                notesStringBuilder.append(bracesIndentation).append(']')
            }
        }
        return """
AppState(
    RouterState(
        currentScreen=${routerState.currentScreen.name}
        screenToOpen=${routerState.screenToOpen.name}
    )
    ListState(
        isLoading=${listState.isLoading}
        isError=${listState.isError}
        notes=$notesStringBuilder
    )
    EditState(
        note=${editState.note}
    )
)""".trimIndent()
    }

}