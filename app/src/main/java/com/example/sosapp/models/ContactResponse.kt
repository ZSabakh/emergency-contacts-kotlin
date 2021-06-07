package com.example.sosapp.models

import com.google.gson.annotations.SerializedName


data class ContactResponse(
        @SerializedName("contact_name")
        val contact_name: String,
        @SerializedName("phone")
        val phone: String
        )