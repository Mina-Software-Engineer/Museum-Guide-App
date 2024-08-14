package com.example.musuemguide.serverSide

import com.google.gson.annotations.SerializedName

data class QuestionModel(
    @SerializedName("text")
    val text: String
)