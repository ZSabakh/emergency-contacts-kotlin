package com.example.sosapp.api

import com.example.sosapp.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST(Constants.LOGIN_URL)
    fun login(@Body request: SignInRequest): Call<UserResponse>

    @POST(Constants.REGISTER_URL)
    fun register(@Body request: SignUpRequest): Call<SignUpResponse>

    @POST(Constants.ADD_CONTACT_URL)
    fun postContact(@Header("x-access-token") token: String, @Body request:Contact): Call <Contact>

    @POST(Constants.REMOVE_CONTACTS_URL)
    fun removeContacts(@Header("x-access-token") token: String, @Body request:RemoveContactRequest): Call <Contact>

    @GET(Constants.CONTACTS_URL)
    fun fetchPosts(@Header("x-access-token") token: String): Call<ContactsResponse>

    @POST(Constants.SEND_TEXT_URL)
    fun sendText(@Header("x-access-token") token: String, @Body request:MessageRequest): Call <MessageResponse>
}