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
* It must not be a framework: if I need to hack something, don't bother me
* It should help me to develop Redux-arhictectured Android apps


## Table of contents

* [Quick start](https://github.com/rozag/kozy-redux#quick-start)
* [What's inside](https://github.com/rozag/kozy-redux#whats-inside)
* [Samples](https://github.com/rozag/kozy-redux#samples)
* [TODO](https://github.com/rozag/kozy-redux#todo)
* [Contributing](https://github.com/rozag/kozy-redux#contributing)
* [License](https://github.com/rozag/kozy-redux#license)
* [Acknowledgements](https://github.com/rozag/kozy-redux#acknowledgements)


## Quick start
TBD


## What's inside

Dependency | Description
---------- | -----------
[kozy-redux-core](https://github.com/rozag/kozy-redux/tree/master/libcore) | Core interfaces. Usually you don't use this dependency, however it's useful for future library development.
[kozy-redux-base](https://github.com/rozag/kozy-redux/tree/master/libbase) | This one is built on top of the kozy-redux-core and provides a subscribable store interface and 2 store implementations: a subscribable store and a buffered subscribable store.


## Samples

Sample | Description
------ | -----------
[sample-counter](https://github.com/rozag/kozy-redux/tree/master/sample-counter) | Simple counter app: press operation button - see the result on the screen. Kind of hello world in a Redux world.
[sample-counter-buffered](https://github.com/rozag/kozy-redux/tree/master/sample-counter-buffered) | Same as the sample-counter, but now the buffered store is used - time travel within your state buffer with a slider.


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
