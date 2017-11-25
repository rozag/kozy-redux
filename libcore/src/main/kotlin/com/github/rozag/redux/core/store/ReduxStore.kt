package com.github.rozag.redux.core.store

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxState

/**
 * [ReduxStore] is a core interface in a Redux paradigm. It holds your [ReduxState]
 * and applies the reducer tree to [ReduxAction] which you dispatch. It also allows
 * you to wrap the [ReduxStore.dispatch] method with your custom [ReduxMiddleware].
 *
 * @param S the type of your [ReduxState]
 * @param A the type of your root [ReduxAction]
 */
interface ReduxStore<S : ReduxState, A : ReduxAction> {

    /**
     * Returns current [ReduxState].
     *
     * @return current [ReduxState]
     */
    fun getState(): S

    /**
     * Replaces current reducer function with the new one. This method is useful for
     * testing or for switching reducers when the new module is loaded dynamically.
     *
     * @param reducer pure function `(state, action) -> state`
     */
    fun replaceReducer(reducer: (state: S, action: A) -> S)

    /**
     * Applies the reducer function to the dispatched [ReduxAction] and saves the new [ReduxState].
     *
     * @param action dispatched [ReduxAction]
     */
    fun dispatch(action: A)

    /**
     * Wraps the [dispatch] method with your [ReduxMiddleware]. The last [ReduxMiddleware] in
     * list will be invoked just before the [dispatch] and just after it:
     * ```
     * store.applyMiddleware(middlewareOne, middlewareTwo)
     * store.dispatch(action)
     * // middlewareOne.doBeforeDispatch(...) invoked
     * // middlewareTwo.doBeforeDispatch(...) invoked
     * // actual store.dispatch(...) invoked
     * // middlewareTwo.doAfterDispatch(...) invoked
     * // middlewareOne.doAfterDispatch(...) invoked
     * ```
     *
     * @param middlewareList vararg list of your [ReduxMiddleware]
     */
    fun applyMiddleware(vararg middlewareList: ReduxMiddleware<S, A, ReduxStore<S, A>>)

}