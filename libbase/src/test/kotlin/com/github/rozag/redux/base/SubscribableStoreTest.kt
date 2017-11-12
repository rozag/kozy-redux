package com.github.rozag.redux.base

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxState
import com.github.rozag.redux.core.ReduxStore
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("FunctionName")
class SubscribableStoreTest {

    private data class TestAction(val number: Int) : ReduxAction
    private data class TestState(val number: Int) : ReduxState

    private val initialState = TestState(1)
    private val initialAction = TestAction(1)
    private val newState = TestState(2)
    private val store: ReduxSubscribableStore<TestState, TestAction> = SubscribableStore(initialState) { _, _ -> newState }

    @Test
    fun actionDispatched_reducerReceivesAction() {
        val store = SubscribableStore<TestState, TestAction>(initialState) { state, action ->
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
        val subscriber = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        store.subscribe(subscriber)

        verify(subscriber, times(1)).onNewState(initialState)
    }

    @Test
    fun actionDispatched_subscriberReceivesNewState() {
        val subscriber = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        store.subscribe(subscriber)

        store.dispatch(initialAction)
        verify(subscriber, times(1)).onNewState(newState)
    }

    @Test
    fun actionDispatched_unsubscribedSubscriberNotInvoked() {
        val subscriber = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        val connection = store.subscribe(subscriber)
        connection.unsubscribe()

        store.dispatch(initialAction)
        verify(subscriber, times(0)).onNewState(newState)
    }

    @Test
    fun actionDispatched_oneSubscriberInvokedWhileUnsubscribedOneNot() {
        val subscriberOne = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        val connectionOne = store.subscribe(subscriberOne)
        connectionOne.unsubscribe()

        val subscriberTwo = mock<ReduxSubscribableStore.Subscriber<TestState>>()
        store.subscribe(subscriberTwo)

        store.dispatch(initialAction)
        verify(subscriberOne, times(0)).onNewState(newState)
        verify(subscriberTwo, times(1)).onNewState(newState)
    }

    @Test
    fun actionDispatched_singleMiddlewareInvoked() {
        val middleware = mock<ReduxMiddleware<TestState, TestAction, ReduxStore<TestState, TestAction>>>()
        store.applyMiddleware(middleware)

        store.dispatch(initialAction)
        verify(middleware, times(1)).doBeforeDispatch(store, initialAction)
    }

    @Test
    fun actionDispatched_severalMiddlewareInvoked() {
        val middlewareOne = mock<ReduxMiddleware<TestState, TestAction, ReduxStore<TestState, TestAction>>>()
        val middlewareTwo = mock<ReduxMiddleware<TestState, TestAction, ReduxStore<TestState, TestAction>>>()
        store.applyMiddleware(middlewareOne, middlewareTwo)

        store.dispatch(initialAction)
        verify(middlewareOne, times(1)).doBeforeDispatch(store, initialAction)
        verify(middlewareTwo, times(1)).doBeforeDispatch(store, initialAction)
    }

    @Test
    fun actionDispatched_secondMiddlewareMethodsInvokedAroundTheFirstMiddlewareMethods() {
        val middlewareOne = mock<ReduxMiddleware<TestState, TestAction, ReduxStore<TestState, TestAction>>>()
        val middlewareTwo = mock<ReduxMiddleware<TestState, TestAction, ReduxStore<TestState, TestAction>>>()
        store.applyMiddleware(middlewareOne, middlewareTwo)

        val inOrder = inOrder(middlewareOne, middlewareTwo)

        store.dispatch(initialAction)
        inOrder.verify(middlewareTwo).doBeforeDispatch(store, initialAction)
        inOrder.verify(middlewareOne).doBeforeDispatch(store, initialAction)
        inOrder.verify(middlewareOne).doAfterDispatch(store, initialAction)
        inOrder.verify(middlewareTwo).doAfterDispatch(store, initialAction)
    }

}