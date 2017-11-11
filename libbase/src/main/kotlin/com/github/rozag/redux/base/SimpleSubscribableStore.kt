package com.github.rozag.redux.base

import com.github.rozag.redux.core.Action
import com.github.rozag.redux.core.Middleware
import com.github.rozag.redux.core.State

class SimpleSubscribableStore<out S : State, A : Action>(
        private var state: S,
        private val reducer: (state: S, action: A) -> S
) : SubscribableStore<S, A> {

    private val subscriberList: MutableList<SubscribableStore.Subscriber<S>> = ArrayList()
    private val middlewareList: MutableList<Middleware<S, A>> = ArrayList()

    override fun getState(): S = state

    override fun dispatch(action: A) {
        // Apply each middleware
        middlewareList.forEach { middleware ->
            middleware.dispatch(state, action)
        }
        // TODO: apply middleware correctly

        // Apply the reducer graph
        state = reducer(state, action)

        // Notify subscribers
        subscriberList.forEach { subscriber ->
            subscriber.onNewState(state)
        }
    }

    override fun applyMiddleware(vararg middlewareArray: Middleware<S, A>) {
        middlewareList.addAll(middlewareArray)
    }

    override fun subscribe(subscriber: SubscribableStore.Subscriber<S>): SubscribableStore.Connection {
        subscriberList.add(subscriber)
        subscriber.onNewState(state)
        return object : SubscribableStore.Connection {
            override fun unsubscribe() {
                subscriberList.remove(subscriber)
            }
        }
    }

}