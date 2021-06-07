package com.example.sosapp.api

import com.example.sosapp.models.ContactResponse
import com.example.sosapp.models.SignInRequest
import com.example.sosapp.models.UserRequest
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST(Constants.LOGIN_URL)
    fun login(@Body request: SignInRequest): Call<UserRequest>

    @GET(Constants.CONTACTS_URL)
    fun fetchPosts(@Header("x-access-token") token: String): Call<ContactResponse>
}