package com.example.sosapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.sosapp.R

class RegisterActivity: AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var btRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        viewInitializations()

    }



    private fun viewInitializations() {
        etUsername = findViewById(R.id.et_register_username)
        etPhone = findViewById(R.id.et_register_phone)
        etPassword = findViewById(R.id.et_register_password)
        btRegister = findViewById(R.id.bt_register)
    }
}