package com.github.rozag.redux.base

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxState
import java.util.*

open class BufferedSubscribableStore<S : ReduxState, A : ReduxAction>(
        private var bufferSizeLimit: Int,
        currentState: S,
        reducer: (state: S, action: A) -> S,
        initialBufferSize: Int = 0
) : SubscribableStore<S, A>(currentState, reducer), ReduxBufferedSubscribableStore<S, A> {

    private var currentBufferPosition: Int = 0
    private val stateBuffer: MutableList<S> = ArrayList(when {
        initialBufferSize > 0 -> initialBufferSize
        bufferSizeLimit > 0 -> bufferSizeLimit
        else -> 1
    })

    init {
        stateBuffer.add(currentState)
    }

    override fun getState(): S = stateBuffer[currentBufferPosition]

    override fun dispatch(action: A) {
        super.dispatch(action)
        val previousBufferSize = stateBuffer.size
        stateBuffer.add(super.getState())
        if (previousBufferSize == bufferSizeLimit) {
            stateBuffer.removeAt(0)
        }
        currentBufferPosition = stateBuffer.size - 1
    }

    override fun bufferSizeLimit(): Int = bufferSizeLimit

    override fun changeSizeLimit(newSizeLimit: Int) {
        if (newSizeLimit in 1..(bufferSizeLimit - 1)) {
            val currentBufferSize = stateBuffer.size
            if (currentBufferSize > newSizeLimit) {
                for (i in currentBufferSize - newSizeLimit - 1 downTo 0) {
                    stateBuffer.removeAt(i)
                }
            }
        }
        bufferSizeLimit = newSizeLimit
    }

    override fun currentBufferSize(): Int = stateBuffer.size

    override fun currentBufferPosition(): Int = currentBufferPosition

    override fun jumpToState(position: Int) {
        currentBufferPosition = when {
            position < 0 -> throw IllegalArgumentException("Position $position is negative")
            position > stateBuffer.lastIndex -> throw IllegalArgumentException(
                    "Position $position is larger than the last buffer's index (${stateBuffer.lastIndex})"
            )
            else -> position
        }
    }

    override fun jumpToFirstState() {
        jumpToState(0)
    }

    override fun jumpToLatestState() {
        jumpToState(stateBuffer.size - 1)
    }

    companion object {
        const val UNLIMITED: Int = 0
    }

}