package com.github.rozag.redux.coroutines

import com.github.rozag.redux.core.Action
import com.github.rozag.redux.core.Middleware
import com.github.rozag.redux.core.State
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("FunctionName")
class SimpleSubscribableStoreTest {

    private data class TestAction(val number: Int) : Action
    private data class TestState(val number: Int) : State

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
        val middleware = mock<Middleware<TestState, TestAction>> {
            on { dispatch(initialState, initialAction) } doReturn initialState
        }
        store.applyMiddleware(middleware)

        store.dispatch(initialAction)
        verify(middleware, times(1)).dispatch(initialState, initialAction)
    }

    @Test
    fun actionDispatched_severalMiddlewareInvoked() {
        val middlewareOne = mock<Middleware<TestState, TestAction>> {
            on { dispatch(initialState, initialAction) } doReturn initialState
        }
        val middlewareTwo = mock<Middleware<TestState, TestAction>> {
            on { dispatch(initialState, initialAction) } doReturn initialState
        }
        store.applyMiddleware(middlewareOne, middlewareTwo)

        store.dispatch(initialAction)
        verify(middlewareOne, times(1)).dispatch(initialState, initialAction)
        verify(middlewareTwo, times(1)).dispatch(initialState, initialAction)
    }

    @Test
    fun actionDispatched_reducerReceivesStateChangedBySingleMiddleware() {
        val store = SimpleSubscribableStore<TestState, TestAction>(initialState) { state, _ ->
            assertEquals(newState, state)
            state
        }

        val middleware = mock<Middleware<TestState, TestAction>> {
            on { dispatch(initialState, initialAction) } doReturn newState
        }
        store.applyMiddleware(middleware)

        store.dispatch(initialAction)
    }

    @Test
    fun actionDispatched_reducerReceivesStateChangedBySeveralMiddleware() {
        val stateOne = TestState(1)
        val stateTwo = TestState(2)
        val stateThree = TestState(3)
        val initialAction = TestAction(1)
        val store = SimpleSubscribableStore<TestState, TestAction>(stateOne) { state, _ ->
            assertEquals(stateThree, state)
            state
        }

        val middlewareOne = mock<Middleware<TestState, TestAction>> {
            on { dispatch(stateOne, initialAction) } doReturn stateTwo
        }
        val middlewareTwo = mock<Middleware<TestState, TestAction>> {
            on { dispatch(stateTwo, initialAction) } doReturn stateThree
        }
        store.applyMiddleware(middlewareOne, middlewareTwo)

        store.dispatch(initialAction)
    }

}