package com.github.rozag.redux.core

/**
 * This class is used to wrap the [ReduxStore.dispatch] method with
 * [doBeforeDispatch] and [doAfterDispatch] methods via
 * [ReduxStore.applyMiddleware], which you implement. You can use it
 * for logging, analytics, etc.
 *
 * Typical usage (logger example):
 * ```
 * // Declare your middleware
 * class LoggingMiddleware : ReduxMiddleware<ReduxState, ReduxAction, ReduxStore<ReduxState, ReduxAction>>() {
 *   override fun doBeforeDispatch(store: ReduxStore<ReduxState, ReduxAction>, action: ReduxAction) {
 *       Log.d("LoggingMiddleware", "Dispatching action: $action")
 *   }
 *
 *   override fun doAfterDispatch(store: ReduxStore<ReduxState, ReduxAction>, action: ReduxAction) {
 *       Log.d("LoggingMiddleware", "New state: ${store.getState()}")
 *   }
 * }
 *
 * // And apply it to your store
 * store.applyMiddleware(LoggingMiddleware())
 * ```
 *
 * @param S the type of your [ReduxState]
 * @param A the type of your root [ReduxAction]
 * @param R the type of your [ReduxStore]
 */
abstract class ReduxMiddleware<S : ReduxState, A : ReduxAction, in R : ReduxStore<S, A>> {

    /**
     * This method is invoked before the [ReduxAction] was dispatched to [ReduxStore].
     *
     * @param store your [ReduxStore]
     * @param action dispatched [ReduxAction]
     */
    abstract fun doBeforeDispatch(store: R, action: A)

    /**
     * This method is invoked after the [ReduxAction] was dispatched to [ReduxStore].
     *
     * @param store your [ReduxStore]
     * @param action dispatched [ReduxAction]
     */
    abstract fun doAfterDispatch(store: R, action: A)

    /**
     * Method which is used internally to wrap the [ReduxStore.dispatch] method
     * with your [ReduxMiddleware]. You shouldn't override this method.
     *
     * @param store your [ReduxStore]
     * @return lambda function which is used to wrap the [ReduxStore.dispatch] method
     */
    fun apply(store: R): ((A) -> Unit) -> (A) -> Unit {
        return { itDispatch: (A) -> Unit ->
            { itAction: A ->
                doBeforeDispatch(store, itAction)
                itDispatch(itAction)
                doAfterDispatch(store, itAction)
            }
        }
    }

}