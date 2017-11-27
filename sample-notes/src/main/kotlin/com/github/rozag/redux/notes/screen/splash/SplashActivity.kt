package com.github.rozag.redux.notes.screen.splash

import android.os.Bundle
import com.github.rozag.redux.notes.AppState
import com.github.rozag.redux.notes.NotesAction
import com.github.rozag.redux.notes.NotesApplication
import com.github.rozag.redux.notes.ReduxActivity
import com.github.rozag.redux.notes.prefs.Prefs
import com.github.rozag.redux.notes.router.RouterAction
import com.github.rozag.redux.notes.router.Screen

class SplashActivity : ReduxActivity() {

    override val layoutResourceId: Int = 0
    override val toolbarTitleId: Int = 0
    override val displayHomeAsUp: Boolean = false
    override val homeButtonEnabled: Boolean = false

    private val prefs: Prefs = NotesApplication.prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // First launch stuff
        if (prefs.isFirstLaunch()) {
            prefs.onFirstLaunchPerformed()
            store.dispatch(NotesAction.FirstLaunch.Started())
        }

        // Start the notes list screen
        store.dispatch(RouterAction.Open(Screen.LIST))
    }

    override fun onNewState(state: AppState) {
        super.onNewState(state)
        if (state.routerState.currentScreen != Screen.NONE) {
            finish()
        }
    }

}