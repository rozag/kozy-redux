package com.github.rozag.redux.base

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxBufferedStore
import com.github.rozag.redux.core.ReduxState

interface ReduxBufferedSubscribableStore<S : ReduxState, A : ReduxAction> :
        ReduxSubscribableStore<S, A>, ReduxBufferedStore<S, A>