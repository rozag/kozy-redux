package com.github.rozag.redux.notes

import android.app.Application
import com.github.rozag.redux.base.BufferedSubscribableStore
import com.github.rozag.redux.notes.logger.AndroidLogger
import com.github.rozag.redux.notes.middleware.LoggingMiddleware

class NotesApplication : Application() {

    companion object {
        val store: Store = BufferedSubscribableStore(
                initialState = State.EMPTY,
                reducer = ::rootReducer,
                bufferSizeLimit = 2,
                initialBufferSize = 2
        )
    }

    override fun onCreate() {
        super.onCreate()
        store.applyMiddleware(LoggingMiddleware(AndroidLogger()))
    }

}