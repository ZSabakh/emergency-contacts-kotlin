package com.example.sosapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sosapp.R
import com.example.sosapp.RecyclerViewAdapter
import com.example.sosapp.api.ApiClient
import com.example.sosapp.api.SessionManager
import com.example.sosapp.models.Contact
import com.example.sosapp.models.ContactsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactsTest: MutableList<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewInitializations()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        fetchContacts()
        
        recyclerView.layoutManager = GridLayoutManager(this, 2)

    }


    private fun viewInitializations() {
        recyclerView = findViewById(R.id.recyclerView)
    }

    private fun fetchContacts() {
        apiClient.getApiService().fetchPosts(token = "${sessionManager.fetchAuthToken()}")
            .enqueue(object : Callback<ContactsResponse> {
                override fun onFailure(call: Call<ContactsResponse>, t: Throwable) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                }

                override fun onResponse(
                    call: Call<ContactsResponse>,
                    response: Response<ContactsResponse>
                ) {
                    if (response.code() != 200) {
                        Toast.makeText(
                            this@MainActivity,
                            "Authorization issue!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    val loginResponse = response.body()
                    Toast.makeText(this@MainActivity, "Contacts fetched!", Toast.LENGTH_SHORT)
                        .show()
                    if (loginResponse != null) {
                        contactsTest = loginResponse.contacts
                        recyclerView.adapter = RecyclerViewAdapter(contactsTest)
                    }
                }
            })
    }
}


