<p align="center">
  <img src="https://github.com/rozag/kozy-redux/blob/master/logo/logo-kozy-redux.png" width="384">
</p>

<p align="center">
  <a href="https://bintray.com/rozag/maven/kozy-redux-core/_latestVersion">
        <img 
             src="https://img.shields.io/badge/kozy--redux--core-0.42-brightgreen.svg" 
             alt="kozy-redux-core">
  </a>
  <a href="https://bintray.com/rozag/maven/kozy-redux-base/_latestVersion">
        <img 
             src="https://img.shields.io/badge/kozy--redux--base-0.42-brightgreen.svg" 
             alt="kozy-redux-base">
  </a>
  <a href="https://twitter.com/intent/tweet?text=Pure%20Kotlin%20Redux%20library%20for%20Android&url=https://github.com/rozag/kozy-redux&via=alexey_mileev&hashtags=Kotlin,Redux,AndroidDev">
        <img 
             src="https://img.shields.io/twitter/url/http/shields.io.svg?style=social" 
             alt="Tweet">
  </a>
</p>

Pure Kotlin redux library. This library is being developed with several key ideas:
* It should be tiny
* The code should be as simple as possible
* If I need to hack, don't bother me
* It should help me to develop Redux-arhictectured Android apps


## Table of contents

* [Quick start](https://github.com/rozag/kozy-redux#quick-start)
* [Middleware](https://github.com/rozag/kozy-redux#middleware)
* [Action creators - handling async stuff](https://github.com/rozag/kozy-redux#action-creators-handling-async-stuff)
* [Buffered store](https://github.com/rozag/kozy-redux#buffered-store)
* [What's inside](https://github.com/rozag/kozy-redux#whats-inside)
* [Samples](https://github.com/rozag/kozy-redux#samples)
* [TODO](https://github.com/rozag/kozy-redux#todo)
* [Contributing](https://github.com/rozag/kozy-redux#contributing)
* [License](https://github.com/rozag/kozy-redux#license)
* [Acknowledgements](https://github.com/rozag/kozy-redux#acknowledgements)


## Quick start

Add the repository to your root `build.gradle` (this step will be removed after linking to jCenter).
```groovy
allprojects {
    repositories {
        jcenter()
        google()
        maven { url = "https://dl.bintray.com/rozag/maven" }
    }
}
```

Add the dependency to your application module's `build.gradle`.
```groovy
dependencies {
    implementation "com.github.rozag:kozy-redux-base:0.42"
}
```

Define your state class. This one will keep the whole state of your app. Normally you don't mutate your state, so make it immutable. Defining an `INITIAL` state is a good practice - your app starts with this empty state.
```kotlin
data class MyState(val number: Int) : ReduxState {
    companion object {
        val INITIAL: MyState = MyState(number = 0)
    }
}
```

Define your action sealed class. Kotlin sealed classes make it pleasure to route different actions to different reducers.
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

Define your root reducer function. Reducer is a pure function that takes the previous state and an action and returns a new state. Your root reducer is like a router that routes different actions and different state parts to different reducers. Kotlin's `when` expression is your friend here. Note that your child reducers can accept a tiny piece of the state tree - they don't usually need the whole app state. The `SetUp` action in the snippet below is used to inflate your initial state. In this simple tutorial we don't need it, but you can use this pattern in your apps. Same with the `TearDown` action - we don't want links to our objects after the app is closed. You can use such action to fill the unneeded state fields with some empty stuff.
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

Create your store object. You can place it wherever you want - inside your `Application` class, for instance. You can also provide your store to other components via any DI framework. But remember: **there should be only one instance of the store in your app**.
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

And now you're ready to go. Dispatch your actions to the store via the `store.dispatch(...)` - your reducer will handle the action and return the new state. Subscribe on state updates in your classes via the `store.subscribe(...)`. The returned `Subscription` object allows you to unsubscribe from state updates. In `Activity` you can do it like this:
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
TBD


## Action creators - handling async stuff
TBD


## Buffered store
TBD


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

* Remove maven url instructions from the [Quick start](https://github.com/rozag/kozy-redux#quick-start) when linked to jCenter
* Add more complex sample project
* Add wiki with best practices and tips
* kozy-redux-rx - store and buffered store with [RxJava](https://github.com/ReactiveX/RxJava) subscriptions


## Contributing
If you find a bug - [create an issue](https://github.com/rozag/kozy-redux/issues/new). It's your contribution. And PRs are always welcome.


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.


## Acknowledgements

* [Redux](https://github.com/reactjs/redux) - Predictable state container for JavaScript apps.
