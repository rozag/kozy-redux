package com.github.rozag.redux.counter.buffered

import android.util.Log
import com.github.rozag.redux.core.ReduxBufferedStore
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

class TearDownMiddleware(
        private val bufferedStore: ReduxBufferedStore<CounterState, CounterAction>
) : ReduxMiddleware<CounterState, CounterAction, ReduxStore<CounterState, CounterAction>>() {

    override fun doBeforeDispatch(store: ReduxStore<CounterState, CounterAction>, action: CounterAction) = Unit

    override fun doAfterDispatch(store: ReduxStore<CounterState, CounterAction>, action: CounterAction) {
        if (action is CounterAction.TearDown) {
            bufferedStore.resetBuffer(CounterState.INITIAL)
        }
    }

}