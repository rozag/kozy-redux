package com.github.rozag.redux.counter

import com.github.rozag.redux.core.ReduxAction

sealed class CounterAction : ReduxAction {

    class SetUp : CounterAction()

    sealed class Add : CounterAction() {
        class One : Add()
        class Ten : Add()
    }

    sealed class Subtract : CounterAction() {
        class One : Subtract()
        class Ten : Subtract()
    }

    class TearDown : CounterAction()

}