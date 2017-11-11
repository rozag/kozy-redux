package com.github.rozag.redux.counter

import android.app.Application
import com.github.rozag.redux.base.SimpleSubscribableStore
import com.github.rozag.redux.base.SubscribableStore

class App : Application() {

    // TODO: Add middleware

    companion object {
        val store: SubscribableStore<CounterState, CounterAction> = SimpleSubscribableStore(
                CounterState.Initial,
                ::rootReducer
        )
    }

    override fun onCreate() {
        super.onCreate()
        store.dispatch(CounterAction.SetUp())
    }

}