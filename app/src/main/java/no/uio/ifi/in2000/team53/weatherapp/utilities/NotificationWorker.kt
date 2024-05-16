package no.uio.ifi.in2000.team53.weatherapp.utilities

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import java.time.LocalTime
import java.time.Duration

/**
 * Worker class for showing a notification.
 *
 * @param appContext The application context.
 * @param workerParams The worker parameters.
 *
 * @return The result of the worker.
 */
class NotificationWorker(private val appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        NotificationHelper.showNotification(
            context = appContext,
            title = "Daily Activities",
            message = "Your activity suggestions are ready!"
        )
        return Result.success()
    }
}

/**
 * Schedules a daily notification at 8 AM.
 *
 * @param context The application context.
 */
fun scheduleDaily8AMNotification(context: Context) {
    val currentTime = LocalTime.now()
    val targetTime = LocalTime.of(8, 0) // 8 AM
    var initialDelay = Duration.between(currentTime, targetTime).toMinutes()
    if (initialDelay < 0) {
        // If it's already past 8 AM, schedule for the next day
        initialDelay += 24 * 60
    }

    val dailyWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(initialDelay, TimeUnit.MINUTES)
        .addTag("daily_weather_notification")
        .build()

    WorkManager.getInstance(context).enqueue(dailyWorkRequest)
}

