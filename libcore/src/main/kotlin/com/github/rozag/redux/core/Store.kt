package com.github.rozag.redux.core

interface Store<S : State, A : Action> {

    fun getState(): S

    fun dispatch(action: A)

    fun applyMiddleware(vararg middlewareArray: Middleware<S, A>)

}