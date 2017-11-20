package com.github.rozag.redux.notes.middleware

import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxStore
import com.github.rozag.redux.notes.Action
import com.github.rozag.redux.notes.State
import com.github.rozag.redux.notes.logger.Logger

class LoggingMiddleware(private val logger: Logger)
    : ReduxMiddleware<State, Action, ReduxStore<State, Action>>() {

    override fun doAfterDispatch(store: ReduxStore<State, Action>, action: Action) {
        logger.d("New state: ${store.getState()}")
    }

    override fun doBeforeDispatch(store: ReduxStore<State, Action>, action: Action) {
        logger.d("Dispatching action: $action")
    }

}