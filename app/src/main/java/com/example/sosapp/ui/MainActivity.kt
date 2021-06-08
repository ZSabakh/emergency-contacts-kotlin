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
import com.example.sosapp.models.ContactsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(){
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewInitializations()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        fetchContacts()

        val contacts = mutableListOf(
            "Davit", "Zaza", "Misha",
            "Bidzina", "Shalva", "Natela"
        )

        GridLayoutManager(
            this,
            2,
            RecyclerView.VERTICAL,
            false
        ).apply {
            recyclerView.layoutManager = this
        }

        recyclerView.adapter = RecyclerViewAdapter(contacts)

    }


    private fun viewInitializations(){
        recyclerView = findViewById(R.id.recyclerView)
    }

    private fun fetchContacts(){
        apiClient.getApiService().fetchPosts(token = "${sessionManager.fetchAuthToken()}")
            .enqueue(object : Callback<ContactsResponse> {
                override fun onFailure(call: Call<ContactsResponse>, t: Throwable) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                }

                override fun onResponse(call: Call<ContactsResponse>, response: Response<ContactsResponse>) {
                    if (response.code() != 200) {
                        Toast.makeText(this@MainActivity, "Authorization issue!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    val loginResponse = response.body()
                    Toast.makeText(this@MainActivity, "Contacts fetched!", Toast.LENGTH_SHORT).show()
                    if (loginResponse != null) {
                        println(loginResponse.contacts[0].contact_name)
                    }
                }
            })
    }
}


