package com.github.rozag.redux.core

interface ReduxStore<S : ReduxState, A : ReduxAction> {

    fun getState(): S

    fun replaceReducer(reducer: (state: S, action: A) -> S)

    fun dispatch(action: A)

    fun applyMiddleware(vararg middlewareList: ReduxMiddleware<S, A, ReduxStore<S, A>>)

}