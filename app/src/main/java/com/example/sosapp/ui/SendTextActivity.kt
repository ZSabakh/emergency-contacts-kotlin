package com.example.sosapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sosapp.R
import com.example.sosapp.api.ApiClient
import com.example.sosapp.api.SessionManager
import com.example.sosapp.models.Contact
import com.example.sosapp.models.MessageRequest
import com.example.sosapp.models.MessageResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendTextActivity: AppCompatActivity() {
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    private lateinit var btSendText: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_text)
        viewInitializations()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        val selectedContacts = intent.getSerializableExtra("selected_contacts")

        btSendText.setOnClickListener{
            val intent = Intent(this@SendTextActivity, MainActivity::class.java)

            apiClient.getApiService().sendText(
                token = "${sessionManager.fetchAuthToken()}",
                MessageRequest(selectedContacts as ArrayList<String>, "TEST TEST")
            )
                .enqueue(object : Callback<MessageResponse> {
                    override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                        startActivity(intent)
                    }

                    override fun onResponse(
                        call: Call<MessageResponse>,
                        response: Response<MessageResponse>
                    ) {
                        if (response.code() != 200) {
//                            Toast.makeText(this@AddContactActivity, "Error", Toast.LENGTH_SHORT).show()
//                            startActivity(intent)
                        }
                        Toast.makeText(
                            this@SendTextActivity,
                            "Text submitted!",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(intent)
                    }
                })
        }
    }

    private fun viewInitializations() {
        btSendText = findViewById(R.id.bt_create_text)
    }
}