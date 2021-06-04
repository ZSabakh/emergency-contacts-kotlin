package com.example.sosapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sosapp.models.SignInBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.sosapp.api.signIn

class MainActivity : AppCompatActivity() {
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var btLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewInitializations()
        btLogin.setOnClickListener{
            performSignIn()
        }
    }

    fun viewInitializations() {
        etUsername = findViewById(R.id.et_login_username)
        etPassword = findViewById(R.id.et_login_password)
        btLogin = findViewById(R.id.bt_login)
    }

    fun performSignIn() {
        val user = etUsername.text.toString()
        val password = etPassword.text.toString()
        signIn(user, password)
    }


    fun goToSignup() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}