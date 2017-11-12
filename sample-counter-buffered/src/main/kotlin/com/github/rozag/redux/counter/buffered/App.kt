package com.github.rozag.redux.counter.buffered

import android.app.Application
import android.util.Log
import com.github.rozag.redux.base.BufferedSubscribableStore
import com.github.rozag.redux.base.ReduxBufferedSubscribableStore

class App : Application() {

    companion object {
        const val BUFFER_SIZE_LIMIT: Int = 20

        val store: ReduxBufferedSubscribableStore<CounterState, CounterAction> = BufferedSubscribableStore(
                BUFFER_SIZE_LIMIT,
                CounterState.initial(BUFFER_SIZE_LIMIT),
                ::rootReducer
        )
    }

    override fun onCreate() {
        super.onCreate()
        store.applyMiddleware(LoggingMiddleware(Log.DEBUG, "DebugLogger"))
    }

}