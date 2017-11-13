package com.github.rozag.redux.base

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxState
import java.util.*

open class BufferedSubscribableStore<S : ReduxState, A : ReduxAction>(
        private var bufferSizeLimit: Int,
        initialState: S,
        override var reducer: (state: S, action: A) -> S,
        initialBufferSize: Int = 0
) : SubscribableStore<S, A>(initialState, reducer), ReduxBufferedSubscribableStore<S, A> {

    companion object {
        const val UNLIMITED: Int = 0
    }

    private var currentBufferPosition: Int = 0
    private val stateBuffer: MutableList<S> = ArrayList(when {
        initialBufferSize > 0 -> initialBufferSize
        bufferSizeLimit > 0 -> bufferSizeLimit
        else -> 1
    })

    override var dispatchFun: (A) -> Unit = { action: A ->
        // Apply the reducer graph
        val newState = reducer(stateBuffer.last(), action)

        // Handle the buffer
        val previousBufferSize = stateBuffer.size
        stateBuffer.add(newState)
        if (previousBufferSize == bufferSizeLimit) {
            stateBuffer.removeAt(0)
        }
        currentBufferPosition = stateBuffer.lastIndex

        // Notify subscribers
        subscriberList.forEach { subscriber ->
            subscriber.onNewState(newState)
        }
    }

    init {
        stateBuffer.add(initialState)
    }

    override fun getState(): S = stateBuffer[currentBufferPosition]

    override fun subscribe(subscriber: ReduxSubscribableStore.Subscriber<S>): ReduxSubscribableStore.Connection {
        // Add the subscriber to list
        subscriberList.add(subscriber)

        // Deliver the current state to the subscriber
        subscriber.onNewState(getState())

        // Return the connection
        return object : ReduxSubscribableStore.Connection {
            override fun unsubscribe() {
                subscriberList.remove(subscriber)
            }
        }
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

    override fun resetBuffer(initialState: S) {
        stateBuffer.clear()
        stateBuffer.add(initialState)
        currentBufferPosition = 0
    }

    override fun jumpToState(position: Int) {
        currentBufferPosition = when {
            position < 0 -> throw IllegalArgumentException("Position $position is negative")
            position > stateBuffer.lastIndex -> throw IllegalArgumentException(
                    "Position $position is larger than the last buffer's index (${stateBuffer.lastIndex})"
            )
            else -> position
        }

        // Notify subscribers
        val newState = getState()
        subscriberList.forEach { subscriber ->
            subscriber.onNewState(newState)
        }
    }

}