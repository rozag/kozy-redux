<p align="center">
  <img src="https://github.com/rozag/kozy-redux/blob/master/logo/logo-kozy-redux.png" width="384">
</p>

<p align="center">
  <a href="https://bintray.com/rozag/maven/kozy-redux-core/_latestVersion">
        <img 
             src="https://img.shields.io/badge/kozy--redux--core-0.43-brightgreen.svg" 
             alt="kozy-redux-core">
  </a>
  <a href="https://bintray.com/rozag/maven/kozy-redux-base/_latestVersion">
        <img 
             src="https://img.shields.io/badge/kozy--redux--base-0.43-brightgreen.svg" 
             alt="kozy-redux-base">
  </a>
  <a href="https://twitter.com/intent/tweet?text=Pure%20Kotlin%20Redux%20library%20for%20Android&url=https://github.com/rozag/kozy-redux&via=alexey_mileev&hashtags=Kotlin,Redux,AndroidDev">
        <img 
             src="https://img.shields.io/twitter/url/http/shields.io.svg?style=social" 
             alt="Tweet">
  </a>
</p>

Pure Kotlin redux library for Android. This library is being developed with several key ideas:
* It should be tiny
* The code should be as simple as possible
* It should be relatively easy to adapt the library to your use case. Don’t hesitate to [file an issue](https://github.com/rozag/kozy-redux/issues/new) if you hit any blocker


## Table of contents

* [Quick start](https://github.com/rozag/kozy-redux#quick-start)
* [Middleware](https://github.com/rozag/kozy-redux#middleware)
* [Action creators - handling async stuff](https://github.com/rozag/kozy-redux#action-creators---handling-async-stuff)
* [Buffered store](https://github.com/rozag/kozy-redux#buffered-store)
* [What's inside](https://github.com/rozag/kozy-redux#whats-inside)
* [Samples](https://github.com/rozag/kozy-redux#samples)
* [TODO](https://github.com/rozag/kozy-redux#todo)
* [Additional reading](https://github.com/rozag/kozy-redux#additional-reading)
* [Contributing](https://github.com/rozag/kozy-redux#contributing)
* [License](https://github.com/rozag/kozy-redux#license)
* [Acknowledgements](https://github.com/rozag/kozy-redux#acknowledgements)


## Quick start

Add the dependency to your application module's `build.gradle`.
```groovy
dependencies {
    implementation "com.github.rozag:kozy-redux-base:0.43"
}
```

Define a state class. It will keep the entire state of your app. Normally you don't mutate your state, so make it immutable. It is a good practice to define an `INITIAL` state.
```kotlin
data class MyState(val number: Int) : ReduxState {
    companion object {
        val INITIAL: MyState = MyState(number = 0)
    }
}
```

Define an action sealed class. Sealed classes make routing actions to reducers a breeze because the `else` branch in the `when` expression can be dropped (with sealed classes the Kotlin compiler can prove that all cases have been handled) - see the root reducer sample below for details.
```kotlin
sealed class MyAction : ReduxAction {
    class SetUp : MyAction()
    class TearDown : MyAction()
    sealed class Feed : MyAction() {
        // Feed actions classes go here
    }
    sealed class Profile : MyAction() {
        // Profile actions classes go here
    }
}
```

Define a root reducer function. Reducer is a pure function that takes the previous state and an action and returns a new state. Your root reducer is like a router that routes different actions and different state parts to different reducers. Kotlin's `when` expression is your friend here. Note that your child reducers can accept a tiny piece of the state tree - they don't usually need the whole app state. The `SetUp` action in the snippet below is used to inflate your initial state. In this simple tutorial we don't need it, but you can use this pattern in your apps. Same with the `TearDown` action - we don't want links to our objects after the app is closed. You can use those actions to populate unneeded state fields with default values.
```kotlin
fun rootReducer(state: MyState, action: MyAction): MyState = when (action) {
    is MyAction.SetUp -> MyState.INITIAL
    is MyAction.TearDown -> MyState.INITIAL
    is MyAction.Feed -> MyState(feedReducer(state.number, action))
    is MyAction.Profile -> MyState(profileReducer(state.number, action))
}
fun feedReducer(number: Int, action: MyAction.Feed): Int { ... }
fun profileReducer(number: Int, action: MyAction.Profile): Int { ... }
```

Create a store object. You can place it wherever you want - inside your `Application` class, for instance. You can also provide your store to other components via any DI framework. But remember: **there should be only one instance of the store in your app**.
```kotlin
typealias MyStore = ReduxSubscribableStore<MyState, MyAction>
class MyApplication : Application() {
    companion object {
        val Store: MyStore = SubscribableStore(MyState.INITIAL, ::rootReducer)
        /* 
         * You can also use the 
         *                      BufferedSubscribableStore(
         *                              MyState.INITIAL, 
         *                              ::rootReducer, 
         *                              MY_BUFFER_SIZE_LIMIT,
         *                              MY_INITIAL_BUFFER_SIZE
         *                      ) 
         * constructor to create a state buffer backed store for the time travel stuff
         */
    }
    override fun onCreate() {
        super.onCreate()
        store.dispatch(CounterAction.SetUp())
    }
}
```

And now you're ready to go. Dispatch your actions to the store via `store.dispatch(...)` - your reducer will handle the action and return a new state. Subscribe to state updates in your classes via `store.subscribe(...)`. The returned `Subscription` object allows you to unsubscribe from state updates. In `Activity` you can do it like this:
```kotlin
class MyActivity : AppCompatActivity(), ReduxSubscribableStore.Subscriber<MyState> {
    private val store: MyStore = MyApplication.Store
    private lateinit var subscription: ReduxSubscribableStore.Subscription
    override fun onStart() {
        super.onStart()
        subscription = store.subscribe(this)
    }
    override fun onStop() {
        super.onStop()
        subscription.cancel()
    }
    override fun onNewState(state: MyState) {
        // Handle the new state
    }
}
```


## Middleware

If you want to react to an action dispatch (logging, analytics, etc.) you can use middleware. In `kozy-redux` it is implemented as an abstract `ReduxMiddleware` class. Under the hood it simply wraps the `store.dispatch(...)` method. To add a new middleware to your app you should extend the `ReduxMiddleware` class, implement `doBeforeDispatch(store, action)` and `doAfterDispatch(store, action)` methods and apply your middleware to your store via the `store.applyMiddleware(vararg middlewareList)` method. For example, you can implement a middleware which will log every action and every new store state like this:
```kotlin
class LoggingMiddleware : ReduxMiddleware<ReduxState, ReduxAction, ReduxStore<ReduxState, ReduxAction>>() {
    override fun doBeforeDispatch(store: ReduxStore<ReduxState, ReduxAction>, action: ReduxAction) {
        Log.d("LoggingMiddleware", "Dispatching action: $action")
    }
 
    override fun doAfterDispatch(store: ReduxStore<ReduxState, ReduxAction>, action: ReduxAction) {
        Log.d("LoggingMiddleware", "New state: ${store.getState()}")
    }
}
// And apply it to your store
store.applyMiddleware(LoggingMiddleware())
```

*Small tip: if you want to build the analytics middleware, it would be great to create your action hierarchy in such a way that you don't need any `analytics.sendEvent(...)` statements anywhere except your analytics middleware.*


## Action creators - handling async stuff

One interesting question is "Where should I put the asynchronous code?". The whole idea of reducers is that they should be pure functions - functions without any side effects or dependencies. The answer is action creators. Action creator is a function or a class that can create and dispatch actions to the store. `kozy-redux` doesn't provide any classes or interfaces for such entities - you're free to create them the way you like.

Let's say we want to perform a database operation and show a progress bar while the operation is running. The common way to handle such case is to create an action for this:
```kotlin
sealed class WriteSmthToDb : MyAction() {
    class Started : WriteSmthToDb()
    data class Success(val result: Result) : WriteSmthToDb()
    data class Error(val error: Error) : WriteSmthToDb
}
```

Now we want to perform an operation. First of all, you invoke your action creator (let it be `WriteSmthToDbActionCreator`). The action creator dispatches the `WriteSmthToDb.Started` action to the store and starts the async operation. Your reducer returns a new state. Your subscriber view receives the state with the flag that the progress bar should be visible and updates UI. When the operation finishes your action creator dispatches either `WriteSmthToDb.Success` or `WriteSmthToDb.Error` action to the store and your UI updates according to the new state.
```kotlin
class WriteSmthToDbActionCreator(val store: MyStore, val db: MyDatabase) {
    fun createAndDispatch(someData: String) {
        // The progress bar should be shown after dispatching this action
        store.dispatch(MyAction.WriteSmthToDb.Started())

        // This method is async, one of callbacks will be invoked later
        db.writeSmth(
                someData,
                { result -> store.dispatch(MyAction.WriteSmthToDb.Success(result)) },
                { error -> store.dispatch(MyAction.WriteSmthToDb.Error(error)) }
        )
    }
}
```


## Buffered store

In some apps we need an undo-like functionality. Buffered store is a way to handle this kind of tasks. In `kozy-redux` buffered store looks like a `ReduxBufferedStore` [interface](https://github.com/rozag/kozy-redux/blob/master/libcore/src/main/kotlin/com/github/rozag/redux/core/ReduxBufferedStore.kt) and an implementation for it - a `BufferedSubscribableStore` [class](https://github.com/rozag/kozy-redux/blob/master/libbase/src/main/kotlin/com/github/rozag/redux/base/BufferedSubscribableStore.kt). The interface looks as following:
```kotlin
interface ReduxBufferedStore<S : ReduxState, A : ReduxAction> : ReduxStore<S, A> {
    fun bufferSizeLimit(): Int
    fun changeSizeLimit(newSizeLimit: Int)
    fun currentBufferSize(): Int
    fun currentBufferPosition(): Int
    fun resetBuffer(initialState: S)
    fun buffer(): List<ReduxState>
    fun jumpToState(position: Int)
    fun resetToState(position: Int)
}
```

Some of these methods are useful for testing, debugging or building a log when an error occurs. Let's take a look at the primary ones:
* `resetBuffer(initialState: S)` clears the buffer, puts the `initialState` into it and notifies the subscribers. You can use this method with the `TearDown` action pattern, for example
* `jumpToState(position: Int)` changes the state buffer pointer position to the specified index and notifies the subscribers
* `resetToState(position: Int)` - same as `jumpToState` but the part of the buffer with `index > position` is removed


## What's inside

Dependency | Description
---------- | -----------
[kozy-redux-core](https://github.com/rozag/kozy-redux/tree/master/libcore) | Core interfaces. Usually you don't use this dependency, however it's useful for future library development.
[kozy-redux-base](https://github.com/rozag/kozy-redux/tree/master/libbase) | This one is built on top of the `kozy-redux-core` and provides a subscribable store interface and 2 store implementations: a subscribable store and a buffered subscribable store.


## Samples

Sample | Description
------ | -----------
[sample-counter](https://github.com/rozag/kozy-redux/tree/master/sample-counter) | Simple counter app: press operation button - see the result on the screen. Kind of hello world in a Redux world.
[sample-counter-buffered](https://github.com/rozag/kozy-redux/tree/master/sample-counter-buffered) | Same as the `sample-counter`, but now the buffered store is used - time travel within your state buffer with a slider.


## TODO

* Add more complex sample project
* Add best practices and tips to the wiki
* kozy-redux-rx - store and buffered store with [RxJava](https://github.com/ReactiveX/RxJava) subscriptions


## Additional reading
* [Redux docs](https://redux.js.org)
* [A cartoon guide to Flux *by Lin Clark*](https://code-cartoons.com/a-cartoon-guide-to-flux-6157355ab207)
* [A cartoon intro to Redux *by Lin Clark*](https://code-cartoons.com/a-cartoon-intro-to-redux-3afb775501a6)
* [Thunks in Redux: The Basics *by Gabriel Lebec*](https://medium.com/fullstack-academy/thunks-in-redux-the-basics-85e538a3fe60)


## Contributing
If you find a bug - [create an issue](https://github.com/rozag/kozy-redux/issues/new). It's your contribution. And PRs are always welcome.


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


## Acknowledgements

* [Redux](https://github.com/reactjs/redux) - Predictable state container for JavaScript apps.
