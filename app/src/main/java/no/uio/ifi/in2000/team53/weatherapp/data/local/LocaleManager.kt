package no.uio.ifi.in2000.team53.weatherapp.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.util.Log
import java.util.*

/**
 * A class for managing the locale of the application.
 */
object LocaleManager {

    /**
     * Sets the locale of the application to the specified language.
     *
     * @param context The context of the application.
     * @param language The language to set the locale to.
     */
    @SuppressLint("ObsoleteSdkInt")
    @Suppress("DEPRECATION")
    fun setLocale(context: Context, language: String) {
        val shortLanguage = if (language == "English") "en" else "nb"
        Log.d(language,"LocaleManager received $language")
        val locale = Locale(shortLanguage)
        Locale.setDefault(locale)
        Log.d("Locale", "Default locale set to: ${Locale.getDefault().language}")

        val resources = context.resources
        val configuration = resources.configuration

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
        } else {
            configuration.setLocale(locale)
        }

        resources.updateConfiguration(configuration, resources.displayMetrics)
        Log.d(locale.language, "Configuration updated with locale: ${configuration.locale.language}")

    }
}