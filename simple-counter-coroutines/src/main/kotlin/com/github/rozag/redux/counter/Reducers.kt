package com.github.rozag.redux.counter

fun rootReducer(state: CounterState, action: CounterAction): CounterState {
    Thread.sleep(1000) // TODO: remove
    return when (action) {
        is CounterAction.Init -> CounterState.initialState
        is CounterAction.Add.One -> addOneReducer(state, action)
        is CounterAction.Add.Ten -> addTenReducer(state, action)
        is CounterAction.Subtract.One -> subtractOneReducer(state, action)
        is CounterAction.Subtract.Ten -> subtractTenReducer(state, action)
    }
}

fun addOneReducer(state: CounterState, action: CounterAction.Add.One): CounterState = CounterState(state.count + 1)

fun addTenReducer(state: CounterState, action: CounterAction.Add.Ten): CounterState = CounterState(state.count + 10)

fun subtractOneReducer(state: CounterState, action: CounterAction.Subtract.One): CounterState = CounterState(state.count - 1)

fun subtractTenReducer(state: CounterState, action: CounterAction.Subtract.Ten): CounterState = CounterState(state.count - 10)