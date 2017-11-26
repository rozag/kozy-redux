package com.github.rozag.redux.notes.screen.edit

import android.os.Bundle
import com.github.rozag.redux.notes.AppState
import com.github.rozag.redux.notes.R
import com.github.rozag.redux.notes.ReduxActivity

class EditActivity : ReduxActivity() {

    override val layoutResourceId = R.layout.activity_edit
    override val toolbarTitleId = 0
    override val displayHomeAsUp = false
    override val homeButtonEnabled = false

    private var isExiting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.anim.slide_in_left, R.anim.stay)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.stay, R.anim.slide_out_right)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        isExiting = true
        store.dispatch(EditAction.TearDown())
    }

    override fun onNewState(state: AppState) {
        super.onNewState(state)

        if (isExiting) {
            return
        }

        // TODO
    }

}