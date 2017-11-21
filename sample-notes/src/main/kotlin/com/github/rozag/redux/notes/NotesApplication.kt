package com.github.rozag.redux.notes

import android.app.Application
import android.arch.persistence.room.Room
import com.github.rozag.kueue.Kueue
import com.github.rozag.kueue.android.MainThreadExecutor
import com.github.rozag.redux.base.BufferedSubscribableStore
import com.github.rozag.redux.notes.database.DB_NAME
import com.github.rozag.redux.notes.database.NotesDatabase
import com.github.rozag.redux.notes.middleware.LoggingMiddleware
import com.github.rozag.redux.notes.repository.CompositeNotesRepository
import com.github.rozag.redux.notes.repository.FakeRemoteNotesRepository
import com.github.rozag.redux.notes.repository.LocalNotesRepository
import com.github.rozag.redux.notes.screen.list.LoadNotesActionCreator
import com.github.rozag.redux.notes.timber.ReleaseTree
import timber.log.Timber
import java.util.concurrent.Executors

class NotesApplication : Application() {

    companion object {
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
        val loggingMiddleware = LoggingMiddleware()
        store.applyMiddleware(loggingMiddleware)

        // Initialize executors
        val mainThreadExecutor = MainThreadExecutor()
        val diskIoThreadExecutor = Executors.newSingleThreadExecutor()
//        val networkIoThreadExecutor = Executors.newFixedThreadPool(3)

        // Initialize task queues
        val taskQueue = Kueue(diskIoThreadExecutor, mainThreadExecutor)

        // Initialize action creators
        loadNotesActionCreator = LoadNotesActionCreator(
                queue = taskQueue,
                store = store,
                repo = notesRepo
        )
    }

}