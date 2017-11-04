package com.github.rozag.redux

interface Store<S : State, A : Action> {

    fun getState(): S

    fun dispatch(action: A)

    fun applyMiddleware(middleware: Middleware<S, A>): Store<S, A>

    fun applyMiddleware(vararg middlewareList: Middleware<S, A>): Store<S, A>

    fun applyMiddleware(middlewareList: List<Middleware<S, A>>): Store<S, A>

    fun subscribe(subscriber: Subscriber<S>): Connection

    interface Subscriber<in S> {
        fun onNewState(state: S)
    }

    interface Connection {
        fun unsubscribe()
    }

}