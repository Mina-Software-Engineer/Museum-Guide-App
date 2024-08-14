package com.example.musuemguide.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.Locale

object LanguageManager {
    private const val PREF_LANGUAGE_KEY = "PREF_LANGUAGE_KEY"

    fun saveLanguagePreference(context: Context, language: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "MyPrefs",
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(PREF_LANGUAGE_KEY, language)
        editor.apply()
    }

    fun loadLanguagePreference(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "MyPrefs",
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(PREF_LANGUAGE_KEY, null)
    }

    fun setLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}