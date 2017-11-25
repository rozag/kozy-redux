package com.github.rozag.redux.notes.middleware

import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.store.ReduxStore
import com.github.rozag.redux.notes.Action
import com.github.rozag.redux.notes.AppState
import timber.log.Timber

class LoggingMiddleware(private val logLevel: Int)
    : ReduxMiddleware<AppState, Action, ReduxStore<AppState, Action>>() {

    override fun doAfterDispatch(store: ReduxStore<AppState, Action>, action: Action) {
        Timber.log(logLevel, "New state: ${store.getState()}")
    }

    override fun doBeforeDispatch(store: ReduxStore<AppState, Action>, action: Action) {
        Timber.log(logLevel, "Dispatching action: $action")
    }

}