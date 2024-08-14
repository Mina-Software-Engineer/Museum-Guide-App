package com.example.musuemguide.userSection.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.musuemguide.application.MuseumGuideApp
import com.example.musuemguide.localStorage.local.ArtifactModel
import com.example.musuemguide.localStorage.local.asArtifactModel
import com.example.musuemguide.userSection.ui.HomeFragmentDirections
import com.example.musuemguide.utils.NavigationCommand
import kotlinx.coroutines.launch

class HomeViewModel(app: Application) :
    BaseViewModel(app, (app as MuseumGuideApp).repository) {

    private val _artifacts = MutableLiveData<List<ArtifactModel>>()
    val artifact: LiveData<List<ArtifactModel>> = _artifacts

    fun getAllArtifacts() {
        try {
            viewModelScope.launch {
                _artifacts.value = localRepo.getAllArtifacts().asArtifactModel()
            }
        } catch (e: Exception) {
            //Log.d("checkkkkk", e.message.toString())
        }
    }

    fun onArtifactItemClicked(artifact: ArtifactModel) {
        navigationCommand.value = NavigationCommand.To(
            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(artifact)
        )
    }
}
