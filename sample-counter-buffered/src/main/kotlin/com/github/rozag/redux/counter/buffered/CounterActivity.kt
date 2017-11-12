package com.github.rozag.redux.counter.buffered

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.github.rozag.redux.base.ReduxBufferedSubscribableStore
import com.github.rozag.redux.base.ReduxSubscribableStore

class CounterActivity : AppCompatActivity(), ReduxSubscribableStore.Subscriber<CounterState> {

    private val store: ReduxBufferedSubscribableStore<CounterState, CounterAction> = App.store

    private lateinit var counterTextView: TextView
    private lateinit var addOneButton: Button
    private lateinit var addTenButton: Button
    private lateinit var subtractOneButton: Button
    private lateinit var subtractTenButton: Button
    private lateinit var stateNumberTextView: TextView
    private lateinit var stateNumberSeekBar: SeekBar

    private lateinit var connection: ReduxSubscribableStore.Connection

    private var isExiting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        counterTextView = findViewById(R.id.counter_tv)

        addOneButton = findViewById(R.id.add_one_btn)
        addOneButton.setOnClickListener { store.dispatch(CounterAction.Operation.Add.One()) }

        addTenButton = findViewById(R.id.add_ten_btn)
        addTenButton.setOnClickListener { store.dispatch(CounterAction.Operation.Add.Ten()) }

        subtractOneButton = findViewById(R.id.subtract_one_btn)
        subtractOneButton.setOnClickListener { store.dispatch(CounterAction.Operation.Subtract.One()) }

        subtractTenButton = findViewById(R.id.subtract_ten_btn)
        subtractTenButton.setOnClickListener { store.dispatch(CounterAction.Operation.Subtract.Ten()) }

        stateNumberTextView = findViewById(R.id.state_number_tv)

        stateNumberSeekBar = findViewById(R.id.state_number_sb)
        stateNumberSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    store.jumpToState(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
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

    @SuppressLint("SetTextI18n")
    override fun onNewState(state: CounterState) {
        if (isExiting) {
            return
        }

        counterTextView.text = state.count.toString()

        stateNumberTextView.text = "${store.currentBufferPosition() + 1}/${store.currentBufferSize()}"
        stateNumberSeekBar.max = store.currentBufferSize() - 1
        stateNumberSeekBar.progress = store.currentBufferPosition()
    }

}