package com.github.rozag.kueue

internal typealias OnComplete<T> = (T) -> Unit

internal typealias OnError = (throwable: Throwable) -> Unit