package com.example.sosapp.api

import com.example.sosapp.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST(Constants.LOGIN_URL)
    fun login(@Body request: SignInRequest): Call<UserResponse>

    @POST(Constants.ADD_CONTACT_URL)
    fun postContact(@Header("x-access-token") token: String, @Body request:Contact): Call <Contact>

    @GET(Constants.CONTACTS_URL)
    fun fetchPosts(@Header("x-access-token") token: String): Call<ContactsResponse>

    @POST(Constants.SEND_TEXT_URL)
    fun sendText(@Header("x-access-token") token: String, @Body request:MessageRequest): Call <MessageResponse>
}