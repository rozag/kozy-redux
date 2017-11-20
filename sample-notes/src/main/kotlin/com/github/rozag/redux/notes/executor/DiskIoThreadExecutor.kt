package com.github.rozag.redux.notes.executor

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DiskIoThreadExecutor : Executor {

    private val diskIo = Executors.newSingleThreadExecutor()

    override fun execute(command: Runnable?) {
        diskIo.execute(command)
    }

}