package no.uio.ifi.in2000.team53.weatherapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import no.uio.ifi.in2000.team53.weatherapp.utilities.NotificationHelper

/**
 * Application class for the app, used mainly for setting up the notification channel.
 */
@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}