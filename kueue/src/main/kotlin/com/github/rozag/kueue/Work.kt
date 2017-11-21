package com.github.rozag.kueue

import java.util.concurrent.Executor

class Work<T> internal constructor(
        private val workerExecutor: Executor,
        private val callbackExecutor: Executor,
        private val runTask: (onSuccess: (T) -> Unit, onError: OnError) -> Unit
) {

    private var onSuccess: ((T) -> Unit)? = null
    private var onError: OnError? = null

    fun onSuccess(onSuccess: (T) -> Unit): Work<T> {
        this.onSuccess = onSuccess
        return this
    }

    fun onError(onError: OnError): Work<T> {
        this.onError = onError
        return this
    }

    fun go() {
        workerExecutor.execute {
            runTask(wrapOnSuccess(onSuccess), wrapOnError(onError))
        }
    }

    private fun wrapOnSuccess(onSuccess: ((T) -> Unit)?): (T) -> Unit {
        return if (onSuccess == null) {
            {}
        } else {
            { result: T ->
                callbackExecutor.execute {
                    onSuccess(result)
                }
            }
        }
    }

    private fun wrapOnError(onError: OnError?): OnError {
        return if (onError == null) {
            {}
        } else {
            { throwable ->
                callbackExecutor.execute {
                    onError(throwable)
                }
            }
        }
    }

}