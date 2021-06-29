package com.example.sosapp.ui
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sosapp.LocationHelper
import com.example.sosapp.R
import com.example.sosapp.TextsRecyclerViewAdapter
import com.example.sosapp.api.ApiClient
import com.example.sosapp.api.SessionManager
import com.example.sosapp.models.*
import com.example.sosapp.ui.models.TextUIModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class SendTextActivity : AppCompatActivity() {
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    private lateinit var btSendCustomText: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var fetchedTexts: MutableList<FetchedTextResponse>
    private lateinit var selectedContacts: Serializable
    val coordinates: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_text)
        viewInitializations()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        fetchTexts()
        recyclerView.layoutManager = GridLayoutManager(this, 2)


        LocationHelper().startListeningUserLocation(this , object : LocationHelper.MyLocationListener {
            override fun onLocationChanged(location: Location) {
                coordinates.add(location.latitude.toString())
                coordinates.add(location.longitude.toString())
            }
        })


        selectedContacts = intent.getSerializableExtra("selected_contacts")!!

        btSendCustomText.setOnClickListener {
            val intent = Intent(this@SendTextActivity, CustomTextActivity::class.java)
            intent.putExtra("selected_contacts", selectedContacts)
            intent.putExtra("coordinates", coordinates as Serializable)
            startActivity(intent)
        }
    }

    private fun fetchTexts() {
        apiClient.getApiService().fetchTexts(token = "${sessionManager.fetchAuthToken()}")
            .enqueue(object : Callback<FetchedTextsResponse> {
                override fun onFailure(call: Call<FetchedTextsResponse>, t: Throwable) {
                    val intent = Intent(this@SendTextActivity, MainActivity::class.java)
                    startActivity(intent)
                }

                override fun onResponse(
                    call: Call<FetchedTextsResponse>,
                    response: Response<FetchedTextsResponse>
                ) {
                    if (response.code() != 200) {
                        Toast.makeText(
                            this@SendTextActivity,
                            "Authorization issue!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@SendTextActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    val textsResponse = response.body()
                    Toast.makeText(this@SendTextActivity, "Texts fetched!", Toast.LENGTH_SHORT)
                        .show()
                    if (textsResponse != null) {
                        fetchedTexts = textsResponse.texts
                        recyclerView.adapter = TextsRecyclerViewAdapter(fetchedTexts.map {
                            println(it)
                            TextUIModel(
                                it._id,
                                it.user_id,
                                it.text,
                                onClick = {
                                    sendText(it.text)
                                },
                            )
                        })
                    }
                }
            })
    }

    fun sendText(text: String){
        val intent = Intent(this@SendTextActivity, MainActivity::class.java)

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
                        Toast.makeText(this@SendTextActivity, "Error", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(intent)
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

    private fun viewInitializations() {
        recyclerView = findViewById(R.id.rv_texts)
        btSendCustomText = findViewById(R.id.bt_send_custom_text)
    }
}