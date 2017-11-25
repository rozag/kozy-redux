package com.github.rozag.redux.base

import com.github.rozag.redux.base.ReduxSubscribableStore.Subscriber
import com.github.rozag.redux.base.ReduxSubscribableStore.Subscription
import com.github.rozag.redux.core.ReduxState

internal class SubscribableDelegate<S : ReduxState> {

    private val subscriberList: MutableList<Subscriber<S>> = ArrayList()

    internal fun notifySubscribers(currentState: S) {
        subscriberList.forEach { subscriber ->
            subscriber.onNewState(currentState)
        }
    }

    internal fun subscribe(currentState: S, subscriber: Subscriber<S>): Subscription {
        // Add the subscriber to list
        subscriberList.add(subscriber)

        // Deliver the current state to the subscriber
        subscriber.onNewState(currentState)

        // Return the connection
        return object : Subscription {
            override fun cancel() {
                subscriberList.remove(subscriber)
            }
        }
    }

}