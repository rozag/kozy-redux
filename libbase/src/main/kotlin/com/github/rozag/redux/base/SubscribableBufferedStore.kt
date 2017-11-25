package com.github.rozag.redux.base

import com.github.rozag.redux.base.ReduxSubscribableStore.Subscriber
import com.github.rozag.redux.base.ReduxSubscribableStore.Subscription
import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxState
import com.github.rozag.redux.core.store.BufferedStore
import com.github.rozag.redux.core.store.ReduxBufferedStore

/**
 * An implementation of [ReduxBufferedStore] and [ReduxSubscribableStore] interfaces.
 * It can do all the things that [SubscribableStore] can, but this class also keeps
 * a buffer of a limited number of previous states (or all of them) and allows you
 * to manipulate this buffer via the [ReduxBufferedStore]'s methods.
 *
 * The usage of this store is the same as the [SubscribableStore]'s, but now you can
 * also manipulate the state history.
 *
 * @param S the type of your [ReduxState]
 * @param A the type of your root [ReduxAction]
 * @property bufferSizeLimit the limit for the state buffer size
 * @constructor creates new [SubscribableBufferedStore]
 */
open class SubscribableBufferedStore<S : ReduxState, A : ReduxAction>(
        initialState: S,
        override var reducer: (state: S, action: A) -> S,
        override var bufferSizeLimit: Int = UNLIMITED,
        initialBufferSize: Int = 0
) : BufferedStore<S, A>(initialState, reducer, bufferSizeLimit, initialBufferSize), ReduxSubscribableBufferedStore<S, A> {

    private val delegate = SubscribableDelegate<S>()

    override fun internalDispatch(action: A) {
        super.internalDispatch(action)
        delegate.notifySubscribers(getState())
    }

    /**
     * Resets the state buffer, populates it with the new initial [ReduxState] and notifies subscribers.
     *
     * @param initialState new initial [ReduxState] for the state buffer
     */
    override fun resetBuffer(initialState: S) {
        super.resetBuffer(initialState)
        delegate.notifySubscribers(getState())
    }

    /**
     * Moves the current state buffer index to the new position and notifies subscribers.
     *
     * @param position position in a state buffer to which the store should jump
     */
    override fun jumpToState(position: Int) {
        super.jumpToState(position)
        delegate.notifySubscribers(getState())
    }

    override fun subscribe(subscriber: Subscriber<S>): Subscription = delegate.subscribe(getState(), subscriber)

}