package com.github.rozag.kueue

import java.util.concurrent.Executor

class Work<T> internal constructor(
        private val workerExecutor: Executor,
        private val callbackExecutor: Executor,
        private val runTask: (onComplete: (T) -> Unit, onError: OnError) -> Unit
) {

    private var onComplete: ((T) -> Unit)? = null
    private var onError: OnError? = null

    fun onComplete(onComplete: (T) -> Unit): Work<T> {
        this.onComplete = onComplete
        return this
    }

    fun onError(onError: OnError): Work<T> {
        this.onError = onError
        return this
    }

    fun go() {
        workerExecutor.execute {
            runTask(wrapOnComplete(onComplete), wrapOnError(onError))
        }
    }

    private fun wrapOnComplete(onComplete: ((T) -> Unit)?): (T) -> Unit {
        return if (onComplete == null) {
            {}
        } else {
            { result: T ->
                callbackExecutor.execute {
                    onComplete(result)
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