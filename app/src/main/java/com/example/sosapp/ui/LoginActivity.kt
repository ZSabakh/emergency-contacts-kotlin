package com.example.sosapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.sosapp.R
import com.example.sosapp.api.ApiClient
import com.example.sosapp.api.SessionManager
import com.example.sosapp.models.ContactResponse
import com.example.sosapp.models.SignInRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.sosapp.models.UserRequest

class LoginActivity : AppCompatActivity() {
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var btLogin: Button
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        apiClient.getApiService().login(SignInRequest(username = "zura", password = "misha777"))
                .enqueue(object : Callback<UserRequest> {
                    override fun onFailure(call: Call<UserRequest>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<UserRequest>, response: Response<UserRequest>) {
                        val loginResponse = response.body()

                        if (loginResponse != null) {
                            if (loginResponse.accessToken.length > 1) {
                                sessionManager.saveAuthToken(loginResponse.accessToken)
                            } else {
                            }
                        }
                    }
                })

    }

    private fun fetchContacts() {
        apiClient.getApiService().fetchPosts(token = "Bearer ${sessionManager.fetchAuthToken()}")
                .enqueue(object : Callback<ContactResponse> {
                    override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<ContactResponse>, response: Response<ContactResponse>) {
                    }
                })
    }
}