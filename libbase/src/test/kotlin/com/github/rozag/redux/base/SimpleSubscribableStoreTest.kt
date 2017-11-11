package com.github.rozag.redux.base

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxState
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("FunctionName")
class SimpleSubscribableStoreTest {

    private data class TestAction(val number: Int) : ReduxAction
    private data class TestState(val number: Int) : ReduxState

    private val initialState = TestState(1)
    private val initialAction = TestAction(1)
    private val newState = TestState(2)
    private val store = SimpleSubscribableStore<TestState, TestAction>(initialState) { _, _ -> newState }

    @Test
    fun actionDispatched_reducerReceivesAction() {
        val store = SimpleSubscribableStore<TestState, TestAction>(initialState) { state, action ->
            assertEquals(initialState, state)
            assertEquals(initialAction, action)
            state
        }
        store.dispatch(initialAction)
    }

    @Test
    fun actionDispatched_storeHasNewState() {
        store.dispatch(initialAction)
        assertEquals(newState, store.getState())
    }

    @Test
    fun subscriberSubscribed_subscriberReceivesCurrentState() {
        val subscriber = mock<SubscribableStore.Subscriber<TestState>>()
        store.subscribe(subscriber)

        verify(subscriber, times(1)).onNewState(initialState)
    }

    @Test
    fun actionDispatched_subscriberReceivesNewState() {
        val subscriber = mock<SubscribableStore.Subscriber<TestState>>()
        store.subscribe(subscriber)

        store.dispatch(initialAction)
        verify(subscriber, times(1)).onNewState(newState)
    }

    @Test
    fun actionDispatched_unsubscribedSubscriberNotInvoked() {
        val subscriber = mock<SubscribableStore.Subscriber<TestState>>()
        val connection = store.subscribe(subscriber)
        connection.unsubscribe()

        store.dispatch(initialAction)
        verify(subscriber, times(0)).onNewState(newState)
    }

    @Test
    fun actionDispatched_oneSubscriberInvokedWhileUnsubscribedOneNot() {
        val subscriberOne = mock<SubscribableStore.Subscriber<TestState>>()
        val connectionOne = store.subscribe(subscriberOne)
        connectionOne.unsubscribe()

        val subscriberTwo = mock<SubscribableStore.Subscriber<TestState>>()
        store.subscribe(subscriberTwo)

        store.dispatch(initialAction)
        verify(subscriberOne, times(0)).onNewState(newState)
        verify(subscriberTwo, times(1)).onNewState(newState)
    }

    @Test
    fun actionDispatched_singleMiddlewareInvoked() {
        val middleware = mock<ReduxMiddleware<TestState, TestAction>>()
        store.applyMiddleware(middleware)

        store.dispatch(initialAction)
        verify(middleware, times(1)).dispatch(initialState, initialAction)
    }

    @Test
    fun actionDispatched_severalMiddlewareInvoked() {
        val middlewareOne = mock<ReduxMiddleware<TestState, TestAction>>()
        val middlewareTwo = mock<ReduxMiddleware<TestState, TestAction>>()
        store.applyMiddleware(middlewareOne, middlewareTwo)

        store.dispatch(initialAction)
        verify(middlewareOne, times(1)).dispatch(initialState, initialAction)
        verify(middlewareTwo, times(1)).dispatch(initialState, initialAction)
    }

}