package com.example.sosapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sosapp.R
import com.example.sosapp.RecyclerViewAdapter
import com.example.sosapp.api.ApiClient
import com.example.sosapp.api.SessionManager
import com.example.sosapp.models.Contact
import com.example.sosapp.models.ContactsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddContactActivity : AppCompatActivity() {
    private lateinit var etNewContactName: EditText
    private lateinit var etNewContactPhone: EditText
    private lateinit var btNewContactSubmit: Button
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        viewInitializations()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        btNewContactSubmit.setOnClickListener {
            val intent = Intent(this@AddContactActivity, MainActivity::class.java)
            apiClient.getApiService().postContact(
                token = "${sessionManager.fetchAuthToken()}",
                Contact(etNewContactName.text.toString(), etNewContactPhone.text.toString())
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
                            Toast.makeText(this@AddContactActivity, "Error", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }
                        Toast.makeText(
                            this@AddContactActivity,
                            "Contact submitted!",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(intent)
                    }
                })


        }
    }

    fun viewInitializations() {
        etNewContactName = findViewById(R.id.et_new_contact_name)
        etNewContactPhone = findViewById(R.id.et_new_contact_phone)
        btNewContactSubmit = findViewById(R.id.bt_new_contact_submit)
    }
}