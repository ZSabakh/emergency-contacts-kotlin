package com.example.sosapp.api

import com.example.sosapp.models.Contact
import com.example.sosapp.models.ContactsResponse
import com.example.sosapp.models.SignInRequest
import com.example.sosapp.models.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST(Constants.LOGIN_URL)
    fun login(@Body request: SignInRequest): Call<UserResponse>

    @POST(Constants.ADD_CONTACT_URL)
    fun postContact(@Header("x-access-token") token: String, @Body request:Contact): Call <Contact>

    @GET(Constants.CONTACTS_URL)
    fun fetchPosts(@Header("x-access-token") token: String): Call<ContactsResponse>
}