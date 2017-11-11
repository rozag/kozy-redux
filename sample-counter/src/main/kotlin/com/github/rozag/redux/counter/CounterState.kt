package com.github.rozag.redux.counter

import com.github.rozag.redux.core.State

data class CounterState(val count: Int) : State {
    companion object {
        val Initial: CounterState = CounterState(0)
    }
}