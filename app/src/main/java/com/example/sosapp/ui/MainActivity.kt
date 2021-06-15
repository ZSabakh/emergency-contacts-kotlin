package com.example.sosapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
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
import com.example.sosapp.ui.model.ContactUIModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactsTest: MutableList<Contact>
    private lateinit var btAddContact: Button
    private lateinit var btSendText: Button
    val selectedContacts: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewInitializations()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        fetchContacts()

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        btAddContact.setOnClickListener {
            val intent = Intent(this@MainActivity, AddContactActivity::class.java)
            startActivity(intent)
        }

    }


    private fun viewInitializations() {
        recyclerView = findViewById(R.id.recyclerView)
        btAddContact = findViewById(R.id.bt_add_contact)
        btSendText = findViewById(R.id.bt_send_text)
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
                        recyclerView.adapter = RecyclerViewAdapter(contactsTest.map {
                            ContactUIModel(
                                it.contact_name,
                                it.phone,
                                onClick = {
                                    selectedContacts.add(it.phone)
                                    btSendText.visibility = View.VISIBLE
                                    btSendText.text = "Send to ${selectedContacts.size}"
                                },
                            )
                        })
                    }
                }
            })
    }
}


