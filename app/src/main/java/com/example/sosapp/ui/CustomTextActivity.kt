package com.example.sosapp.ui
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sosapp.R
import com.example.sosapp.api.ApiClient
import com.example.sosapp.api.SessionManager
import com.example.sosapp.models.MessageRequest
import com.example.sosapp.models.MessageResponse
import com.example.sosapp.models.SubmitTextRequest
import com.example.sosapp.models.SubmitTextResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class CustomTextActivity: AppCompatActivity() {
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    private lateinit var etCustomText: EditText
    private lateinit var cbSendText: CheckBox
    private lateinit var btCompleteCustomText: Button
    private lateinit var selectedContacts: Serializable
    private lateinit var coordinates: MutableList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_text)
        viewInitializations()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        selectedContacts = intent.getSerializableExtra("selected_contacts")!!
        coordinates = intent.getSerializableExtra("coordinates") as MutableList<String>

        btCompleteCustomText.setOnClickListener {
            postText(etCustomText.text.toString())
            if(cbSendText.isChecked){
                sendText(etCustomText.text.toString())
            }else{
                val intent = Intent(this@CustomTextActivity, SendTextActivity::class.java)
                startActivity(intent)
            }
        }
    }


    private fun postText(text: String){
        val intent = Intent(this@CustomTextActivity, MainActivity::class.java)

        apiClient.getApiService().postText(
            token = "${sessionManager.fetchAuthToken()}",
            SubmitTextRequest(text)
        )
            .enqueue(object : Callback<SubmitTextResponse> {
                override fun onFailure(call: Call<SubmitTextResponse>, t: Throwable) {
                    startActivity(intent)
                }

                override fun onResponse(
                    call: Call<SubmitTextResponse>,
                    response: Response<SubmitTextResponse>
                ) {
                    if (response.code() != 200) {
                        Toast.makeText(this@CustomTextActivity, "Error", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    }
                    Toast.makeText(
                        this@CustomTextActivity,
                        "Text submitted!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(intent)
                }
            })
    }

    private fun sendText(text: String){
        val intent = Intent(this@CustomTextActivity, MainActivity::class.java)

        apiClient.getApiService().sendText(
            token = "${sessionManager.fetchAuthToken()}",
            MessageRequest(selectedContacts as ArrayList<String>, text, coordinates)
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
                        Toast.makeText(this@CustomTextActivity, "Error", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(intent)
                    }
                    Toast.makeText(
                        this@CustomTextActivity,
                        "Text submitted!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(intent)
                }
            })
    }

    fun viewInitializations() {
        btCompleteCustomText = findViewById(R.id.bt_complete_custom_text)
        etCustomText = findViewById(R.id.et_custom_text)
        cbSendText = findViewById(R.id.cb_send_text)
    }
}