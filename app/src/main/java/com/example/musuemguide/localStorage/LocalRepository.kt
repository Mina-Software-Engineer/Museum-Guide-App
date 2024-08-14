package com.example.musuemguide.localStorage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musuemguide.localStorage.dto.ArtifactDTO
import com.example.musuemguide.localStorage.local.ArtifactDao
import com.example.musuemguide.localStorage.local.DataSource
import com.example.musuemguide.serverSide.NetworkUtils
import com.example.musuemguide.serverSide.QuestionModel
import com.example.musuemguide.serverSide.ResponseModel
import com.example.musuemguide.utils.TextToSpeechUtil
import retrofit2.Call
import java.io.File

class LocalRepository(
    private val artifactDao: ArtifactDao,
) : DataSource {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response



    override suspend fun addArtifacts(artifacts: List<ArtifactDTO>) {

        artifactDao.addAllArtifacts(artifacts)
    }

    override suspend fun getAllArtifacts(): List<ArtifactDTO> {

        return artifactDao.getAllArtifacts()
    }

    override suspend fun deleteAllArtifacts() {
        artifactDao.clearDatabase()
    }

    fun sendTextAndReceiveEnglishResponse(text: String) {

        val englishAPI = NetworkUtils.instance.getEnglishAPI(QuestionModel(text))
        englishAPI.enqueue(object : retrofit2.Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: retrofit2.Response<ResponseModel>
            ) {
                if (response.isSuccessful) {
                    _response.value = response.body()!!.response
                    Log.d("checkasdklajd", response.body()!!.response)
                } else {
                    _response.value = "Error: ${response.code()}"
                    Log.d("checkasdklajd", response.body()!!.response)
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                _response.value = "Error: ${t.message}"
                Log.d("checkasdklajd", _response.value.toString())
            }
        })
    }

    fun sendTextAndReceiveFrenchResponse(text: String) {

        val englishAPI = NetworkUtils.instance.getFrenchAPI(QuestionModel(text))
        englishAPI.enqueue(object : retrofit2.Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: retrofit2.Response<ResponseModel>
            ) {
                if (response.isSuccessful) {
                    _response.value = response.body()!!.response
                    Log.d("checkasdklajd", response.body()!!.response)
                } else {
                    _response.value = "Error: ${response.code()}"
                    Log.d("checkasdklajd", response.body()!!.response)
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                _response.value = "Error: ${t.message}"
                Log.d("checkasdklajd", _response.value.toString())
            }
        })
    }

    fun sendTextAndReceiveGermanResponse(text: String) {

        val englishAPI = NetworkUtils.instance.getGermanAPI(QuestionModel(text))
        englishAPI.enqueue(object : retrofit2.Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: retrofit2.Response<ResponseModel>
            ) {
                if (response.isSuccessful) {
                    _response.value = response.body()!!.response
                    Log.d("checkasdklajd", response.body()!!.response)
                } else {
                    _response.value = "Error: ${response.code()}"
                    Log.d("checkasdklajd", response.body()!!.response)
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                _response.value = "Error: ${t.message}"
                Log.d("checkasdklajd", _response.value.toString())
            }
        })
    }
}