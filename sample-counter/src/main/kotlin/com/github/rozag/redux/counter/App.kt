package com.github.rozag.redux.counter

import android.app.Application
import android.util.Log
import com.github.rozag.redux.base.SubscribableStore
import com.github.rozag.redux.base.ReduxSubscribableStore

class App : Application() {

    companion object {
        val store: ReduxSubscribableStore<CounterState, CounterAction> = SubscribableStore(
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