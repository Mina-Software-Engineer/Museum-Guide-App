package com.example.musuemguide.serverSide

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServer {
    @POST("english/process/")
    fun getEnglishAPI(@Body data: QuestionModel): Call<ResponseModel>

    @POST("french/process/")
    fun getFrenchAPI(@Body data: QuestionModel): Call<ResponseModel>

    @POST("german/process/")
    fun getGermanAPI(@Body data: QuestionModel): Call<ResponseModel>
}