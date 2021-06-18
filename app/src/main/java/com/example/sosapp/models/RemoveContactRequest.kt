package com.example.sosapp.models

import com.google.gson.annotations.SerializedName

class RemoveContactRequest (
    @SerializedName("_id")
    val _id: ArrayList<String>,
)