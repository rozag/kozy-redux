package com.github.rozag.redux.base

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxState

open class BufferedSubscribableStore<S : ReduxState, A : ReduxAction>(
        private val bufferSize: Int,
        private var currentState: S,
        private var reducer: (state: S, action: A) -> S
) : SubscribableStore<S, A>(currentState, reducer), ReduxBufferedSubscribableStore<S, A> {

    override fun bufferSize(): Int {
        TODO()
    }

    override fun currentBufferPosition(): Int {
        TODO()
    }

    override fun jumpToState(position: Int) {
        TODO()
    }

    override fun jumpToLatest() {
        TODO()
    }

    companion object {
        const val UNLIMITED: Int = -1
    }

}