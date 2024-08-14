package com.example.musuemguide.userSection.viewmodels

import android.app.Application
import com.example.musuemguide.application.MuseumGuideApp

class DetailsViewModel(app: Application) :
    BaseViewModel(app, (app as MuseumGuideApp).repository) {
}