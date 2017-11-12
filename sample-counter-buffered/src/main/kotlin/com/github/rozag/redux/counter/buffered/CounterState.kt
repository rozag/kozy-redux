package com.github.rozag.redux.counter.buffered

import com.github.rozag.redux.core.ReduxState

data class CounterState(val count: Int) : ReduxState {

    companion object {
        val INITIAL = CounterState(0)
    }

}