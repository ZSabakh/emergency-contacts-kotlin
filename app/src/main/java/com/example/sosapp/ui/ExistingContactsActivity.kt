package com.example.sosapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sosapp.ContactsRecyclerViewAdapter
import com.example.sosapp.R
import com.example.sosapp.ui.models.ContactUIModel
import com.example.sosapp.utility.ContactData
import com.example.sosapp.utility.retrieveAllContacts

class ExistingContactsActivity: AppCompatActivity() {
    private lateinit var rvExistingContacts: RecyclerView

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_existing_contacts)
        viewInitializations()

        rvExistingContacts.layoutManager = GridLayoutManager(this, 1)
        var contacts: List<ContactData> = retrieveAllContacts()
        contacts = contacts.filter { contact -> contact.phoneNumber.any()}

        rvExistingContacts.adapter = ContactsRecyclerViewAdapter(contacts.map {
            println(it.phoneNumber[0])
            ContactUIModel(
                it.name,
                it.phoneNumber[0],
                it.contactId.toString(),
                onClick = {
//                    btRemoveContacts.visibility = View.VISIBLE
//                    btSendText.visibility = View.VISIBLE
//                    if(!selectedContactNumbers.contains(it.phone)){
//                        selectedContactNumbers.add(it.phone)
//                        selectedContactIDs.add(it._id)
//                    }else{
//                        selectedContactNumbers.remove(it.phone)
//                        selectedContactIDs.remove(it._id)
//                    }
//                    btSendText.text = "Send to ${selectedContactNumbers.size}"
//                    if(selectedContactNumbers.size == 0){
//                        btRemoveContacts.visibility = View.INVISIBLE
//                        btSendText.visibility = View.INVISIBLE
//                    }
                },
            )
        })
    }

    fun viewInitializations() {
        rvExistingContacts = findViewById(R.id.rv_existing_contacts)
    }
}
