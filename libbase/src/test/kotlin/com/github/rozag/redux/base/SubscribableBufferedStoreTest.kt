package com.github.rozag.redux.base

import com.github.rozag.redux.core.TestAction
import com.github.rozag.redux.core.TestState
import com.github.rozag.redux.core.store.BufferedStore
import com.github.rozag.redux.core.store.BufferedStoreTest
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@Suppress("FunctionName")
class SubscribableBufferedStoreTest : BufferedStoreTest() {

    private lateinit var subscribableStore: ReduxSubscribableStore<TestState, TestAction>

    private val initialBufferSizeLimit = 1

    @Before
    override fun setUp() {
        initialState = TestState(-2)
        initialAction = TestAction(-1)
        newState = TestState(-1)
        reducer = { _, _ -> newState }
        subscribableStore = SubscribableBufferedStore(initialState, reducer, initialBufferSizeLimit)
        bufferedStore = subscribableStore as SubscribableBufferedStore<TestState, TestAction>
        store = bufferedStore
    }

    @Test
    fun severalActionsDispatched_subscriberReceivesSameAsGetStateReturns() {
        bufferedStore.changeSizeLimit(BufferedStore.UNLIMITED)

        val states = Array(10) { index -> TestState(index) }
        val actions = Array(10) { index -> TestAction(index) }

        bufferedStore.replaceReducer { _, action ->
            states[actions.indexOf(action)]
        }

        subscribableStore.subscribe(object : ReduxSubscribableStore.Subscriber<TestState> {
            override fun onNewState(state: TestState) {
                assertEquals(state, bufferedStore.getState())
            }
        })

        for (i in 0..actions.lastIndex) {
            bufferedStore.dispatch(actions[i])
        }
    }

    @Test
    fun subscriberSubscribed_subscriberReceivesLatestState() {
        bufferedStore.changeSizeLimit(BufferedStore.UNLIMITED)
        bufferedStore.dispatch(initialAction)

        val subscriber = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        subscribableStore.subscribe(subscriber)
        verify(subscriber, times(1)).onNewState(newState)
    }

    @Test
    fun jumpToStateInvoked_subscriberReceivesSelectedState() {
        bufferedStore.changeSizeLimit(BufferedStore.UNLIMITED)
        bufferedStore.dispatch(initialAction)

        val subscriber = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        subscribableStore.subscribe(subscriber)
        verify(subscriber, times(1)).onNewState(newState)

        bufferedStore.jumpToState(0)
        verify(subscriber, times(1)).onNewState(initialState)
    }

    @Test
    fun resetBufferInvoked_subscriberNotified() {
        bufferedStore.changeSizeLimit(BufferedStore.UNLIMITED)
        bufferedStore.dispatch(initialAction)

        val subscriber = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        subscribableStore.subscribe(subscriber)
        verify(subscriber, times(1)).onNewState(newState)

        bufferedStore.resetBuffer(initialState)
        verify(subscriber, times(1)).onNewState(initialState)
    }

    @Test
    fun resetToStateInvoked_subscriberReceivesSelectedState() {
        bufferedStore.changeSizeLimit(BufferedStore.UNLIMITED)
        bufferedStore.dispatch(initialAction)

        val subscriber = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        subscribableStore.subscribe(subscriber)
        verify(subscriber, times(1)).onNewState(newState)

        bufferedStore.resetToState(0)
        verify(subscriber, times(1)).onNewState(initialState)
    }

}