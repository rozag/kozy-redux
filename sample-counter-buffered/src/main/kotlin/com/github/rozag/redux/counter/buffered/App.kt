package com.github.rozag.redux.counter.buffered

import android.app.Application
import android.util.Log
import com.github.rozag.redux.base.BufferedSubscribableStore
import com.github.rozag.redux.base.ReduxBufferedSubscribableStore

class App : Application() {

    companion object {
        val store: ReduxBufferedSubscribableStore<CounterState, CounterAction> = BufferedSubscribableStore(
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