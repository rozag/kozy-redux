package com.github.rozag.redux.notes.middleware

import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxStore
import com.github.rozag.redux.notes.Action
import com.github.rozag.redux.notes.State
import timber.log.Timber

class LoggingMiddleware()
    : ReduxMiddleware<State, Action, ReduxStore<State, Action>>() {

    override fun doAfterDispatch(store: ReduxStore<State, Action>, action: Action) {
        Timber.d("New state: ${store.getState()}")
    }

    override fun doBeforeDispatch(store: ReduxStore<State, Action>, action: Action) {
        Timber.d("Dispatching action: $action")
    }

}