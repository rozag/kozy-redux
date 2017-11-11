package com.github.rozag.redux.core

interface ReduxMiddleware<in S : ReduxState, in A : ReduxAction> {
    fun dispatch(state: S, action: A)
}