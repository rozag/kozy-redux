package com.github.rozag.redux.base

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxBufferedStore
import com.github.rozag.redux.core.ReduxState

/**
 * An implementation of [ReduxBufferedSubscribableStore] interface. It can do all the
 * things that [SubscribableStore] can, but this class also keeps a buffer of a limited
 * number of previous states (or all of them) and allows you to manipulate this buffer
 * via the [ReduxBufferedStore]'s methods.
 *
 * The usage of this store is the same as the [SubscribableStore]'s, but now you can
 * also manipulate the state history.
 *
 * @param S the type of your [ReduxState]
 * @param A the type of your root [ReduxAction]
 * @property bufferSizeLimit the limit for the state buffer size
 * @constructor creates new [BufferedSubscribableStore]
 */
open class BufferedSubscribableStore<S : ReduxState, A : ReduxAction>(
        initialState: S,
        override var reducer: (state: S, action: A) -> S,
        private var bufferSizeLimit: Int = UNLIMITED,
        initialBufferSize: Int = 0
) : SubscribableStore<S, A>(initialState, reducer), ReduxBufferedSubscribableStore<S, A> {

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

        notifySubscribers()
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
        notifySubscribers()
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
        notifySubscribers()
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