package com.github.rozag.redux.notes.middleware

import com.github.rozag.kueue.Kueue
import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.store.ReduxStore
import com.github.rozag.redux.notes.Action
import com.github.rozag.redux.notes.AppState
import com.github.rozag.redux.notes.IdGenerator
import com.github.rozag.redux.notes.R
import com.github.rozag.redux.notes.model.Note
import com.github.rozag.redux.notes.repo.NotesRepo
import com.github.rozag.redux.notes.resources.ResProvider
import timber.log.Timber

class FirstLaunchMiddleware(
        private val idGenerator: IdGenerator,
        private val resProvider: ResProvider,
        private val repo: NotesRepo,
        private val taskQueue: Kueue
) : ReduxMiddleware<AppState, Action, ReduxStore<AppState, Action>>() {

    override fun doBeforeDispatch(store: ReduxStore<AppState, Action>, action: Action) = Unit

    override fun doAfterDispatch(store: ReduxStore<AppState, Action>, action: Action) {
        if (action is Action.FirstLaunch.Started) {
            taskQueue.fromCallable {
                val notes = listOf(
                        Note(
                                id = idGenerator.generateId(),
                                title = resProvider.getString(R.string.initial_note_title_0),
                                body = resProvider.getString(R.string.initial_note_body_0)
                        ),
                        Note(
                                id = idGenerator.generateId(),
                                title = resProvider.getString(R.string.initial_note_title_1),
                                body = resProvider.getString(R.string.initial_note_body_1)
                        ),
                        Note(
                                id = idGenerator.generateId(),
                                title = resProvider.getString(R.string.initial_note_title_2),
                                body = resProvider.getString(R.string.initial_note_body_2)
                        )
                )
                notes.forEach { note -> repo.addNote(note) }
                notes
            }
                    .onComplete { notes -> store.dispatch(Action.FirstLaunch.Complete(notes)) }
                    .onError { throwable -> Timber.e(throwable) }
                    .go()
        }
    }

}