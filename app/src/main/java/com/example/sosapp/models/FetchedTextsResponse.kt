package com.example.sosapp.models

import com.google.gson.annotations.SerializedName

data class FetchedTextsResponse(
    @SerializedName("texts")
    val texts: ArrayList<FetchedTextResponse>
)