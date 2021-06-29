package com.example.sosapp.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.sosapp.R

class CustomTextActivity: AppCompatActivity() {
    private lateinit var btCompleteCustomText: Button
    private lateinit var etCustomText: EditText
    private lateinit var cbSendText: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_text)
        viewInitializations()


    }



    fun viewInitializations() {
        btCompleteCustomText = findViewById(R.id.bt_complete_custom_text)
        etCustomText = findViewById(R.id.et_custom_text)
        cbSendText = findViewById(R.id.cb_send_text)
    }
}