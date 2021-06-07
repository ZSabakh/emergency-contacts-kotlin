package com.example.sosapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sosapp.R
import com.example.sosapp.api.ApiClient
import com.example.sosapp.api.SessionManager
import com.example.sosapp.models.SignInRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.sosapp.models.UserResponse

class LoginActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btLogin: Button
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewInitializations()

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        btLogin.setOnClickListener {
            apiClient.getApiService().login(SignInRequest(username = etUsername.text.toString(), password = etPassword.text.toString()))
                    .enqueue(object : Callback<UserResponse> {
                        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                            Toast.makeText(this@LoginActivity, "Login failure!", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                            val loginResponse = response.body()
                            if (loginResponse != null) {
                                if (loginResponse.accessToken.length > 1) {
                                    Toast.makeText(this@LoginActivity, "Success!", Toast.LENGTH_SHORT).show()
                                    sessionManager.saveAuthToken(loginResponse.accessToken)
                                } else {
                                    Toast.makeText(this@LoginActivity, "Credentials incorrect!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    })

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }


    fun viewInitializations() {
        etUsername = findViewById(R.id.et_login_username)
        etPassword = findViewById(R.id.et_login_password)
        btLogin = findViewById(R.id.bt_login)
    }
}