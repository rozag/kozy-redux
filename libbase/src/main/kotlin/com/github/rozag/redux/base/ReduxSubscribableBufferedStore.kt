package com.github.rozag.redux.base

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxState
import com.github.rozag.redux.core.store.ReduxBufferedStore

/**
 * This interface combines [ReduxSubscribableStore] and [ReduxBufferedStore] interfaces.
 *
 * @param S the type of your [ReduxState]
 * @param A the type of your root [ReduxAction]
 */
interface ReduxSubscribableBufferedStore<S : ReduxState, A : ReduxAction> :
        ReduxSubscribableStore<S, A>,
        ReduxBufferedStore<S, A>