package com.github.rozag.redux.notes.executor

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class NetworkIoThreadExecutor(private val threadCount: Int) : Executor {

    private val networkIo = Executors.newFixedThreadPool(threadCount)

    override fun execute(command: Runnable?) {
        networkIo.execute(command)
    }

}