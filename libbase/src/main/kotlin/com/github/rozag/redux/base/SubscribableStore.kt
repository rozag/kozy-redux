package com.github.rozag.redux.base

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxState
import com.github.rozag.redux.core.ReduxStore

/**
 * An implementation of the [ReduxSubscribableStore] interface.
 *
 * Typical usage:
 * * Define your state class.
 * ```
 *         data class MyState(val number: Int) : ReduxState {
 *             companion object {
 *                 val INITIAL: MyState = MyState(0)
 *             }
 *         }
 * ```
 * * Define your action sealed class.
 * ```
 *         sealed class MyAction {
 *             class One : MyAction()
 *             class Two : MyAction()
 *             class Three : MyAction()
 *         }
 * ```
 * * Define your pure root reducer function.
 * ```
 *         fun rootReducer(state: MyState, action: MyAction): MyState = when (action) {
 *             is MyAction.One -> ...
 *             is MyAction.Two -> ...
 *             is MyAction.Three -> ...
 *         }
 * ```
 * * Create the store instance. **There should be only one store instance for an app!**
 * ```
 *         val store: ReduxSubscribableStore<MyState, MyAction> =
 *             SubscribableStore(MyState.INITIAL, ::rootReducer)
 * ```
 * * And now you can dispatch your actions to the store, subscribe on state updates,
 * apply your middleware, etc.
 *
 * @param S the type of your [ReduxState]
 * @param A the type of your root [ReduxAction]
 * @property currentState an initial [ReduxState] for your [SubscribableStore]
 * @property reducer a pure function `(state, action) -> state`
 * @constructor creates new [SubscribableStore]
 */
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

    /**
     * This method is called in order to dispatch an action. It is used to
     * simplify wrapping [dispatch] with [ReduxMiddleware].
     *
     * @param action dispatched [ReduxAction]
     */
    protected open fun internalDispatch(action: A) {
        currentState = reducer(currentState, action)
        notifySubscribers()
    }

    /**
     * Invokes [ReduxSubscribableStore.Subscriber.onNewState] with the current
     * [ReduxState] on each [ReduxSubscribableStore.Subscriber].
     */
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

    override fun subscribe(subscriber: ReduxSubscribableStore.Subscriber<S>): ReduxSubscribableStore.Subscription {
        // Add the subscriber to list
        subscriberList.add(subscriber)

        // Deliver the current state to the subscriber
        subscriber.onNewState(getState())

        // Return the connection
        return object : ReduxSubscribableStore.Subscription {
            override fun cancel() {
                subscriberList.remove(subscriber)
            }
        }
    }

}