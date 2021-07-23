package com.example.sosapp.ui

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sosapp.LocationHelper
import com.example.sosapp.R
import com.example.sosapp.TextsRecyclerViewAdapter
import com.example.sosapp.api.ApiClient
import com.example.sosapp.api.SessionManager
import com.example.sosapp.models.*
import com.example.sosapp.ui.models.TextUIModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    private lateinit var btSelectRemoveTexts: FloatingActionButton
    private lateinit var cvRemoveContacts: CardView
    private lateinit var btRemoveTexts: Button
    private lateinit var tvSelectedTextsCounter: TextView
    private var saveOnly: Boolean = false
    private val selectedTextIDs: MutableList<String> = ArrayList()
    private var isRemovingTexts: Boolean = false

    val coordinates: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_text)
        viewInitializations()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        fetchTexts()
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        LocationHelper().startListeningUserLocation(
            this,
            object : LocationHelper.MyLocationListener {
                override fun onLocationChanged(location: Location) {
                    coordinates.add(location.latitude.toString())
                    coordinates.add(location.longitude.toString())
                }
            })

        if (intent.hasExtra("save_only")) {
            saveOnly = intent.getSerializableExtra("save_only") as Boolean
        }
        if (!saveOnly) {
            selectedContacts = intent.getSerializableExtra("selected_contacts")!!
        } else {

        }

        btSelectRemoveTexts.setOnClickListener {
            if (!isRemovingTexts) {
                isRemovingTexts = true
                cvRemoveContacts.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (isRemovingTexts) {
                tvSelectedTextsCounter.text = "${selectedTextIDs.size}"
                isRemovingTexts = false
                selectedTextIDs.clear()
                tvSelectedTextsCounter.text = "${selectedTextIDs.size}"
                cvRemoveContacts.visibility = View.INVISIBLE
                fetchTexts()
            }
        }

        btRemoveTexts.setOnClickListener {
            removeTexts()
            fetchTexts()
        }

        btSendCustomText.setOnClickListener {
            val intent = Intent(this@SendTextActivity, CustomTextActivity::class.java)
            if (!saveOnly) {
                intent.putExtra("selected_contacts", selectedContacts)
                intent.putExtra("coordinates", coordinates as Serializable)
            } else {
                intent.putExtra("save_only", saveOnly)
            }
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
                    if (textsResponse != null) {
                        fetchedTexts = textsResponse.texts
                        mapTexts(fetchedTexts = fetchedTexts)
                    }
                }
            })
    }

    fun mapTexts(fetchedTexts: MutableList<FetchedTextResponse>) {
        recyclerView.adapter = TextsRecyclerViewAdapter(fetchedTexts.map {
            TextUIModel(
                it._id,
                it.user_id,
                it.text,
                onClick = {
                    if(!saveOnly){
                        if (!isRemovingTexts) {
                            sendText(it.text)
                        } else if (it.user_id != "ADMIN") {
                            cvRemoveContacts.visibility = View.VISIBLE
                            if (!selectedTextIDs.contains(it._id)) {
                                selectedTextIDs.add(it._id)
                            } else {
                                selectedTextIDs.remove(it._id)
                            }
                            tvSelectedTextsCounter.text = "${selectedTextIDs.size}"
                            if (selectedTextIDs.size == 0) {
                            }
                        } else if (it.user_id == "ADMIN") {
                            Toast.makeText(
                                this@SendTextActivity,
                                "Can't remove sample texts",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        mapTexts(fetchedTexts)
                    }
                },
                isSelected = selectedTextIDs.contains(it._id),
                isAdmin = it.user_id == "ADMIN"
            )
        })
    }

    fun sendText(text: String) {
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

    fun removeTexts() {
        apiClient.getApiService().removeTexts(
            token = "${sessionManager.fetchAuthToken()}",
            RemoveRequest(selectedTextIDs as ArrayList<String>)
        )
            .enqueue(object : Callback<SubmitRemoveResponse> {
                override fun onFailure(call: Call<SubmitRemoveResponse>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<SubmitRemoveResponse>,
                    response: Response<SubmitRemoveResponse>
                ) {
                    if (response.code() != 200) {
                        Toast.makeText(this@SendTextActivity, "Error", Toast.LENGTH_SHORT)
                            .show()
                    }
                    Toast.makeText(
                        this@SendTextActivity,
                        "Texts removed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        cvRemoveContacts.visibility = View.INVISIBLE
        isRemovingTexts = false
        selectedTextIDs.clear()
    }

    private fun viewInitializations() {
        recyclerView = findViewById(R.id.rv_texts)
        btSendCustomText = findViewById(R.id.bt_send_custom_text)
        btSelectRemoveTexts = findViewById(R.id.bt_select_remove_texts)
        tvSelectedTextsCounter = findViewById(R.id.tv_selected_texts_counter)
        cvRemoveContacts = findViewById(R.id.cv_remove_contacts)
        btRemoveTexts = findViewById(R.id.bt_remove_texts)
    }
}