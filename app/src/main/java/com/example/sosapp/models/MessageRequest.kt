package com.example.sosapp.models

import com.google.gson.annotations.SerializedName

data class MessageRequest(
    @SerializedName("phone")
    val phone: ArrayList<String>,
    @SerializedName("text")
    val text: String
)