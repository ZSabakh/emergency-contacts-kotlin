package com.example.sosapp.models

import com.google.gson.annotations.SerializedName

data class SubmitTextRequest(
    @SerializedName("text")
    val text: String,
)
