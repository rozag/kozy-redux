package com.github.rozag.redux.coroutines

import com.github.rozag.redux.core.Action
import com.github.rozag.redux.core.Middleware
import com.github.rozag.redux.core.State
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import kotlin.coroutines.experimental.CoroutineContext

class SimpleSubscribableStore<S : State, A : Action>(
        private var state: S,
        private val reducer: (state: S, action: A) -> S,
        private val coroutineContext: CoroutineContext = UI
) : SubscribableStore<S, A> {

    private val subscriberList: MutableList<SubscribableStore.Subscriber<S>> = ArrayList()
    private val middlewareList: MutableList<Middleware<S, A>> = ArrayList()

    override fun getState(): S = state

    override fun dispatch(action: A) {
        async(coroutineContext) {
            val newStateDeferred: Deferred<S> = bg {
                // Apply every middleware
                var middlewareHandledState = state
                middlewareList.forEach { middleware ->
                    middlewareHandledState = middleware.dispatch(middlewareHandledState, action)
                }

                // Apply the reducer graph
                reducer(middlewareHandledState, action)
            }

            // Save the new state
            state = newStateDeferred.await()

            // Notify subscribers
            subscriberList.forEach { subscriber ->
                subscriber.onNewState(state)
            }
        }
    }

    override fun applyMiddleware(vararg middlewareArray: Middleware<S, A>) {
        middlewareList.addAll(middlewareArray)
    }

    override fun subscribe(subscriber: SubscribableStore.Subscriber<S>): SubscribableStore.Connection {
        subscriberList.add(subscriber)
        subscriber.onNewState(state)
        return object : SubscribableStore.Connection {
            override fun unsubscribe() {
                subscriberList.remove(subscriber)
            }
        }
    }

}