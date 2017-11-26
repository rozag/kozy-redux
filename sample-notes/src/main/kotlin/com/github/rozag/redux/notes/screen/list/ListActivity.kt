package com.github.rozag.redux.notes.screen.list

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View.*
import android.widget.ProgressBar
import com.github.rozag.redux.notes.*

class ListActivity : ReduxActivity() {

    override val layoutResourceId = R.layout.activity_list
    override val toolbarTitleId = R.string.app_name
    override val displayHomeAsUp = false
    override val homeButtonEnabled = false

    private val loadNotesActionCreator: ActionCreator = NotesApplication.loadNotesActionCreator

    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var addNoteButton: FloatingActionButton

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coordinatorLayout = findViewById(R.id.coordinator_layout)
        recyclerView = findViewById(R.id.recycler_view)
        progressBar = findViewById(R.id.progress_bar)
        addNoteButton = findViewById(R.id.add_note_btn)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = ListAdapter(emptyList()) { note ->
            Snackbar.make(coordinatorLayout, "Clicked note id: ${note.id}", Snackbar.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        addNoteButton.setOnClickListener {
            Snackbar.make(coordinatorLayout, "TODO", Snackbar.LENGTH_SHORT).show()
        }

        loadNotesActionCreator.createAndDispatch()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        store.dispatch(ListAction.TearDown())
    }

    override fun onNewState(state: AppState) {
        val listState = state.listState
        when {
            listState.isLoading -> {
                progressBar.visibility = VISIBLE
                recyclerView.visibility = INVISIBLE
            }
            listState.isError -> {
                progressBar.visibility = GONE
                recyclerView.visibility = VISIBLE
                Snackbar.make(coordinatorLayout, "Error", Snackbar.LENGTH_SHORT).show()
            }
            else -> {
                progressBar.visibility = GONE
                recyclerView.visibility = VISIBLE
                adapter.updateNotes(listState.notes)
            }
        }
    }

}