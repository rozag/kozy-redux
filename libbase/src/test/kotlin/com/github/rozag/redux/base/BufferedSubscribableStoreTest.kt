package com.github.rozag.redux.base

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.*

@Suppress("FunctionName")
class BufferedSubscribableStoreTest : SubscribableStoreTest() {

    private lateinit var bufferedStore: ReduxBufferedSubscribableStore<TestState, TestAction>

    private val initialBufferSizeLimit = 1

    @Before
    override fun setUp() {
        initialState = TestState(-2)
        initialAction = TestAction(-1)
        newState = TestState(-1)
        reducer = { _, _ -> newState }
        bufferedStore = BufferedSubscribableStore(initialState, reducer, initialBufferSizeLimit)
        store = bufferedStore
    }

    @Test
    fun storeCreated_initialBufferSizeIsOne() {
        assertEquals(1, bufferedStore.currentBufferSize())
    }

    @Test
    fun bufferSizeLimitIncreased_limitActuallyIncreased() {
        val newSizeLimit = initialBufferSizeLimit + 1
        bufferedStore.changeSizeLimit(newSizeLimit)
        assertEquals(newSizeLimit, bufferedStore.bufferSizeLimit())
    }

    @Test
    fun bufferSizeLimitDecreased_limitActuallyDecreased() {
        val initialBufferSizeLimit = 2
        bufferedStore.changeSizeLimit(initialBufferSizeLimit)
        val newSizeLimit = initialBufferSizeLimit - 1
        bufferedStore.changeSizeLimit(newSizeLimit)
        assertEquals(newSizeLimit, bufferedStore.bufferSizeLimit())
    }

    @Test
    fun bufferSizeLimitIncreased_bufferSizeNotChanged() {
        val newSizeLimit = initialBufferSizeLimit + 1
        bufferedStore.changeSizeLimit(newSizeLimit)
        assertEquals(initialBufferSizeLimit, bufferedStore.currentBufferSize())
    }

    @Test
    fun actionDispatchedWhileBufferIsFull_bufferSizeNotChanged() {
        bufferedStore.dispatch(initialAction)
        assertEquals(initialBufferSizeLimit, bufferedStore.currentBufferSize())
    }

    @Test
    fun actionDispatchedWhileBufferIsNotFull_bufferSizeIncreasedByOne() {
        val newBufferSizeLimit = 2
        bufferedStore.changeSizeLimit(newBufferSizeLimit)
        bufferedStore.dispatch(initialAction)
        assertEquals(newBufferSizeLimit, bufferedStore.currentBufferSize())
    }

    @Test
    fun bufferSizeLimitDecreasedWhileBufferIsFull_bufferSizeDecreased() {
        val initialBufferSizeLimit = 2
        bufferedStore.changeSizeLimit(initialBufferSizeLimit)
        bufferedStore.dispatch(initialAction) // Now the buffer size equals 2
        val newBufferSizeLimit = 1
        bufferedStore.changeSizeLimit(newBufferSizeLimit)
        assertEquals(newBufferSizeLimit, bufferedStore.currentBufferSize())
    }

    @Test
    fun bufferSizeLimitSetToUnlimited_bufferSizeNotChanged() {
        val initialBufferSizeLimit = 2
        bufferedStore.changeSizeLimit(initialBufferSizeLimit)
        bufferedStore.dispatch(initialAction) // Now the buffer size equals 2
        val newBufferSizeLimit = BufferedSubscribableStore.UNLIMITED
        bufferedStore.changeSizeLimit(newBufferSizeLimit)
        assertEquals(initialBufferSizeLimit, bufferedStore.currentBufferSize())
    }

