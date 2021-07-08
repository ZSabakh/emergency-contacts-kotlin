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
import com.example.sosapp.ContactsRecyclerViewAdapter
import com.example.sosapp.api.ApiClient
import com.example.sosapp.api.SessionManager
import com.example.sosapp.models.*
import com.example.sosapp.ui.models.ContactUIModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactsTest: MutableList<Contact>
    private lateinit var btAddContact: FloatingActionButton
    private lateinit var btGotoTexts: FloatingActionButton
    private lateinit var btSendText: Button
    private lateinit var btRemoveContacts: Button
    val selectedContactNumbers: MutableList<String> = ArrayList()
    val selectedContactIDs: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewInitializations()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        fetchContacts()

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        btRemoveContacts.setOnClickListener{
            apiClient.getApiService().removeContacts(
                token = "${sessionManager.fetchAuthToken()}",
                RemoveRequest(selectedContactIDs as ArrayList<String>)
            )
                .enqueue(object : Callback<Contact> {
                    override fun onFailure(call: Call<Contact>, t: Throwable) {
                        startActivity(intent)
                    }

                    override fun onResponse(
                        call: Call<Contact>,
                        response: Response<Contact>
                    ) {
                        if (response.code() != 200) {
                            Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(
                                this@MainActivity,
                                "Contact removed!",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(intent)
                        }
                    }
                })
            fetchContacts()
            selectedContactIDs.clear()
            selectedContactNumbers.clear()
        }
        btAddContact.setOnClickListener {
            val intent = Intent(this@MainActivity, AddContactActivity::class.java)
            startActivity(intent)
        }
        btSendText.setOnClickListener{
            val intent = Intent(this@MainActivity, SendTextActivity::class.java)
            intent.putExtra("selected_contacts", selectedContactNumbers as Serializable)
            startActivity(intent)
        }
        btGotoTexts.setOnClickListener {
            val intent = Intent(this@MainActivity, SendTextActivity::class.java)
            intent.putExtra("save_only", true)
            startActivity(intent)
        }

    }


    private fun viewInitializations() {
        recyclerView = findViewById(R.id.rv_contacts)
        btAddContact = findViewById(R.id.bt_add_contact)
        btSendText = findViewById(R.id.bt_send_custom_text)
        btRemoveContacts = findViewById(R.id.bt_remove_contacts)
        btGotoTexts = findViewById(R.id.bt_goto_texts)
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
                        recyclerView.adapter = ContactsRecyclerViewAdapter(contactsTest.map {
                            ContactUIModel(
                                it.contact_name,
                                it.phone,
                                it._id,
                                onClick = {
                                    btRemoveContacts.visibility = View.VISIBLE
                                    btSendText.visibility = View.VISIBLE
                                    if(!selectedContactNumbers.contains(it.phone)){
                                        selectedContactNumbers.add(it.phone)
                                        selectedContactIDs.add(it._id)
                                    }else{
                                        selectedContactNumbers.remove(it.phone)
                                        selectedContactIDs.remove(it._id)
                                    }
                                    btSendText.text = "Send to ${selectedContactNumbers.size}"
                                    if(selectedContactNumbers.size == 0){
                                        btRemoveContacts.visibility = View.INVISIBLE
                                        btSendText.visibility = View.INVISIBLE
                                    }
                                },
                            )
                        })
                    }
                }
            })
    }
}


