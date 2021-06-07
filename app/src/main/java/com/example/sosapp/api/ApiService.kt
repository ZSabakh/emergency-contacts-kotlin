package com.example.sosapp.api

import com.example.sosapp.models.ContactsResponse
import com.example.sosapp.models.SignInRequest
import com.example.sosapp.models.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST(Constants.LOGIN_URL)
    fun login(@Body request: SignInRequest): Call<UserResponse>

    @GET(Constants.CONTACTS_URL)
    fun fetchPosts(@Header("x-access-token") token: String): Call<ContactsResponse>
}