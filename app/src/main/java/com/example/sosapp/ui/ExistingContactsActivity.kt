package com.example.sosapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sosapp.ContactsRecyclerViewAdapter
import com.example.sosapp.R
import com.example.sosapp.api.ApiClient
import com.example.sosapp.api.SessionManager
import com.example.sosapp.models.Contact
import com.example.sosapp.models.ContactsRequest
import com.example.sosapp.ui.models.ContactUIModel
import com.example.sosapp.utility.ContactData
import com.example.sosapp.utility.retrieveAllContacts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExistingContactsActivity : AppCompatActivity() {
    private lateinit var rvExistingContacts: RecyclerView
    private lateinit var cvAddExisting: CardView
    private lateinit var btAddExisting: Button
    private lateinit var tvSelectedContactsCounter: TextView
    private lateinit var etSearchExistingContacts: EditText
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    private val selectedContact: MutableList<ContactData> = ArrayList()
    private val selectedContactIDs: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_existing_contacts)
        viewInitializations()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        rvExistingContacts.layoutManager = GridLayoutManager(this, 1)

        checkPermission()

        etSearchExistingContacts.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                renderContacts(etSearchExistingContacts.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        btAddExisting.setOnClickListener {
            val requests: MutableList<Contact> = mutableListOf()
            selectedContact.map {
                requests.add(Contact(it.name, it.phoneNumber[0], ""))
            }

            val intent = Intent(this@ExistingContactsActivity, MainActivity::class.java)
            apiClient.getApiService().postContact(
                token = "${sessionManager.fetchAuthToken()}",
                ContactsRequest(requests)
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
                            Toast.makeText(
                                this@ExistingContactsActivity,
                                "Error",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            startActivity(intent)
                        }
                        Toast.makeText(
                            this@ExistingContactsActivity,
                            "Contact submitted!",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(intent)
                    }
                })
        }

    }


    private fun renderContacts(contactName: String?) {
        @SuppressLint("MissingPermission")
        var contacts: List<ContactData> = retrieveAllContacts()
        contacts = contacts.filter { contact -> contact.phoneNumber.any() }
        if (!contactName.isNullOrBlank()) {
            contacts =
                contacts.filter { contact -> contact.name.contains(contactName, ignoreCase = true) }
        }

        //Filter search with the above line too.
        rvExistingContacts.adapter = ContactsRecyclerViewAdapter(contacts.map {
            ContactUIModel(
                it.name,
                it.phoneNumber[0],
                it.contactId.toString(),
                onClick = {
                    cvAddExisting.visibility = View.VISIBLE
                    if (!selectedContactIDs.contains(it.contactId.toString())) {
                        selectedContact.add(it)
                        selectedContactIDs.add(it.contactId.toString())
                    } else {
                        selectedContact.remove(it)
                        selectedContactIDs.remove(it.contactId.toString())
                    }
                    tvSelectedContactsCounter.text = "Save ${selectedContactIDs.size} contacts"
                    if (selectedContactIDs.size == 0) {
                        cvAddExisting.visibility = View.INVISIBLE
                    }
                },
            )
        })
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this@ExistingContactsActivity,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                this@ExistingContactsActivity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                1
            )
        } else {
            renderContacts(null)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this@ExistingContactsActivity,
                    "Contact Permission Granted",
                    Toast.LENGTH_SHORT
                ).show()
                renderContacts(null)

            } else {
                Toast.makeText(
                    this@ExistingContactsActivity,
                    "Contact Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun viewInitializations() {
        rvExistingContacts = findViewById(R.id.rv_existing_contacts)
        cvAddExisting = findViewById(R.id.cv_add_existing)
        btAddExisting = findViewById(R.id.bt_add_existing)
        etSearchExistingContacts = findViewById(R.id.et_search_existing_contacts)
        tvSelectedContactsCounter = findViewById(R.id.tv_selected_contacts_counter)
    }
}
