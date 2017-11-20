package com.github.rozag.redux.notes.screen.splash

import android.content.Intent
import android.os.Bundle
import com.github.rozag.redux.notes.BaseActivity
import com.github.rozag.redux.notes.screen.list.NotesListActivity

class SplashActivity : BaseActivity() {

    override val layoutResourceId: Int = 0
    override val toolbarTitleId: Int = 0
    override val displayHomeAsUp: Boolean = false
    override val homeButtonEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, NotesListActivity::class.java))
        finish()
    }

}