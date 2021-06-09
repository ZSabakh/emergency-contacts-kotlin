package com.example.sosapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.sosapp.R

class AddContactActivity : AppCompatActivity() {
    private lateinit var etNewContactName: EditText
    private lateinit var etNewContactPhone: EditText
    private lateinit var btNewContactSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        viewInitializations()

        btNewContactSubmit.setOnClickListener {

        }
    }

    fun viewInitializations() {
        etNewContactName = findViewById(R.id.et_new_contact_name)
        etNewContactPhone = findViewById(R.id.et_new_contact_phone)
        btNewContactSubmit = findViewById(R.id.bt_add_contact)
    }
}