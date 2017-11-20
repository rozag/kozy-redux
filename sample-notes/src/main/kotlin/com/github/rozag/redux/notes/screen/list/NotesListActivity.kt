package com.github.rozag.redux.notes.screen.list

import android.os.Bundle
import com.github.rozag.redux.notes.BaseActivity
import com.github.rozag.redux.notes.R

class NotesListActivity : BaseActivity() {

    override val layoutResourceId = R.layout.activity_main
    override val toolbarTitleId = R.string.app_name
    override val displayHomeAsUp = false
    override val homeButtonEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}