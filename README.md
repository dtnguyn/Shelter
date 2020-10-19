
<h1 align="center">Shelter</h1>

<p align="center">  
Shelter is a real estate app that is used for finding rent and sale houses. It is also a community blog for peole living in the same area to connect with one another.
</p>
</br>

<p align="center">
<img src="/previews/screenshot.png" alt="Images comming soon"/>
</p>

## Try out this app
The app is almost finshed now. Once it's finished, a link to the Google Play Store will be posted.
For now, you can clone it and use Android Studio to run this app

<img src="/previews/preview.gif"  alt="Images comming soon" align="right" width="32%"/>

## Tech stack & libraries
- Minimum SDK level 21
- Written in [Kotlin](https://kotlinlang.org/) 
- Architecture
  - MVI Architecture (Model - View - Intent)
  - Repository pattern
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - A new library built on top of Dagger for dependency injection
- [Retrofit2](https://github.com/square/retrofit) - Pull data from network to build paging data
- [Room](https://developer.android.com/topic/libraries/architecture/room) - Cache data from network to optimize API calls
- [Paging 3.0](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) (alpha) - Paging system to manage data flow.
- [Gson](https://github.com/google/gson) - A JSON serialize & deserialize library
- [Glide](https://github.com/bumptech/glide) - loading images.
- UI
  - [DataBinding](https://developer.android.com/topic/libraries/data-binding) - bind UI components in layouts to data sources
  - [TransformationLayout](https://github.com/skydoves/transformationlayout) - implementing transformation motion animations.
  - [Material-Components](https://github.com/material-components/material-components-android) - Material design components like ripple animation, cardView.
  - [Navigation-Components](https://developer.android.com/guide/navigation) - A new way to navigate in Android Jetpack
  - [Image-Slider](https://github.com/smarteist/Android-Image-Slider) - Image slider (Carousel) for android.
  - [Shimmering-Loading-Effects](https://github.com/facebook/shimmer-android) - A popular loading effects used by many apps


## Architecture & Design Pattern
Shelter is based on MVI architecture with repository pattern. (MVI is a more organized version of MVVM)

![architecture](https://miro.medium.com/max/911/1*TTKpvdzyNXfPBhVyRqD6EA.png)

## API

<img src="https://www.christalks.com/wp-content/uploads/2016/08/realtorcom_logo.jpg" align="right" width="10%"/>

Shelter using the [RealtorAPI](https://rapidapi.com/apidojo/api/realtor) for constructing RESTful API.<br>
Shelter also using Firebase for building a community blog.

## Support :heart:
If you find this repo useful or you like the code that I write, leave a star ⭐

# License
```xml

Copyright 2020 dtnguyn (Adron Nguyen)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
associated documentation files (the "Software"), to deal in the Software without restriction, including 
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the 
following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial 
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS IN THE SOFTWARE.
```
