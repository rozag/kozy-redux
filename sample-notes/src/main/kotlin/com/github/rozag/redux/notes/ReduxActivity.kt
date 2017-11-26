package com.github.rozag.redux.notes

import com.github.rozag.redux.base.ReduxSubscribableStore
import com.github.rozag.redux.notes.router.Router

abstract class ReduxActivity : BaseActivity(), ReduxSubscribableStore.Subscriber<AppState> {

    private val router: Router = NotesApplication.router

    protected val store: NotesStore = NotesApplication.store

    private lateinit var subscription: ReduxSubscribableStore.Subscription

    override fun onStart() {
        super.onStart()
        subscription = store.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        subscription.cancel()
    }

    override fun onNewState(state: AppState) {
        router.showScreenIfNeeded(this, state.routerState.screenToOpen)
    }

}