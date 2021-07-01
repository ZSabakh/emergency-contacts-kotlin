package com.example.sosapp.models

import com.google.gson.annotations.SerializedName

data class FetchedTextResponse (
    @SerializedName("_id")
    val _id: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("user_id")
    val user_id: String,
        )