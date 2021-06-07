package com.example.sosapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sosapp.R
import com.example.sosapp.api.ApiClient
import com.example.sosapp.api.SessionManager
import com.example.sosapp.models.ContactsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(){
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        fetchContacts()
    }


    private fun fetchContacts() {
        apiClient.getApiService().fetchPosts(token = "${sessionManager.fetchAuthToken()}")
            .enqueue(object : Callback<ContactsResponse> {
                override fun onFailure(call: Call<ContactsResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<ContactsResponse>, response: Response<ContactsResponse>) {
                    val loginResponse = response.body()
                    Toast.makeText(this@MainActivity, "Contacts fetched!", Toast.LENGTH_SHORT).show()
                    if (loginResponse != null) {
                        println(loginResponse.contacts[0].contact_name)
                    }
                }
            })
    }
}


