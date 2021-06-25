package com.example.sosapp.models

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("roles")
    val roles: Array<String>
)