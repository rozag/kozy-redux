package com.github.rozag.redux.notes

import com.github.rozag.redux.base.ReduxSubscribableStore

abstract class ReduxActivity : BaseActivity(), ReduxSubscribableStore.Subscriber<AppState> {

    protected val store: Store = NotesApplication.store

    private lateinit var subscription: ReduxSubscribableStore.Subscription

    override fun onStart() {
        super.onStart()
        subscription = store.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        subscription.cancel()
    }

}