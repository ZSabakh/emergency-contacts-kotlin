package com.example.sosapp.models

import com.google.gson.annotations.SerializedName

data class SubmitRemoveResponse(
    @SerializedName("message")
    val message: String,
)
