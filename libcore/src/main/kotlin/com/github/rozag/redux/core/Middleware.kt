package com.github.rozag.redux.core

interface Middleware<S : State, in A : Action> {
    fun dispatch(state: S, action: A): S
}