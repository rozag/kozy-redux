package com.github.rozag.redux.counter.buffered

import com.github.rozag.redux.core.ReduxState

data class CounterState(
        val count: Int,
        val currentStateIndex: Int,
        val totalStateCount: Int,
        val bufferSizeLimit: Int
) : ReduxState {

    companion object {
        fun initial(bufferSizeLimit: Int) = CounterState(
                0,
                0,
                1,
                bufferSizeLimit
        )
    }

}