package com.example.tp3veilletechnologique

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import com.example.tp3veilletechnologique.databinding.LoginLayoutBinding
import com.example.tp3veilletechnologique.databinding.RegisterLayoutBinding

class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: RegisterLayoutBinding
    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = RegisterLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.register.setOnClickListener{
            val email = binding.username.text.toString()
            val passwordString = binding.password.text.toString()
            val passwordCheck = binding.passwordCheck.text.toString()

            if(email.isNotEmpty() && passwordCheck.isNotEmpty() && passwordString.isNotEmpty()) {
                if (passwordString == passwordCheck) {
                    registerUser(email, passwordString)
                } else {
                    Toast.makeText(
                        this,
                        "Les mots de passes ne sont pas identiques",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    val errorMessage = task.exception?.message.toString()
                }
            }
    }
}