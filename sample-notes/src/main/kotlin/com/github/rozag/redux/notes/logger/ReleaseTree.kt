package com.github.rozag.redux.notes.logger

import android.annotation.SuppressLint
import android.util.Log
import timber.log.Timber.Tree

@SuppressLint("LogNotTimber")
class ReleaseTree : Tree() {

    private val maxLogLength = 8000

    override fun isLoggable(tag: String?, priority: Int) = !listOf(Log.VERBOSE, Log.DEBUG, Log.INFO).contains(priority)

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (isLoggable(tag, priority)) {
            if (priority == Log.ERROR && t != null) {
                t.printStackTrace()
                // MyCrashReportingTool.report(t)
            }

            if (tag != null) {
                if (message.length < maxLogLength) {
                    if (priority == Log.ASSERT) {
                        Log.wtf(tag, message)
                    } else {
                        Log.println(priority, tag, message)
                    }
                    return
                }

                var i = 0
                val length = message.length
                while (i < length) {
                    var newLine = message.indexOf('\n', i)
                    newLine = if (newLine != -1) newLine else length
                    do {
                        val end = Math.min(newLine, i + maxLogLength)
                        val part = message.substring(i, end)
                        if (priority == Log.ASSERT) {
                            Log.wtf(tag, part)
                        } else {
                            Log.println(priority, tag, part)
                        }
                        i = end
                    } while (i < newLine)
                    i++
                }
            }
        }
    }

}