package com.github.rozag.redux.counter

import android.app.Application
import com.github.rozag.redux.coroutines.SimpleSubscribableStore
import com.github.rozag.redux.coroutines.SubscribableStore

class App : Application() {

    companion object {
        val store: SubscribableStore<CounterState, CounterAction> = SimpleSubscribableStore(
                CounterState.initialState,
                ::rootReducer
        )
    }

    override fun onCreate() {
        super.onCreate()
        store.dispatch(CounterAction.Init())
    }

}