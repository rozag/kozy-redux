package com.github.rozag.redux.counter.buffered

fun rootReducer(state: CounterState, action: CounterAction): CounterState = when (action) {
    is CounterAction.Operation -> {
        val count = operationReducer(state.count, action)
        val (currentStateIndex, totalStateCount) = storeNumbersReducer(state, action)
        CounterState(
                count,
                currentStateIndex,
                totalStateCount,
                state.bufferSizeLimit
        )
    }
    is CounterAction.TimeTravel -> CounterState(
            state.count,
            action.stateIndex,
            state.totalStateCount,
            state.bufferSizeLimit
    )
    is CounterAction.TearDown -> CounterState.initial(App.BUFFER_SIZE_LIMIT)
}

fun storeNumbersReducer(state: CounterState, action: CounterAction.Operation): Pair<Int, Int> {
    val totalStateCount: Int = if (state.totalStateCount == state.bufferSizeLimit) {
        state.totalStateCount
    } else {
        state.totalStateCount + 1
    }
    val currentStateIndex: Int = state.totalStateCount - 1
    return Pair(currentStateIndex, totalStateCount)
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