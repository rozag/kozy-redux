package com.github.rozag.redux.base

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxState
import com.github.rozag.redux.core.ReduxStore

open class SubscribableStore<S : ReduxState, A : ReduxAction>(
        private var currentState: S,
        open var reducer: (state: S, action: A) -> S
) : ReduxSubscribableStore<S, A> {

    private val subscriberList: MutableList<ReduxSubscribableStore.Subscriber<S>> = ArrayList()
    private var dispatchFun: (A) -> Unit = { action: A -> internalDispatch(action) }

    override fun getState(): S = currentState

    override fun replaceReducer(reducer: (state: S, action: A) -> S) {
        this.reducer = reducer
    }

    override fun dispatch(action: A) = dispatchFun(action)

    protected open fun internalDispatch(action: A) {
        currentState = reducer(currentState, action)
        notifySubscribers()
    }

    protected fun notifySubscribers() {
        subscriberList.forEach { subscriber ->
            subscriber.onNewState(getState())
        }
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
        subscriber.onNewState(getState())

        // Return the connection
        return object : ReduxSubscribableStore.Connection {
            override fun unsubscribe() {
                subscriberList.remove(subscriber)
            }
        }
    }

}