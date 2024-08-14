package com.example.musuemguide.userSection.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.musuemguide.localStorage.LocalRepository
import com.example.musuemguide.utils.NavigationCommand
import com.example.musuemguide.utils.SingleLiveEvent

abstract class BaseViewModel(
    val app: Application,
    val localRepo: LocalRepository,
) : AndroidViewModel(app) {

    val navigationCommand: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()
    val showSnackBar: SingleLiveEvent<String> = SingleLiveEvent()
    val showLoading: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val showToast: SingleLiveEvent<String> = SingleLiveEvent()
    val buttonClickable: SingleLiveEvent<Boolean> = SingleLiveEvent()

}
