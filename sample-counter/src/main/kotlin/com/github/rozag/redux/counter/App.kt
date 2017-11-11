package com.github.rozag.redux.counter

import android.app.Application
import android.util.Log
import com.github.rozag.redux.base.SimpleSubscribableStore
import com.github.rozag.redux.base.SubscribableStore

class App : Application() {

    companion object {
        val store: SubscribableStore<CounterState, CounterAction> = SimpleSubscribableStore(
                CounterState.Initial,
                ::rootReducer
        )
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