package com.github.rozag.redux.base

import com.github.rozag.redux.core.Action
import com.github.rozag.redux.core.State
import com.github.rozag.redux.core.Store

interface SubscribableStore<out S : State, A : Action> : Store<S, A> {

    fun subscribe(subscriber: Subscriber<S>): Connection

    interface Subscriber<in S> {
        fun onNewState(state: S)
    }

    interface Connection {
        fun unsubscribe()
    }

}