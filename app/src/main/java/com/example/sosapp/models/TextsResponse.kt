package com.example.sosapp.models

import com.google.gson.annotations.SerializedName

data class TextsResponse(
    @SerializedName("texts")
    val texts: ArrayList<TextResponse>
)