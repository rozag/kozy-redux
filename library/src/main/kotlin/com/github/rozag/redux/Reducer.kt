package com.github.rozag.redux

interface Reducer<S : State, in A : Action> : (S, A) -> S {
    override fun invoke(store: S, action: A): S
}