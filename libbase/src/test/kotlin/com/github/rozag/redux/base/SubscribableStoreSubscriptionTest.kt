package com.github.rozag.redux.base

class SubscribableStoreSubscriptionTest : AbsSubscriptionTest() {

    override val subscribableStore = SubscribableStore(initialState, reducer)

}