package com.github.rozag.redux.counter.buffered

import android.app.Application
import android.util.Log
import com.github.rozag.redux.base.BufferedSubscribableStore
import com.github.rozag.redux.base.ReduxBufferedSubscribableStore

typealias CounterStore = ReduxBufferedSubscribableStore<CounterState, CounterAction>

class App : Application() {

    companion object {
        val store: CounterStore = BufferedSubscribableStore(
                CounterState.INITIAL,
                ::rootReducer,
                20
        )
    }

    override fun onCreate() {
        super.onCreate()
        store.applyMiddleware(
                LoggingMiddleware(Log.DEBUG, "DebugLogger"),
                TearDownMiddleware(store)
        )
    }

}