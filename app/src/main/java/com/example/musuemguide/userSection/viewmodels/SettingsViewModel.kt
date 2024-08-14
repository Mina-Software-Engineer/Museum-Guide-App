package com.example.musuemguide.userSection.viewmodels

import android.app.Application
import com.example.musuemguide.application.MuseumGuideApp

class SettingsViewModel(app: Application) : BaseViewModel(app, (app as MuseumGuideApp).repository,){


    /*    private val _lang = MutableLiveData<LanguageDTO>()
        val lang : LiveData<LanguageDTO> = _lang*/


    /*fun setLanguage(langCode: String) {
        viewModelScope.launch {
            localRepo.setAppLanguage(LanguageDTO(langCode = langCode))
        }
    }*/

    /* fun getLanguage() {
         _lang.value = localRepo.getAppLanguage().value
     }*/

    fun showSnackBarTxt(txt: String) {
        showSnackBar.value = txt
    }


}