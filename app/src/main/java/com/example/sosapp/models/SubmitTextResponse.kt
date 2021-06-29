package com.example.sosapp.models

import com.google.gson.annotations.SerializedName

data class SubmitTextResponse(
    @SerializedName("message")
    val message: String,
)
