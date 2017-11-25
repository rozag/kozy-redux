package com.github.rozag.redux.base

import com.github.rozag.redux.base.ReduxSubscribableStore.Subscriber
import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxState
import com.github.rozag.redux.core.store.ReduxStore

/**
 * An extension for the [ReduxStore] interface. Defines [subscribe] method
 * which allows you to subscribe [Subscriber] on [ReduxState] updates.
 *
 * @param S the type of your [ReduxState]
 * @param A the type of your root [ReduxAction]
 */
interface ReduxSubscribableStore<S : ReduxState, A : ReduxAction> : ReduxStore<S, A> {

    /**
     * Subscribes the subscriber on [ReduxState] updates.
     *
     * @param subscriber a [ReduxSubscribableStore] state updates listener
     * @return [Subscription] object
     */
    fun subscribe(subscriber: Subscriber<S>): Subscription

    /**
     * Listener for the [ReduxSubscribableStore] state updates.
     */
    interface Subscriber<in S> {
        /**
         * Invoked when the new [ReduxState] appears in the [ReduxSubscribableStore]
         *
         * @param state new [ReduxState]
         */
        fun onNewState(state: S)
    }

    /**
     * Connection between the [Subscriber] and the [ReduxSubscribableStore].
     */
    interface Subscription {
        /**
         * Unsubscribes ths [Subscriber] from the [ReduxSubscribableStore].
         */
        fun cancel()
    }

}