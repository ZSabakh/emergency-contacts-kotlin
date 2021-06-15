package com.example.sosapp.ui.models

data class ContactUIModel(
    val contactName: String,
    val phone: String,
    val onClick: () -> Unit
)