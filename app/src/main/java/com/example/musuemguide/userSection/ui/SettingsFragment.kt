package com.example.musuemguide.userSection.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.example.musuemguide.R
import com.example.musuemguide.databinding.FragmentSettingsBinding
import com.example.musuemguide.databinding.LanguagesBottomSheetBinding
import com.example.musuemguide.userSection.viewmodels.SettingsViewModel
import com.example.musuemguide.utils.LanguageManager
import com.example.musuemguide.utils.NavigationCommand
import com.example.musuemguide.utils.setDisplayHomeAsUpEnabled
import com.example.musuemguide.utils.setTitle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.android.ext.android.inject


class SettingsFragment : BaseFragment() {

    private lateinit var standardBottomSheetBehavior: BottomSheetBehavior<View>
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding
    override val _viewModel: SettingsViewModel by inject()
    override fun startAnimation() {

    }

    private lateinit var sharedPreferences: SharedPreferences


    companion object {
        const val PREF_THEME_KEY = "pref_theme_key"
        const val THEME_LIGHT = "light_theme"
        const val THEME_DARK = "dark_theme"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater)

        setDisplayHomeAsUpEnabled(binding!!.appBarToolbar, true)
        setTitle(getString(R.string.settings))


        binding!!.appBarToolbar.navigationIcon = resources.getDrawable(R.drawable.back_btn)
        binding!!.appBarToolbar.setNavigationOnClickListener(View.OnClickListener {
            _viewModel.navigationCommand.postValue(NavigationCommand.Back)
        })

        binding!!.qrReaderContainer.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_authFragment)
        }

        binding!!.settingLanguageContainer.settingLanguageLayoutContainer.setOnClickListener { _ ->
            val preferredLanguage = LanguageManager.loadLanguagePreference(requireContext())

            if (preferredLanguage != null) {
                showBottomSheetLanguages(preferredLanguage)
            } else {
                showBottomSheetLanguages("en")
            }
        }

        val currentTheme = sharedPreferences.getString(PREF_THEME_KEY, THEME_LIGHT)
        binding!!.settingThemeContainer.themeSwitch.isChecked = currentTheme == THEME_DARK

        binding!!.settingThemeContainer.themeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            changeTheme(isChecked)
        }
        return binding!!.root
    }

    private fun changeTheme(darkTheme: Boolean) {
        val theme = if (darkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            THEME_DARK
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_LIGHT
        }
        sharedPreferences.edit().putString(PREF_THEME_KEY, theme).apply()
    }

    private fun dismissBottomSheet() {
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun showBottomSheetLanguages(preferredLanguage: String) {

        val dialog = BottomSheetDialog(requireContext())
        val view = LanguagesBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(view.root)
        dialog.setCanceledOnTouchOutside(true)
        dialog.behavior.isDraggable = true
        dialog.show()

        view.languagesRadioGroup.check(
            when (preferredLanguage) {
                "en" -> view.radioBtnEnglish.id
                "de" -> view.radioBtnGerman.id
                "ar" -> view.radioBtnArabic.id
                "fr" -> view.radioBtnFrench.id
                "hi" -> view.radioBtnHindi.id
                else -> view.radioBtnEnglish.id
            }
        )

        view.languagesRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                view.radioBtnEnglish.id -> {
                    selectLanguage("en")
                }

                view.radioBtnGerman.id -> {
                    selectLanguage("de")
                }

                view.radioBtnArabic.id -> {
                    selectLanguage("ar")
                }

                view.radioBtnFrench.id -> {
                    selectLanguage("fr")
                }

                view.radioBtnHindi.id -> {
                    selectLanguage("hi")
                }
            }
        }
    }


    private fun selectLanguage(lang: String) {
        LanguageManager.saveLanguagePreference(requireContext(), lang)
        LanguageManager.setLocale(requireContext(), lang)
        requireActivity().recreate()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            android.R.id.home -> {
                // Perform back navigation
                _viewModel.navigationCommand.postValue(
                    NavigationCommand.Back
                )
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}