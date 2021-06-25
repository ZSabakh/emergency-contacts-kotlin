package com.example.sosapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sosapp.R
import com.example.sosapp.api.ApiClient
import com.example.sosapp.api.SessionManager
import com.example.sosapp.models.SignUpRequest
import com.example.sosapp.models.SignUpResponse
import com.example.sosapp.models.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var btRegister: Button
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        viewInitializations()

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        btRegister.setOnClickListener {
            apiClient.getApiService().register(
                SignUpRequest(
                    etUsername.text.toString(),
                    etPhone.text.toString(),
                    etPassword.text.toString(),
                    arrayOf("user"),
                )
            )
                .enqueue(object : Callback<SignUpResponse> {
                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Register failure!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    override fun onResponse(
                        call: Call<SignUpResponse>,
                        response: Response<SignUpResponse>
                    ) {
                        val registerResponse = response.body()
                        if (registerResponse != null) {
                            Toast.makeText(this@RegisterActivity, registerResponse.message, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                })
        }
    }


    private fun viewInitializations() {
        etUsername = findViewById(R.id.et_register_username)
        etPhone = findViewById(R.id.et_register_phone)
        etPassword = findViewById(R.id.et_register_password)
        btRegister = findViewById(R.id.bt_register_submit)
    }
}