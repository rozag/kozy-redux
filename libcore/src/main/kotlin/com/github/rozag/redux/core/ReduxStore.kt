package com.github.rozag.redux.core

interface ReduxStore<out S : ReduxState, A : ReduxAction> {

    fun getState(): S

    fun dispatch(action: A)

    fun applyMiddleware(vararg middlewareArray: ReduxMiddleware<S, A>)

}