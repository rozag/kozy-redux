package com.github.rozag.kueue

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.Executor

@Suppress("FunctionName")
class KueueTest {

    private val executor = Executor { runnable -> runnable?.run() }
    private val kueue: Kueue = Kueue(executor, executor)

    @Test
    fun noCallbacksSet_taskPerformed() {
        var taskPerformed = false
        kueue.perform<Boolean> { _, _ ->
            taskPerformed = true
        }.go()
        assertTrue(taskPerformed)
    }

    @Test
    fun onCompleteSet_taskPerformed() {
        var taskPerformed = false
        var onCompleteInvoked = false
        kueue.perform<Boolean> { onComplete, _ ->
            taskPerformed = true
            onComplete(true)
        }
                .onComplete { bool -> onCompleteInvoked = bool }
                .go()
        assertTrue(taskPerformed)
        assertTrue(onCompleteInvoked)
    }

    @Test
    fun onErrorSet_taskPerformed() {
        var taskPerformed = false
        var onErrorInvoked = false
        val error = Exception("test")
        kueue.perform<Boolean> { _, onError ->
            taskPerformed = true
            onError(error)
        }
                .onError { throwable ->
                    onErrorInvoked = true
                    assertEquals(error, throwable)
                }
                .go()
        assertTrue(taskPerformed)
        assertTrue(onErrorInvoked)
    }

    @Test
    fun onCompleteAndOnErrorSet_normalTaskPerformed() {
        var taskPerformed = false
        var onCompleteInvoked = false
        var onErrorInvoked = false
        kueue.perform<Boolean> { onComplete, _ ->
            taskPerformed = true
            onComplete(true)
        }
                .onComplete { bool -> onCompleteInvoked = bool }
                .onError { onErrorInvoked = true }
                .go()
        assertTrue(taskPerformed)
        assertTrue(onCompleteInvoked)
        assertTrue(!onErrorInvoked)
    }

    @Test
    fun onCompleteAndOnErrorSet_errorTaskPerformed() {
        var taskPerformed = false
        var onCompleteInvoked = false
        var onErrorInvoked = false
        val error = Exception("test")
        kueue.perform<Boolean> { _, onError ->
            taskPerformed = true
            onError(error)
        }
                .onComplete { bool -> onCompleteInvoked = bool }
                .onError { throwable ->
                    onErrorInvoked = true
                    assertEquals(error, throwable)
                }
                .go()
        assertTrue(taskPerformed)
        assertTrue(!onCompleteInvoked)
        assertTrue(onErrorInvoked)
    }

    @Test
    fun noCallbacksSet_callableTaskPerformed() {
        var taskPerformed = false
        kueue.fromCallable { taskPerformed = true }.go()
        assertTrue(taskPerformed)
    }

    @Test
    fun onCompleteSet_callableTaskPerformed() {
        var taskPerformed = false
        var onCompleteInvoked = false
        kueue.fromCallable {
            taskPerformed = true
            true
        }
                .onComplete { bool -> onCompleteInvoked = bool }
                .go()
        assertTrue(taskPerformed)
        assertTrue(onCompleteInvoked)
    }

    @Test
    fun onErrorSet_callableTaskPerformed() {
        var taskPerformed = false
        var onErrorInvoked = false
        val error = Exception("test")
        kueue.fromCallable<Boolean> {
            taskPerformed = true
            throw error
        }
                .onError { throwable ->
                    onErrorInvoked = true
                    assertEquals(error, throwable)
                }
                .go()
        assertTrue(taskPerformed)
        assertTrue(onErrorInvoked)
    }

    @Test
    fun onCompleteAndOnErrorSet_normalCallableTaskPerformed() {
        var taskPerformed = false
        var onCompleteInvoked = false
        var onErrorInvoked = false
        kueue.fromCallable {
            taskPerformed = true
            true
        }
                .onComplete { bool -> onCompleteInvoked = bool }
                .onError { onErrorInvoked = true }
                .go()
        assertTrue(taskPerformed)
        assertTrue(onCompleteInvoked)
        assertTrue(!onErrorInvoked)
    }

    @Test
    fun onCompleteAndOnErrorSet_errorCallableTaskPerformed() {
        var taskPerformed = false
        var onCompleteInvoked = false
        var onErrorInvoked = false
        val error = Exception("test")
        kueue.fromCallable<Boolean> {
            taskPerformed = true
            throw error
        }
                .onComplete { bool -> onCompleteInvoked = bool }
                .onError { throwable ->
                    onErrorInvoked = true
                    assertEquals(error, throwable)
                }
                .go()
        assertTrue(taskPerformed)
        assertTrue(!onCompleteInvoked)
        assertTrue(onErrorInvoked)
    }

}