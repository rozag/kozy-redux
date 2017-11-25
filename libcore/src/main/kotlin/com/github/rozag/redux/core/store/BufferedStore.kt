package com.github.rozag.redux.core.store

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxState

open class BufferedStore<S : ReduxState, A : ReduxAction>(
        initialState: S,
        override var reducer: (state: S, action: A) -> S,
        protected open var bufferSizeLimit: Int = UNLIMITED,
        initialBufferSize: Int = 0
) : Store<S, A>(initialState, reducer), ReduxBufferedStore<S, A> {

    companion object {
        /**
         * Do not limit the state buffer size.
         */
        const val UNLIMITED: Int = 0
    }

    private var currentBufferPosition: Int = 0
    private val stateBuffer: MutableList<S> = ArrayList(when {
        initialBufferSize > 0 -> initialBufferSize
        bufferSizeLimit > 0 -> bufferSizeLimit
        else -> 1
    })

    init {
        stateBuffer.add(initialState)
    }

    override fun getState(): S = stateBuffer[currentBufferPosition]

    override fun internalDispatch(action: A) {
        // Apply the reducer graph
        val newState = reducer(stateBuffer.last(), action)

        // Handle the buffer
        val previousBufferSize = stateBuffer.size
        stateBuffer.add(newState)
        if (previousBufferSize == bufferSizeLimit) {
            stateBuffer.removeAt(0)
        }
        currentBufferPosition = stateBuffer.lastIndex
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

    /**
     * Resets the state buffer, populates it with the new initial [ReduxState] and notifies subscribers.
     *
     * @param initialState new initial [ReduxState] for the state buffer
     */
    override fun resetBuffer(initialState: S) {
        stateBuffer.clear()
        stateBuffer.add(initialState)
        currentBufferPosition = 0
    }

    override fun buffer(): List<ReduxState> = ArrayList(stateBuffer)

    private fun rangeCheck(position: Int) {
        val bufferSize = stateBuffer.size
        if (position < 0 || position >= bufferSize) {
            throw IllegalArgumentException("Index: $position, Size: $bufferSize")
        }
    }

    /**
     * Moves the current state buffer index to the new position and notifies subscribers.
     *
     * @param position position in a state buffer to which the store should jump
     */
    override fun jumpToState(position: Int) {
        rangeCheck(position)
        currentBufferPosition = position
    }

    /**
     * Moves the current state buffer index to the new position and removes all
     * following [ReduxState] from the state buffer and notifies subscribers.
     *
     * @param position position in a state buffer to which the store should be reset
     */
    override fun resetToState(position: Int) {
        rangeCheck(position)
        for (i in stateBuffer.lastIndex downTo position + 1) {
            stateBuffer.removeAt(i)
        }
        jumpToState(position)
    }

}