package com.github.rozag.redux.notes.screen.splash

import android.content.Intent
import android.os.Bundle
import com.github.rozag.redux.notes.Action
import com.github.rozag.redux.notes.BaseActivity
import com.github.rozag.redux.notes.NotesApplication
import com.github.rozag.redux.notes.NotesStore
import com.github.rozag.redux.notes.prefs.Prefs
import com.github.rozag.redux.notes.screen.list.ListActivity

class SplashActivity : BaseActivity() {

    override val layoutResourceId: Int = 0
    override val toolbarTitleId: Int = 0
    override val displayHomeAsUp: Boolean = false
    override val homeButtonEnabled: Boolean = false

    private val prefs: Prefs = NotesApplication.prefs
    private val store: NotesStore = NotesApplication.store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // First launch stuff
        if (prefs.isFirstLaunch()) {
            prefs.onFirstLaunchPerformed()
            store.dispatch(Action.FirstLaunch.Started())
        }

        // Start the notes list screen
        startActivity(Intent(this, ListActivity::class.java))
        finish()
    }

}