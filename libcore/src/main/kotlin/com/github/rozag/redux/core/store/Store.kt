package com.github.rozag.redux.core.store

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxState

/**
 * An implementation of the [ReduxStore] interface.
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
 *         val store: ReduxStore<MyState, MyAction> = Store(MyState.INITIAL, ::rootReducer)
 * ```
 * * And now you can dispatch your actions to the store, apply your middleware, etc.
 *
 * @param S the type of your [ReduxState]
 * @param A the type of your root [ReduxAction]
 * @property currentState an initial [ReduxState] for your [ReduxStore]
 * @property reducer a pure function `(state, action) -> state`
 * @constructor creates new [Store]
 */
open class Store<S : ReduxState, A : ReduxAction>(
        protected open var currentState: S,
        open var reducer: (state: S, action: A) -> S
) : ReduxStore<S, A> {

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
    }

    override fun applyMiddleware(vararg middlewareList: ReduxMiddleware<S, A, ReduxStore<S, A>>) {
        middlewareList.forEach { middleware ->
            dispatchFun = middleware.apply(this)(dispatchFun)
        }
    }

}