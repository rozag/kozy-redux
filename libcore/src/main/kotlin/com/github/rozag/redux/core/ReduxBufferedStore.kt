package com.github.rozag.redux.core

interface ReduxBufferedStore<S : ReduxState, A : ReduxAction> : ReduxStore<S, A> {

    fun bufferSize(): Int

    fun currentBufferPosition(): Int

    fun jumpToState(position: Int)

    fun jumpToLatest()

}