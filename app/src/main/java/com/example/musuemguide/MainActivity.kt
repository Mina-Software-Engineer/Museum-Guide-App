package com.example.musuemguide

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.musuemguide.databinding.ActivityMainBinding
import com.example.musuemguide.userSection.ui.SettingsFragment
import com.example.musuemguide.utils.LanguageManager


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        sharedPreferences = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

        //installSplashScreen()
        setContentView(binding.root)

        val currentTheme = sharedPreferences.getString(
            SettingsFragment.PREF_THEME_KEY,
            SettingsFragment.THEME_LIGHT
        )

        changeTheme(currentTheme == SettingsFragment.THEME_DARK)

        val preferredLanguage = LanguageManager.loadLanguagePreference(this)
        //Log.d("Language", "Language is $preferredLanguage")
        preferredLanguage?.let {
            LanguageManager.setLocale(this, it)
        }

        binding.bottomNavigation.setupWithNavController(
            findNavController(R.id.fragmentContainerView)
        )
    }

    private fun changeTheme(darkTheme: Boolean) {
        val theme = if (darkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            SettingsFragment.THEME_DARK
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            SettingsFragment.THEME_LIGHT
        }
        sharedPreferences.edit().putString(SettingsFragment.PREF_THEME_KEY, theme).apply()
    }

}
