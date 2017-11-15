package com.github.rozag.redux.core

/**
 * Sometimes you want to keep a story of all your [ReduxState] and to jump back in time
 * to them (UNDO-buttons, for instance). [ReduxBufferedStore] defines such interface
 * extension for [ReduxStore]. Collection which contains all [ReduxState] is called state buffer.
 *
 * Moreover, you can limit the size of the state buffer (e.g. store only 20 latest [ReduxState]).
 *
 * @param S the type of your [ReduxState]
 * @param A the type of your root [ReduxAction]
 */
interface ReduxBufferedStore<S : ReduxState, A : ReduxAction> : ReduxStore<S, A> {

    /**
     * Returns the limit for the state buffer size.
     *
     * @return current state buffer size limit
     */
    fun bufferSizeLimit(): Int

    /**
     * Changes the state buffer size limit.
     *
     * @param newSizeLimit new state buffer size limit for the store
     */
    fun changeSizeLimit(newSizeLimit: Int)

    /**
     * Returns current size of the state buffer.
     *
     * @return current state buffer size
     */
    fun currentBufferSize(): Int

    /**
     * Returns the index of the current [ReduxState] in the state buffer.
     *
     * @return current position in the state buffer
     */
    fun currentBufferPosition(): Int

    /**
     * Resets the state buffer and populates it with the new initial [ReduxState].
     *
     * @param initialState new initial [ReduxState] for the state buffer
     */
    fun resetBuffer(initialState: S)

    /**
     * Moves the current state buffer index to the new position.
     *
     * @param position position in a state buffer to which the store should jump
     */
    fun jumpToState(position: Int)

    /**
     * Moves the current state buffer index to the new position and removes all
     * following [ReduxState] from the state buffer.
     *
     * @param position position in a state buffer to which the store should be reset
     */
    fun resetToState(position: Int)

}