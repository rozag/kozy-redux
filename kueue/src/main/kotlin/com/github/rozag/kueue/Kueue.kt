package com.github.rozag.kueue

import java.util.concurrent.Executor

class Kueue(
        private val workerExecutor: Executor,
        private val callbackExecutor: Executor
) {

    fun <T> fromCallable(callable: () -> T) = perform<T> { onSuccess, onError ->
        try {
            val result = callable()
            onSuccess(result)
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    fun <T> perform(runTask: (onSuccess: (T) -> Unit, onError: OnError) -> Unit) = Work(workerExecutor, callbackExecutor, runTask)

}