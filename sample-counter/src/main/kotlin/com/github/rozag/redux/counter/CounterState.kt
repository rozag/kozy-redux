package com.github.rozag.redux.counter

import com.github.rozag.redux.core.State

data class CounterState(val count: Int) : State {
    companion object {
        val initialState: CounterState = CounterState(0)
    }
}