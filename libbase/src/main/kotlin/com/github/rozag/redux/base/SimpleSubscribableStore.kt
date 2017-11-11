package com.github.rozag.redux.base

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxState
import com.github.rozag.redux.core.ReduxStore

class SimpleSubscribableStore<S : ReduxState, A : ReduxAction>(
        private var state: S,
        private val reducer: (state: S, action: A) -> S
) : SubscribableStore<S, A> {

    private val subscriberList: MutableList<SubscribableStore.Subscriber<S>> = ArrayList()
    private var dispatchFun: (A) -> Unit = { action: A ->
        // Apply the reducer graph
        state = reducer(state, action)

        // Notify subscribers
        subscriberList.forEach { subscriber ->
            subscriber.onNewState(state)
        }
    }

    override fun getState(): S = state

    override fun dispatch(action: A) {
        dispatchFun(action)
    }

    override fun applyMiddleware(vararg middlewareList: ReduxMiddleware<S, A, ReduxStore<S, A>>) {
        middlewareList.forEach { middleware ->
            dispatchFun = middleware.apply(this)(dispatchFun)
        }
    }

    override fun subscribe(subscriber: SubscribableStore.Subscriber<S>): SubscribableStore.Connection {
        // Add the subscriber to list
        subscriberList.add(subscriber)

        // Deliver the current state to the subscriber
        subscriber.onNewState(state)

        // Return the connection
        return object : SubscribableStore.Connection {
            override fun unsubscribe() {
                subscriberList.remove(subscriber)
            }
        }
    }

}