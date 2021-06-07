package com.example.sosapp.models

import com.google.gson.annotations.SerializedName

data class UserResponse(
        @SerializedName("id")
        val id: String,
        @SerializedName("username")
        val username: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("accessToken")
        val accessToken: String)