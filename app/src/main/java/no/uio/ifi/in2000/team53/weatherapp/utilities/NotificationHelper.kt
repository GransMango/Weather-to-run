package no.uio.ifi.in2000.team53.weatherapp.utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import no.uio.ifi.in2000.team53.weatherapp.MainActivity
import no.uio.ifi.in2000.team53.weatherapp.R

/**
 * Helper class for showing notifications.
 */
object NotificationHelper {

    private const val CHANNEL_ID = "weather_notification_channel"

    /**
     * Creates a notification channel.
     *
     * @param context The context.
     */
    fun createNotificationChannel(context: Context) {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        } else {
            // Permission not granted, handle according to your app requirement
            // Possibly show a dialog explaining why the permission is needed and prompt for it
        }
    }

    /**
     * Shows a notification.
     *
     * @param context The context.
     * @param title The title of the notification.
     * @param message The message of the notification.
     */
    fun showNotification(context: Context, title: String, message: String) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // Create an Intent for the activity you want to start
            val resultIntent = Intent(context, MainActivity::class.java)
            // Create the PendingIntent
            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(resultIntent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_weather_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent) // Set the PendingIntent into the notification
                .setAutoCancel(true) // Notification will disappear after tap on it

            with(NotificationManagerCompat.from(context)) {
                notify(System.currentTimeMillis().toInt(), builder.build())
            }
        }
    }
}
