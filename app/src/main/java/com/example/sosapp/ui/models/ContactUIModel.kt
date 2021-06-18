package com.example.sosapp.ui.models

data class ContactUIModel(
    val contactName: String,
    val phone: String,
    val _id: String,
    val onClick: () -> Unit
)