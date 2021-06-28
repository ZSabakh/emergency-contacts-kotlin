package com.example.sosapp.ui.models

data class TextUIModel(
    val _id: String,
    val user_id: String?,
    val text: String,
    val onClick: () -> Unit
)