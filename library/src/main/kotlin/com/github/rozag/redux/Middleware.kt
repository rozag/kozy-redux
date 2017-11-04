package com.github.rozag.redux

interface Middleware<out S : State, in A : Action> {
    fun dispatch(action: A): S
}