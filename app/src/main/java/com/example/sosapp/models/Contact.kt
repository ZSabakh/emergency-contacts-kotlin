package com.example.sosapp.models

import com.google.gson.annotations.SerializedName

data class Contact(
    @SerializedName("contact_name")
    val contact_name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("_id")
    val _id: String
)
