package com.example.musuemguide.serverSide

import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("response")
    val response: String
)
