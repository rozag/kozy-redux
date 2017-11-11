package com.github.rozag.redux.counter

import android.util.Log
import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxStore

class LoggingMiddleware(
        private val priority: Int = Log.DEBUG,
        private val tag: String = "LoggingMiddleware"
) : ReduxMiddleware<CounterState, CounterAction, ReduxStore<CounterState, CounterAction>>() {

    override fun doBeforeDispatch(store: ReduxStore<CounterState, CounterAction>, action: CounterAction) {
        Log.println(priority, tag, "Dispatching action: $action")
    }

    override fun doAfterDispatch(store: ReduxStore<CounterState, CounterAction>, action: CounterAction) {
        Log.println(priority, tag, "New state: ${store.getState()}")
    }

}