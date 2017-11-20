package com.github.rozag.redux.notes.middleware

import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxStore
import com.github.rozag.redux.notes.Action
import com.github.rozag.redux.notes.State

class LoggingMiddleware(val tag: String = "LoggingMiddleware") : ReduxMiddleware<State, Action, ReduxStore<State, Action>>() {

    override fun doAfterDispatch(store: ReduxStore<State, Action>, action: Action) {
        // TODO
    }

    override fun doBeforeDispatch(store: ReduxStore<State, Action>, action: Action) {
        // TODO
    }

}