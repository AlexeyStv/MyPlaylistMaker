package com.practicum.android.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    val THEME_KEY = "key_theme"
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val shPrefs = getSharedPreferences(PM_PREFERENCE, MODE_PRIVATE)
        darkTheme = shPrefs.getBoolean(THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}