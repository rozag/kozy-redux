package com.github.rozag.redux.counter

import com.github.rozag.redux.core.ReduxState

data class CounterState(val count: Int) : ReduxState {
    companion object {
        val INITIAL: CounterState = CounterState(0)
    }
}