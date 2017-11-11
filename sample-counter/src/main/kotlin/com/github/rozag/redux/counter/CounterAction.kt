package com.github.rozag.redux.counter

import com.github.rozag.redux.core.Action

sealed class CounterAction : Action {
    class Init: CounterAction()
    sealed class Add: CounterAction() {
        class One: Add()
        class Ten: Add()
    }
    sealed class Subtract : CounterAction() {
        class One: Subtract()
        class Ten: Subtract()
    }
}