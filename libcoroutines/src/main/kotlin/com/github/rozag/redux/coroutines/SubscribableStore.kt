package com.github.rozag.redux.coroutines

import com.github.rozag.redux.core.Action
import com.github.rozag.redux.core.State
import com.github.rozag.redux.core.Store

interface SubscribableStore<S : State, A : Action> : Store<S, A> {

    fun subscribe(subscriber: Subscriber<S>): Connection

    interface Subscriber<in S> {
        fun onNewState(state: S)
    }

    interface Connection {
        fun unsubscribe()
    }

}