    @Test
    fun bufferSizeLimitSetToUnlimited_bufferSizeIncreasesWhileActionsDispatched() {
        bufferedStore.changeSizeLimit(BufferedSubscribableStore.UNLIMITED)
        for (i in 2..10) { // the loop starts from 2 because we already have an initial state
            bufferedStore.dispatch(initialAction)
            assertEquals(i, bufferedStore.currentBufferSize())
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun jumpToNegativeStatePosition_exceptionThrown() {
        bufferedStore.jumpToState(-1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun jumpToStatePositionLargerThanLastBufferIndex_exceptionThrown() {
        bufferedStore.jumpToState(bufferedStore.currentBufferSize())
    }

    @Test
    fun severalActionsDispatched_getStateReturnsCorrectState() {
        bufferedStore.changeSizeLimit(5)

        val states = Array(10) { index -> TestState(index) }
        val actions = Array(10) { index -> TestAction(index) }

        bufferedStore.replaceReducer { _, action ->
            states[actions.indexOf(action)]
        }

        for (i in 0..actions.lastIndex) {
            bufferedStore.dispatch(actions[i])
            assertEquals(states[i], bufferedStore.getState())
        }
    }

    @Test
    fun severalActionsDispatched_subscriberReceivesSameAsGetStateReturns() {
        bufferedStore.changeSizeLimit(BufferedSubscribableStore.UNLIMITED)

        val states = Array(10) { index -> TestState(index) }
        val actions = Array(10) { index -> TestAction(index) }

        bufferedStore.replaceReducer { _, action ->
            states[actions.indexOf(action)]
        }

        bufferedStore.subscribe(object : ReduxSubscribableStore.Subscriber<TestState> {
            override fun onNewState(state: TestState) {
                assertEquals(state, bufferedStore.getState())
            }
        })

        for (i in 0..actions.lastIndex) {
            bufferedStore.dispatch(actions[i])
        }
    }

    @Test
    fun severalActionsDispatched_bufferSizeNotOvercomesLimit() {
        val bufferSizeLimit = 5
        bufferedStore.changeSizeLimit(bufferSizeLimit)

        val statesCount = 10
        val states = Array(statesCount) { index -> TestState(index) }
        val actions = Array(statesCount) { index -> TestAction(index) }

        bufferedStore.replaceReducer { _, action ->
            states[actions.indexOf(action)]
        }

        for (i in 0..actions.lastIndex) {
            bufferedStore.dispatch(actions[i])
            assertTrue(bufferedStore.currentBufferSize() <= bufferSizeLimit)
        }
    }

    @Test
    fun jumpToStateWithinBufferBounds_getStateReturnsCorrectState() {
        bufferedStore.changeSizeLimit(BufferedSubscribableStore.UNLIMITED)

        val statesCount = 10
        val states = Array(statesCount) { index -> TestState(index) }
        val actions = Array(statesCount) { index -> TestAction(index) }
        val indices = IntArray(statesCount) { index -> index }.toMutableList()

        bufferedStore.replaceReducer { _, action ->
            states[actions.indexOf(action)]
        }

        for (i in 0..actions.lastIndex) {
            bufferedStore.dispatch(actions[i])
        }

        assertEquals(statesCount + 1, bufferedStore.currentBufferSize()) // +1 because of the initial state

        Collections.shuffle(indices)

        for (index in indices) {
            bufferedStore.jumpToState(index + 1) // +1 because of initial state
            assertEquals(states[index], bufferedStore.getState())
        }
    }

    @Test
    fun actionDispatchedWhileCurrentPositionPointingToTheEndOfBuffer_currentBufferPositionReturnsTheLastIndex() {
        bufferedStore.changeSizeLimit(2)
        bufferedStore.dispatch(initialAction)
        assertEquals(bufferedStore.currentBufferSize() - 1, bufferedStore.currentBufferPosition())
    }

    @Test
    fun actionDispatchedWhileCurrentPositionPointingToTheBeginningOfBuffer_currentBufferPositionReturnsTheLastIndex() {
        bufferedStore.changeSizeLimit(2)
        bufferedStore.jumpToState(0)
        bufferedStore.dispatch(initialAction)
        assertEquals(bufferedStore.currentBufferSize() - 1, bufferedStore.currentBufferPosition())
    }

    @Test
    fun subscriberSubscribed_subscriberReceivesLatestState() {
        bufferedStore.changeSizeLimit(BufferedSubscribableStore.UNLIMITED)
        bufferedStore.dispatch(initialAction)

        val subscriber = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        bufferedStore.subscribe(subscriber)
        verify(subscriber, times(1)).onNewState(newState)
    }

    @Test
    fun jumpToStateInvoked_subscriberReceivesSelectedState() {
        bufferedStore.changeSizeLimit(BufferedSubscribableStore.UNLIMITED)
        bufferedStore.dispatch(initialAction)

        val subscriber = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        bufferedStore.subscribe(subscriber)
        verify(subscriber, times(1)).onNewState(newState)

        bufferedStore.jumpToState(0)
        verify(subscriber, times(1)).onNewState(initialState)
    }

    @Test
    fun clearBufferInvoked_bufferSizeEqualsOne() {
        bufferedStore.changeSizeLimit(BufferedSubscribableStore.UNLIMITED)
        bufferedStore.dispatch(initialAction)
        assertEquals(2, bufferedStore.currentBufferSize())

        bufferedStore.resetBuffer(initialState)
        assertEquals(1, bufferedStore.currentBufferSize())
    }

    @Test
    fun clearBufferInvoked_getStateReturnsInitialState() {
        bufferedStore.changeSizeLimit(BufferedSubscribableStore.UNLIMITED)
        bufferedStore.dispatch(initialAction)
        assertEquals(2, bufferedStore.currentBufferSize())

        bufferedStore.resetBuffer(initialState)
        assertEquals(initialState, bufferedStore.getState())
    }

}