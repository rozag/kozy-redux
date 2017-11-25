package com.github.rozag.redux.core.store

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxState

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