package com.example.sosapp.models

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("text")
    val text: String
)