package com.github.rozag.redux.notes

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.util.Log
import com.github.rozag.kueue.Kueue
import com.github.rozag.kueue.android.MainThreadExecutor
import com.github.rozag.redux.base.SubscribableBufferedStore
import com.github.rozag.redux.notes.database.DB_NAME
import com.github.rozag.redux.notes.database.NotesDatabase
import com.github.rozag.redux.notes.middleware.FirstLaunchMiddleware
import com.github.rozag.redux.notes.middleware.LoggingMiddleware
import com.github.rozag.redux.notes.prefs.AndroidPrefs
import com.github.rozag.redux.notes.prefs.Prefs
import com.github.rozag.redux.notes.repo.CompositeNotesRepo
import com.github.rozag.redux.notes.repo.FakeRemoteNotesRepo
import com.github.rozag.redux.notes.repo.LocalNotesRepo
import com.github.rozag.redux.notes.resources.AndroidResProvider
import com.github.rozag.redux.notes.resources.ResProvider
import com.github.rozag.redux.notes.router.Router
import com.github.rozag.redux.notes.screen.edit.creator.UpdateNoteAndExitActionCreator
import com.github.rozag.redux.notes.screen.list.creator.DeleteNoteActionCreator
import com.github.rozag.redux.notes.screen.list.creator.LoadNotesActionCreator
import com.github.rozag.redux.notes.screen.list.creator.NewNoteActionCreator
import com.github.rozag.redux.notes.timber.ReleaseTree
import timber.log.Timber
import java.util.concurrent.Executors

class NotesApplication : Application() {

    // TODO: todo-notes

    companion object {
        private const val PREFS_NAME = "notes"
        lateinit var prefs: Prefs

        lateinit var resProvider: ResProvider

        lateinit var store: NotesStore

        lateinit var router: Router

        // List screen action creators
        lateinit var loadNotesActionCreator: LoadNotesActionCreator
        lateinit var newNoteActionCreator: NewNoteActionCreator
        lateinit var deleteNoteActionCreator: DeleteNoteActionCreator

        // Edit screen action creators
        lateinit var updateNoteAndExitActionCreator: UpdateNoteAndExitActionCreator
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize the logger
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

        // Initialize prefs
        val sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = AndroidPrefs(sharedPrefs)

        // Initialize resources provider
        resProvider = AndroidResProvider(this)

        // Initialize repositories
        val notesDatabase = Room.databaseBuilder(
                this,
                NotesDatabase::class.java,
                DB_NAME
        ).build()
        val notesDao = notesDatabase.notesDao()
        val localNotesRepo = LocalNotesRepo(notesDao)
        val remoteNotesRepo = FakeRemoteNotesRepo(sleepMillis = 3000)
        val notesRepo = CompositeNotesRepo(
                localRepo = localNotesRepo,
                remoteRepo = remoteNotesRepo
        )

        // Initialize id generator
        val idGenerator = IdGenerator()

        // Initialize executors
        val mainThreadExecutor = MainThreadExecutor()
        val diskIoThreadExecutor = Executors.newSingleThreadExecutor()

        // Initialize task queues
        val taskQueue = Kueue(
                workerExecutor = diskIoThreadExecutor,
                callbackExecutor = mainThreadExecutor
        )

        // Initialize the store
        store = SubscribableBufferedStore(
                initialState = AppState.EMPTY,
                reducer = ::rootReducer,
                bufferSizeLimit = 2,
                initialBufferSize = 2
        )
        store.applyMiddleware(
                LoggingMiddleware(
                        logLevel = Log.VERBOSE,
                        packageName = packageName.replace(".debug", "")
                ),
                FirstLaunchMiddleware(
                        idGenerator = idGenerator,
                        resProvider = resProvider,
                        repo = notesRepo,
                        taskQueue = taskQueue
                )
        )

        // Initialize router
        router = Router(store)

        // Initialize list screen action creators
        loadNotesActionCreator = LoadNotesActionCreator(
                store = store,
                repo = notesRepo,
                taskQueue = taskQueue
        )
        newNoteActionCreator = NewNoteActionCreator(
                idGenerator = idGenerator,
                store = store,
                repo = notesRepo,
                taskQueue = taskQueue
        )
        deleteNoteActionCreator = DeleteNoteActionCreator(
                store = store,
                repo = notesRepo,
                taskQueue = taskQueue
        )

        // Initialize edit screen action creators
        updateNoteAndExitActionCreator = UpdateNoteAndExitActionCreator(
                store = store,
                repo = notesRepo,
                taskQueue = taskQueue
        )
    }

}