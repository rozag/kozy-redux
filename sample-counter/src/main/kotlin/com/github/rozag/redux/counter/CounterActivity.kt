package com.github.rozag.redux.counter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.github.rozag.redux.base.ReduxSubscribableStore

class CounterActivity : AppCompatActivity(), ReduxSubscribableStore.Subscriber<CounterState> {

    private val store: CounterStore = App.store

    private lateinit var counterTextView: TextView
    private lateinit var addOneButton: Button
    private lateinit var addTenButton: Button
    private lateinit var subtractOneButton: Button
    private lateinit var subtractTenButton: Button

    private lateinit var connection: ReduxSubscribableStore.Connection

    private var isExiting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        counterTextView = findViewById(R.id.counter_tv)

        addOneButton = findViewById(R.id.add_one_btn)
        addOneButton.setOnClickListener { store.dispatch(CounterAction.Add.One()) }

        addTenButton = findViewById(R.id.add_ten_btn)
        addTenButton.setOnClickListener { store.dispatch(CounterAction.Add.Ten()) }

        subtractOneButton = findViewById(R.id.subtract_one_btn)
        subtractOneButton.setOnClickListener { store.dispatch(CounterAction.Subtract.One()) }

        subtractTenButton = findViewById(R.id.subtract_ten_btn)
        subtractTenButton.setOnClickListener { store.dispatch(CounterAction.Subtract.Ten()) }
    }

    override fun onStart() {
        super.onStart()
        connection = store.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        connection.unsubscribe()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        isExiting = true
        store.dispatch(CounterAction.TearDown())
    }

    override fun onNewState(state: CounterState) {
        if (isExiting) {
            return
        }
        counterTextView.text = state.count.toString()
    }

}