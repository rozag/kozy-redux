package com.github.rozag.redux.notes

interface ActionCreator {
    fun createAndDispatch(): Unit
}