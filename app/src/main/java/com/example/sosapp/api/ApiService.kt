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
    fun postContact(@Header("x-access-token") token: String, @Body request:ContactsRequest): Call <Contact>

    @POST(Constants.ADD_TEXT_URL)
    fun postText(@Header("x-access-token") token: String, @Body request:SubmitTextRequest): Call <SubmitRemoveResponse>

    @POST(Constants.REMOVE_CONTACTS_URL)
    fun removeContacts(@Header("x-access-token") token: String, @Body request:RemoveRequest): Call <Contact>

    @POST(Constants.REMOVE_TEXTS_URL)
    fun removeTexts(@Header("x-access-token") token: String, @Body request: RemoveRequest): Call <SubmitRemoveResponse>

    @GET(Constants.CONTACTS_URL)
    fun fetchPosts(@Header("x-access-token") token: String): Call<ContactsResponse>

    @GET(Constants.TEXTS_URL)
    fun fetchTexts(@Header("x-access-token") token: String): Call <FetchedTextsResponse>

    @POST(Constants.SEND_TEXT_URL)
    fun sendText(@Header("x-access-token") token: String, @Body request:MessageRequest): Call <MessageResponse>
}