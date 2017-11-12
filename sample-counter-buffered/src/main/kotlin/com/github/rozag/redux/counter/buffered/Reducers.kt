package com.github.rozag.redux.counter.buffered

fun rootReducer(state: CounterState, action: CounterAction): CounterState = when (action) {
    is CounterAction.Operation -> CounterState(operationReducer(state.count, action))
    is CounterAction.TearDown -> CounterState.INITIAL
}

fun operationReducer(count: Int, action: CounterAction.Operation): Int = when (action) {
    is CounterAction.Operation.Add -> additionReducer(count, action)
    is CounterAction.Operation.Subtract -> subtractionReducer(count, action)
}

fun additionReducer(count: Int, action: CounterAction.Operation.Add): Int = when (action) {
    is CounterAction.Operation.Add.One -> count + 1
    is CounterAction.Operation.Add.Ten -> count + 10
}

fun subtractionReducer(count: Int, action: CounterAction.Operation.Subtract): Int = when (action) {
    is CounterAction.Operation.Subtract.One -> count - 1
    is CounterAction.Operation.Subtract.Ten -> count - 10
}