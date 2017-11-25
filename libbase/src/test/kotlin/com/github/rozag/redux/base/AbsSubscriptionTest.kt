package com.github.rozag.redux.base

import com.github.rozag.redux.core.TestAction
import com.github.rozag.redux.core.TestState
import com.github.rozag.redux.core.store.StoreTest
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test

@Suppress("FunctionName")
abstract class AbsSubscriptionTest : StoreTest() {

    protected abstract val subscribableStore: ReduxSubscribableStore<TestState, TestAction>

    @Before
    override fun setUp() {
        initialState = TestState(-2)
        initialAction = TestAction(-1)
        newState = TestState(-1)
        reducer = { _, _ -> newState }
        store = subscribableStore
    }

    @Test
    fun subscriberSubscribed_subscriberReceivesCurrentState() {
        val subscriber = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        subscribableStore.subscribe(subscriber)

        verify(subscriber, times(1)).onNewState(initialState)
    }

    @Test
    fun actionDispatched_subscriberReceivesNewState() {
        val subscriber = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        subscribableStore.subscribe(subscriber)

        subscribableStore.dispatch(initialAction)
        verify(subscriber, times(1)).onNewState(newState)
    }

    @Test
    fun actionDispatched_unsubscribedSubscriberNotInvoked() {
        val subscriber = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        val connection = subscribableStore.subscribe(subscriber)
        connection.cancel()

        subscribableStore.dispatch(initialAction)
        verify(subscriber, times(0)).onNewState(newState)
    }

    @Test
    fun actionDispatched_oneSubscriberInvokedWhileUnsubscribedOneNot() {
        val subscriberOne = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        val connectionOne = subscribableStore.subscribe(subscriberOne)
        connectionOne.cancel()

        val subscriberTwo = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        subscribableStore.subscribe(subscriberTwo)

        subscribableStore.dispatch(initialAction)
        verify(subscriberOne, times(0)).onNewState(newState)
        verify(subscriberTwo, times(1)).onNewState(newState)
    }

}