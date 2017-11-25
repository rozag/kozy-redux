package com.github.rozag.redux.base

import com.github.rozag.redux.base.ReduxSubscribableStore.Subscriber
import com.github.rozag.redux.base.ReduxSubscribableStore.Subscription
import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxState
import com.github.rozag.redux.core.store.Store

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
        override var currentState: S,
        override var reducer: (state: S, action: A) -> S
) : Store<S, A>(currentState, reducer), ReduxSubscribableStore<S, A> {

    private val delegate = SubscribableDelegate<S>()

    /**
     * This method is called in order to dispatch an action. It is used to
     * simplify wrapping [dispatch] with [ReduxMiddleware].
     *
     * @param action dispatched [ReduxAction]
     */
    override fun internalDispatch(action: A) {
        super.internalDispatch(action)
        delegate.notifySubscribers(getState())
    }

    override fun subscribe(subscriber: Subscriber<S>): Subscription = delegate.subscribe(getState(), subscriber)

}