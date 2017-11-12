package com.github.rozag.redux.counter.buffered

import com.github.rozag.redux.core.ReduxAction

sealed class CounterAction : ReduxAction {

    sealed class Operation : CounterAction() {
        sealed class Add : Operation() {
            class One : Add()
            class Ten : Add()
        }

        sealed class Subtract : Operation() {
            class One : Subtract()
            class Ten : Subtract()
        }
    }

//    data class TimeTravel(val stateIndex: Int) : CounterAction()

    class TearDown : CounterAction()

}