package com.github.rozag.redux.core

abstract class ReduxMiddleware<S : ReduxState, A : ReduxAction, in R : ReduxStore<S, A>> {

    abstract fun doBeforeDispatch(store: R, action: A)
    abstract fun doAfterDispatch(store: R, action: A)

    fun apply(store: R): ((A) -> Unit) -> (A) -> Unit {
        return { itDispatch: (A) -> Unit ->
            { itAction: A ->
                doBeforeDispatch(store, itAction)
                itDispatch(itAction)
                doAfterDispatch(store, itAction)
            }
        }
    }

}