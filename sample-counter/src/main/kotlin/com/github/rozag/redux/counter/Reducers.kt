package com.github.rozag.redux.counter

fun rootReducer(state: CounterState, action: CounterAction): CounterState = when (action) {
    is CounterAction.SetUp -> CounterState.Initial
    is CounterAction.Add -> when (action) {
        is CounterAction.Add.One -> addOneReducer(state, action)
        is CounterAction.Add.Ten -> addTenReducer(state, action)
    }
    is CounterAction.Subtract -> when (action) {
        is CounterAction.Subtract.One -> subtractOneReducer(state, action)
        is CounterAction.Subtract.Ten -> subtractTenReducer(state, action)
    }
    is CounterAction.TearDown -> CounterState.Initial
}

fun addOneReducer(state: CounterState, action: CounterAction.Add.One): CounterState = CounterState(state.count + 1)

fun addTenReducer(state: CounterState, action: CounterAction.Add.Ten): CounterState = CounterState(state.count + 10)

fun subtractOneReducer(state: CounterState, action: CounterAction.Subtract.One): CounterState = CounterState(state.count - 1)

fun subtractTenReducer(state: CounterState, action: CounterAction.Subtract.Ten): CounterState = CounterState(state.count - 10)