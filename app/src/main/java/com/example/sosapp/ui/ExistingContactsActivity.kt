package com.example.sosapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sosapp.ContactsRecyclerViewAdapter
import com.example.sosapp.R
import com.example.sosapp.ui.models.ContactUIModel
import com.example.sosapp.utility.ContactData
import com.example.sosapp.utility.retrieveAllContacts


class ExistingContactsActivity : AppCompatActivity() {
    private lateinit var rvExistingContacts: RecyclerView
    private lateinit var cvAddExisting: CardView
    private lateinit var btAddExisting: Button
    private lateinit var tvSelectedContactsCounter: TextView
    private lateinit var etSearchExistingContacts: EditText
    private val selectedContact: MutableList<ContactData> = ArrayList()
    private val selectedContactIDs: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_existing_contacts)
        viewInitializations()

        rvExistingContacts.layoutManager = GridLayoutManager(this, 1)
        renderContacts(null)

        etSearchExistingContacts.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                renderContacts(etSearchExistingContacts.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })

    }

    private fun renderContacts(contactName: String?) {
        @SuppressLint("MissingPermission")
        var contacts: List<ContactData> =  retrieveAllContacts()
        contacts = contacts.filter { contact -> contact.phoneNumber.any() }
        if(!contactName.isNullOrBlank()){
            contacts = contacts.filter {contact -> contact.name.contains(contactName, ignoreCase = true)}
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

    fun viewInitializations() {
        rvExistingContacts = findViewById(R.id.rv_existing_contacts)
        cvAddExisting = findViewById(R.id.cv_add_existing)
        btAddExisting = findViewById(R.id.bt_add_existing)
        etSearchExistingContacts = findViewById(R.id.et_search_existing_contacts)
        tvSelectedContactsCounter = findViewById(R.id.tv_selected_contacts_counter)
    }
}
