package com.example.tp3veilletechnologique

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity: AppCompatActivity() {
    lateinit var username : EditText
    lateinit var password : EditText
    lateinit var passwordCheck : EditText
    lateinit var register : Button

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)
    }
}