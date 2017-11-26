package com.github.rozag.redux.notes.screen.edit

fun editReducer(state: EditState, action: EditAction): EditState = when (action) {
    is EditAction.TearDown -> EditState.EMPTY
}