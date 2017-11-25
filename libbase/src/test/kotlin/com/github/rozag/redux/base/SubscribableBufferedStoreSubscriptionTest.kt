package com.github.rozag.redux.base

class SubscribableBufferedStoreSubscriptionTest : AbsSubscriptionTest() {

    override val subscribableStore = SubscribableBufferedStore(initialState, reducer, initialBufferSize = 1)

}