package com.example.tp3veilletechnologique

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast

class RegisterActivity: AppCompatActivity() {
    val firebaseAuth = FirebaseAuth.getInstance()
    lateinit var username : EditText
    lateinit var password : EditText
    lateinit var passwordCheck : EditText
    lateinit var register : Button

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)
        register = findViewById(R.id.register)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        passwordCheck = findViewById(R.id.passwordCheck)

        register.setOnClickListener{
            val email = username.toString().trim()
            val passwordString = password.toString()
            registerUser(email, passwordString)
        }
    }

    private fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                } else {
                    val errorMessage = task.exception?.message.toString()
                }
            }
    }
}