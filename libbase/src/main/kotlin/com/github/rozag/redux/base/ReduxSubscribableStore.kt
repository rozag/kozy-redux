package com.github.rozag.redux.base

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxState
import com.github.rozag.redux.core.ReduxStore

interface ReduxSubscribableStore<S : ReduxState, A : ReduxAction> : ReduxStore<S, A> {

    fun subscribe(subscriber: Subscriber<S>): Connection

    interface Subscriber<in S> {
        fun onNewState(state: S)
    }

    interface Connection {
        fun unsubscribe()
    }

}