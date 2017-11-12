package com.github.rozag.redux.base

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxState
import com.github.rozag.redux.core.ReduxStore

open class SubscribableStore<S : ReduxState, A : ReduxAction>(
        private var currentState: S,
        private var reducer: (state: S, action: A) -> S
) : ReduxSubscribableStore<S, A> {

    private val subscriberList: MutableList<ReduxSubscribableStore.Subscriber<S>> = ArrayList()
    private var dispatchFun: (A) -> Unit = { action: A ->
        // Apply the reducer graph
        currentState = reducer(currentState, action)

        // Notify subscribers
        subscriberList.forEach { subscriber ->
            subscriber.onNewState(currentState)
        }
    }

    override fun getState(): S = currentState

    override fun replaceReducer(reducer: (state: S, action: A) -> S) {
        this.reducer = reducer
    }

    override fun dispatch(action: A) {
        dispatchFun(action)
    }

    override fun applyMiddleware(vararg middlewareList: ReduxMiddleware<S, A, ReduxStore<S, A>>) {
        middlewareList.forEach { middleware ->
            dispatchFun = middleware.apply(this)(dispatchFun)
        }
    }

    override fun subscribe(subscriber: ReduxSubscribableStore.Subscriber<S>): ReduxSubscribableStore.Connection {
        // Add the subscriber to list
        subscriberList.add(subscriber)

        // Deliver the current state to the subscriber
        subscriber.onNewState(currentState)

        // Return the connection
        return object : ReduxSubscribableStore.Connection {
            override fun unsubscribe() {
                subscriberList.remove(subscriber)
            }
        }
    }

}