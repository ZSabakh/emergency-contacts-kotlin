package com.example.sosapp.models

import com.google.gson.annotations.SerializedName

data class ContactsRequest(
    @SerializedName("contacts")
    val contacts: MutableList<Contact>,
)
