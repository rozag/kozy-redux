package com.github.rozag.redux.base

import com.github.rozag.redux.core.TestAction
import com.github.rozag.redux.core.TestState
import org.junit.Before

class SubscribableStoreSubscriptionTest : AbsSubscriptionTest() {

    @Before
    override fun setUp() {
        initialState = TestState(-2)
        initialAction = TestAction(-1)
        newState = TestState(-1)
        reducer = { _, _ -> newState }
        subscribableStore = SubscribableStore(initialState, reducer)
        store = subscribableStore
    }

}