package com.example.musuemguide.userSection.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.musuemguide.application.MuseumGuideApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class RobotControlViewModel(app: Application) :
    BaseViewModel(app, (app as MuseumGuideApp).repository) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }
    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private val deviceName = "HC-05" // Change to your robot's Bluetooth name
    private val uuid: UUID =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // .Standard UUID for SPP

    val receivedMessage: MutableLiveData<String> = MutableLiveData()
    val permissionGranted: MutableLiveData<Boolean> = MutableLiveData(false)

    var mediaPlayerA: MediaPlayer? = null
    var audioFileA: File? = null

    var mediaPlayerB: MediaPlayer? = null
    var audioFileB: File? = null

    var pos: Int = 1
    //private var flag = false

    /*
        private val _showBottomSheet = MutableLiveData<Boolean>()
        val showBottomSheet: LiveData<Boolean> = _showBottomSheet
    */

    //private val _serverResponse = MutableLiveData<String>()
    var serverResponse: LiveData<String> = MutableLiveData<String>()

    private val _btnVisibility = MutableLiveData<Int>()
    val btnVisibility: LiveData<Int> = _btnVisibility

    private val _goToText = MutableLiveData<String>()
    val goToText: LiveData<String> = _goToText

    private val _welcomeMsg = MutableLiveData<String>()
    val welcomeMsg: LiveData<String> = _welcomeMsg

    private val _btnText = MutableLiveData<String>()
    val btnText: LiveData<String> = _btnText

    private val _mediaFlag = MutableLiveData<Boolean>()
    val mediaFlag: LiveData<Boolean> = _mediaFlag

    private val _robotLocation = MutableLiveData<String>()
    val robotLocation: LiveData<String> = _robotLocation

    init {
        serverResponse = localRepo.response
    }

    fun showToaster(btnName: String) {
        showToast.value = btnName
    }

    fun showSnackBarTxt(txt: String) {
        showSnackBar.value = txt
    }

    fun showLoading(bool: Boolean) {
        showLoading.value = bool
    }

    fun isButtonClickable(bool: Boolean) {
        buttonClickable.value = bool
    }

    private fun changeBtnTextTo(txt: String) {
        _btnText.value = txt
    }


    @SuppressLint("MissingPermission")
    fun setupBluetooth() {
        if (bluetoothAdapter == null) {
            return
        }

        if (!bluetoothAdapter!!.isEnabled) {
            bluetoothAdapter!!.enable()
        }

        val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter!!.bondedDevices
        val device: BluetoothDevice? = pairedDevices.find { it.name == deviceName }

        if (device != null) {
            viewModelScope.launch {
                connectToDevice(device)
            }
        }
    }

    fun playAudioA() {
        if (audioFileA == null || !audioFileA!!.exists()) {
            return
        }
        mediaPlayerA = MediaPlayer().apply {
            setDataSource(audioFileA!!.absolutePath)
            prepare()
            start()
            setOnCompletionListener {
                onAudioPlayCompletedA()
            }
        }
    }

    fun playAudioB() {
        if (audioFileB == null || !audioFileB!!.exists()) {
            return
        }
        mediaPlayerB = MediaPlayer().apply {
            setDataSource(audioFileB!!.absolutePath)
            prepare()
            start()
        }
    }


    private fun onAudioPlayCompletedA() {
        // Handle completion logic for mediaPlayerA
        pos++
        if (pos <= 6) {
            startTour(pos.toString())
        } else if (pos == 7) {
            makeSpeech("Thank you for visiting the museum. Have a great day!")
        } else {
            showSnackBarTxt("Tour completed.")
        }
    }

    /*fun onAudioPlayCompletedB() {
        // Handle completion logic for mediaPlayerB
        pos++
        if (pos <= 6) {
            startTour(pos.toString())
        } else if (pos == 7) {
            makeSpeech("Thank you for visiting the museum. Have a great day!")
        } else {
            showSnackBarTxt("Tour completed.")
        }
    }*/

    @SuppressLint("MissingPermission")
    private suspend fun connectToDevice(device: BluetoothDevice) = withContext(Dispatchers.IO) {
        //isButtonClickable(false)
        try {

            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            inputStream = bluetoothSocket?.inputStream
            outputStream = bluetoothSocket?.outputStream
            if (bluetoothSocket?.isConnected == true) {
                withContext(Dispatchers.Main) {
                    viewModelScope.launch {
                        showSnackBarTxt("Connected to Bluetooth module")
                        btnVisibilityChange(0)  //Visible
                    }
                }
                readData()
            } else {
                withContext(Dispatchers.Main) {
                    showSnackBarTxt("Failed to connect to Bluetooth module")
                    isButtonClickable(true)
                }
            }
            // Start reading from the InputStream
            //readData()
        } catch (e: IOException) {
            withContext(Dispatchers.Main) {
                showSnackBarTxt("Connection error: ${e.message}")
                showLoading(false)
                isButtonClickable(true)
            }
            try {
                bluetoothSocket?.close()
            } catch (closeException: IOException) {
                withContext(Dispatchers.Main) {
                    showSnackBarTxt("Error closing socket: ${closeException.message}")
                }
            }
        }
    }

    private suspend fun readData() = withContext(Dispatchers.IO) {
        val buffer = ByteArray(1024)
        var bytes: Int

        while (true) {
            try {
                bytes = inputStream?.read(buffer) ?: 0
                val readMessage = String(buffer, 0, bytes)
                withContext(Dispatchers.Main) {
                    receivedMessage.value = readMessage
                    //btnVisibilityChange(0)  //Visible
                }
            } catch (e: IOException) {
                break
            }

        }
    }

    @SuppressLint("MissingPermission")
    fun sendPosition(position: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (bluetoothSocket!!.isConnected) {
                    outputStream?.write(position.toByteArray())
                    Log.d("kjasbdkjassd", "bt is connected")
                } else {
                    /*val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter!!.bondedDevices
                    val device: BluetoothDevice? = pairedDevices.find { it.name == deviceName }
                    if (device != null) {
                        connectToDevice(device)
                    }*/

                    showSnackBarTxt("Bluetooth not connected")

                }
            } catch (e: IOException) {
                //Log.d("jhghjgjgjkk", e.message.toString())
                val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter!!.bondedDevices
                val device: BluetoothDevice? = pairedDevices.find { it.name == deviceName }
                if (device != null) {
                    connectToDevice(device)
                    sendPosition(position)
                }

                //showSnackBarTxt("Error sending data: ${e.message}")
            }
        }

    }


    private fun btnVisibilityChange(visibility: Int) {
        _btnVisibility.value = visibility
    }

    fun btnTextChange(text: String) {
        _goToText.value = text
    }

    fun makeSpeech(text: String) {
        _welcomeMsg.value = text

    }

    fun sendText(text: String, language: String) {

        when (language) {
            "en" -> {
                //serverResponse = localRepo.response
                Log.d("ajkjkgjga", serverResponse.value.toString())
                localRepo.sendTextAndReceiveEnglishResponse(text)
            }

            "fr" -> localRepo.sendTextAndReceiveFrenchResponse(text)
            "de" -> localRepo.sendTextAndReceiveGermanResponse(text)
        }

    }

    fun startTour(position: String) {
        _robotLocation.value = position
    }

    fun setFlag(flag: Boolean) {
        _mediaFlag.value = flag
    }

    /* fun setButtonSheetBool(flag: Boolean) {
         _showBottomSheet.value = flag
     }*/

    fun pauseAudioA() {
        mediaPlayerA?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    fun pauseAudioB() {
        mediaPlayerB?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    fun stopAudioA() {
        mediaPlayerA?.apply {
            if (isPlaying) {
                stop()
                release()
            }
        }
        mediaPlayerA = null
    }

    fun stopAudioB() {
        mediaPlayerB?.apply {
            if (isPlaying) {
                stop()
                release()
            }
        }
        mediaPlayerB = null
    }

    fun resumeAudioA() {
        mediaPlayerA?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }

    fun resumeAudioB() {
        mediaPlayerB?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }
}