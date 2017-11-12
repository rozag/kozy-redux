package com.github.rozag.redux.base

import org.junit.Before

@Suppress("FunctionName")
class BufferedSubscribableStoreTest : SubscribableStoreTest() {

    @Before
    override fun setUp() {
        initialState = TestState(1)
        initialAction = TestAction(1)
        newState = TestState(2)
        reducer = { _, _ -> newState }
        store = BufferedSubscribableStore(BufferedSubscribableStore.UNLIMITED, initialState, reducer)
    }

}