package com.example.sosapp.api

import android.widget.Toast
import com.example.sosapp.ApiInterface
import com.example.sosapp.RetrofitInstance
import com.example.sosapp.models.SignInBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun signIn(email: String, password: String){
    val retIn = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
    val signInInfo = SignInBody(email, password)
    retIn.signin(signInInfo).enqueue(object : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//            Toast.makeText(
//                this@MainActivity,
//                t.message,
//                Toast.LENGTH_SHORT
//            ).show()
        }
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.code() == 200) {
                //OK RESPONSE
            } else {
                //FAIL RESPONE
            }
        }
    })
}