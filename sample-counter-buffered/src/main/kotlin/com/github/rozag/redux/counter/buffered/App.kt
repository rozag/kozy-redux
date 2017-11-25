package com.github.rozag.redux.counter.buffered

import android.app.Application
import android.util.Log
import com.github.rozag.redux.base.ReduxSubscribableBufferedStore
import com.github.rozag.redux.base.SubscribableBufferedStore

typealias CounterStore = ReduxSubscribableBufferedStore<CounterState, CounterAction>

class App : Application() {

    companion object {
        val store: CounterStore = SubscribableBufferedStore(
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