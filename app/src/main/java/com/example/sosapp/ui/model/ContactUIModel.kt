package com.example.sosapp.ui.model

data class ContactUIModel(
    val contactName: String,
    val phone: String,
    val onClick: () -> Unit
)