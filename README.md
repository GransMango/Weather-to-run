# Team 53
We've decided to utilize KDoc as documentation for our application.
Seeing as the documentation can generate html files, we decided to host a website showcasing the documentation: https://in2000-team53-documentation.azurewebsites.net/
In case the website doesn't work or you want to view the files locally, we've included them in the docs folder.

# How to run our app
Running our app should be fairly simple, you first need to build the project, which Android Studio will do for you when you run the application.
We utilized environment variables in our code to ensure that we don't expose secrets, however as we understand, we need to expose them to allow the grader to run the application.

# Our API's
We've created our own API's for this application, this to seperate some of the concerns for the application.

## Pollen API
The pollen API is essentially a proxy for the NAAF pollenvarsel.
Seeing as NAAF's pollenvarsel only takes in a regionID as parameter, we needed to make a Flask application which maps a latitude and longitude location to a municipality. 
This then maps those muncipalities to NAAFs own defined pollen regions.

 - Pollenvarsel API Website - https://in2000-reverseregionlookup.azurewebsites.net/
 - Source code - https://github.com/GransMango/IN2000-regionAndPollen

## Activity API
We also decided to create an Activity API which fetches remote activities. 
This was essential to ensure that our application can dynamically add and update existing activities.
By utilizing an SQL database and exposing an API to the application, we don't have to deploy our application if we need to change an activity.

 - Activity API Website - https://in2000-weather-sqlapi.azurewebsites.net/
 - Source code - https://github.com/GransMango/in2000-sql-api

# Overview of our Dependencies/Libraries

## AndroidX Libraries

- Core KTX
    - androidx.core:core-ktx:1.13.1
    Kotlin extensions for Android Jetpack and platform APIs, making them more idiomatic to Kotlin.

- Lifecycle Runtime KTX
    - androidx.lifecycle:lifecycle-runtime-ktx:2.7.0
    Manages lifecycles of Android components and performs actions in response to lifecycle changes.

- Activity Compose
    - androidx.activity:activity-compose:1.9.0
    Integration between the activity lifecycle and Jetpack Compose.

- Compose Libraries
    - androidx.compose.ui:ui
    - androidx.compose.ui:ui-graphics
    - androidx.compose.ui:ui-tooling-preview
    - androidx.compose.material3:material3
    - androidx.compose.material:material:1.6.7
    Jetpack Compose libraries for building UI components declaratively.

- Navigation Compose
    - androidx.navigation:navigation-compose:2.7.7
    Integration for navigation components in Jetpack Compose.

- AppCompat
    - androidx.appcompat:appcompat:1.6.1
    Support for using newer Android components on older versions of the platform.

DataStore
    - androidx.datastore:datastore-preferences:1.1.1
    Provides a data storage solution for key-value pairs or typed objects with protocol buffers. Used to ensure Setup is shown on first launch.

Room Database
    - androidx.room:room-runtime:2.6.1
    - androidx.room:room-ktx:2.6.1
    Abstraction layer over SQLite for fluent database access. Used to store users chosen activities and profile.

## Third-Party and More Unusual Libraries

- Ktor
    - io.ktor:ktor-client-core:2.3.8
    - io.ktor:ktor-client-json:2.3.8
    - io.ktor:ktor-client-serialization:2.3.8
    - io.ktor:ktor-client-android:2.3.8
    - io.ktor:ktor-serialization-kotlinx-json:2.3.8
    - io.ktor:ktor-client-content-negotiation:2.3.8
    Ktor is a framework for building asynchronous servers and clients in connected systems using the Kotlin programming language.
    Used in conjunction with API.

- Coil
    - io.coil-kt:coil-compose:2.5.0
    - io.coil-kt:coil-gif:2.5.0
    An image loading library for Android backed by Kotlin Coroutines. It is fast, lightweight, and easy to use.
    Used to get a slick splash screen.

- Kotlin Coroutines
    - org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3
    - org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3
    Libraries for using Kotlin coroutines to simplify asynchronous programming on Android.
    Used to manage threads.

- Google Play Services Location
    - com.google.android.gms:play-services-location:21.2.0
    Provides APIs to access device location services, allowing apps to request the last known location and subscribe to location updates.
    Used to fetch users location for use in API's.

- Dagger Hilt
    - com.google.dagger:hilt-android:2.51
    - com.google.dagger:hilt-android-compiler:2.51
    - androidx.hilt:hilt-navigation-compose:1.2.0
    A dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project.
    Used to help us maintain SingleTon pattern.

- Lottie
    - com.airbnb.android:lottie-compose:6.4.0
    A library for parsing Adobe After Effects animations exported as JSON with Bodymovin and rendering them natively on mobile and on the web.
    This was heavily used for theming our application

- OSMDroid
    - org.osmdroid:osmdroid-android:6.1.10
    - org.osmdroid:osmdroid-wms:6.1.10
    An alternative to Google Maps API for Android. OSMDroid is a library for displaying and manipulating OpenStreetMap data in Android apps.
    This was used to get location from users who doesn't want to enable Location Services.

## Testing Libraries

- JUnit
    - junit:junit:4.13.2
    A simple framework to write repeatable tests. It is an instance of the xUnit architecture for unit testing frameworks.
    Used in both Unit and Integration Testing

- Mockk
    - io.mockk:mockk-android:1.13.9
    - io.mockk:mockk-agent:1.13.9
    A mocking library for Kotlin used for unit testing. Used to mockk mainly ViewModels and Repositories in Integration and Unit testing.

- Espresso
    - androidx.test.espresso:espresso-core:3.5.1
    Provides APIs for writing UI tests to simulate user interactions.
    Used to attempt to write UI tests.

- Kotlin Coroutines Test
    - org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3
    Provides testing utilities for Kotlin coroutines.
    Used extensively in Integration tests.

- Robolectric
    - org.robolectric:robolectric:4.7.3
    Allows Android applications to be tested on the JVM without an emulator.
    Nice to utilize logging in testing.

- Compose Testing
    - androidx.compose.ui:ui-test-junit4
    Provides testing utilities for Jetpack Compose UI components.
