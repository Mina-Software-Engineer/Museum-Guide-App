package com.example.musuemguide.userSection.viewmodels

import android.app.Application
import com.example.musuemguide.application.MuseumGuideApp

class SettingsViewModel(app: Application) :
    BaseViewModel(app, (app as MuseumGuideApp).repository) {

    fun showSnackBarTxt(txt: String) {
        showSnackBar.value = txt
    }
}