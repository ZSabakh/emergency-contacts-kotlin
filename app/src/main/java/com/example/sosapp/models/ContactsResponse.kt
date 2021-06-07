package com.example.sosapp.models

import com.google.gson.annotations.SerializedName


data class ContactsResponse(
    @SerializedName("contacts")
    val contacts: ArrayList<Contact>
)