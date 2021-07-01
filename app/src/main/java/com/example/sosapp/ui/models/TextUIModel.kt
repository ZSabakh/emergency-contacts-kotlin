package com.example.sosapp.ui.models

data class TextUIModel(
    val _id: String,
    val user_id: String?,
    val text: String,
    val isSelected: Boolean,
    val isAdmin: Boolean,
    val onClick: () -> Unit
)