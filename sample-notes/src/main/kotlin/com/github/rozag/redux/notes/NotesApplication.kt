package com.github.rozag.redux.notes

import android.app.Application
import android.arch.persistence.room.Room
import com.github.rozag.redux.base.BufferedSubscribableStore
import com.github.rozag.redux.notes.database.DB_NAME
import com.github.rozag.redux.notes.database.NotesDatabase
import com.github.rozag.redux.notes.executor.DiskIoThreadExecutor
import com.github.rozag.redux.notes.executor.MainThreadExecutor
import com.github.rozag.redux.notes.executor.NetworkIoThreadExecutor
import com.github.rozag.redux.notes.logger.Logger
import com.github.rozag.redux.notes.logger.ReleaseTree
import com.github.rozag.redux.notes.logger.TimberLogger
import com.github.rozag.redux.notes.middleware.LoggingMiddleware
import com.github.rozag.redux.notes.repository.CompositeNotesRepository
import com.github.rozag.redux.notes.repository.FakeRemoteNotesRepository
import com.github.rozag.redux.notes.repository.LocalNotesRepository
import com.github.rozag.redux.notes.screen.list.LoadNotesActionCreator
import timber.log.Timber

class NotesApplication : Application() {

    companion object {
        lateinit var logger: Logger
        lateinit var store: Store
        lateinit var loadNotesActionCreator: ActionCreator
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize the logger
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
        logger = TimberLogger()

        // Initialize repositories
        val notesDatabase = Room.databaseBuilder(
                this,
                NotesDatabase::class.java,
                DB_NAME
        ).build()
        val notesDao = notesDatabase.notesDao()
        val localNotesRepo = LocalNotesRepository(notesDao)
        val remoteNotesRepo = FakeRemoteNotesRepository(sleepMillis = 3000)
        val notesRepo = CompositeNotesRepository(
                localRepo = localNotesRepo,
                remoteRepo = remoteNotesRepo
        )

        // Initialize the store
        store = BufferedSubscribableStore(
                initialState = State.EMPTY,
                reducer = ::rootReducer,
                bufferSizeLimit = 2,
                initialBufferSize = 2
        )
        val loggingMiddleware = LoggingMiddleware(logger = logger)
        store.applyMiddleware(loggingMiddleware)

        // Initialize executors
        val mainThreadExecutor = MainThreadExecutor()
        val diskIoThreadExecutor = DiskIoThreadExecutor()
        val networkIoThreadExecutor = NetworkIoThreadExecutor(threadCount = 3)

        // Initialize action creators
        loadNotesActionCreator = LoadNotesActionCreator(
                workExecutor = diskIoThreadExecutor,
                callbackExecutor = mainThreadExecutor,
                store = store,
                repo = notesRepo
        )
    }

}