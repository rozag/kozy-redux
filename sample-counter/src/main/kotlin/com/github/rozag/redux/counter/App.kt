package com.github.rozag.redux.counter

import android.app.Application
import android.util.Log
import com.github.rozag.redux.base.ReduxSubscribableStore
import com.github.rozag.redux.base.SubscribableStore

typealias CounterStore = ReduxSubscribableStore<CounterState, CounterAction>

class App : Application() {

    companion object {
        val store: CounterStore = SubscribableStore(CounterState.INITIAL, ::rootReducer)
    }

    override fun onCreate() {
        super.onCreate()
        store.apply {
            applyMiddleware(
                    LoggingMiddleware(Log.DEBUG, "DebugLogger"),
                    LoggingMiddleware(Log.ERROR, "ErrorLogger")
            )
            dispatch(CounterAction.SetUp())
        }
    }

}