package com.example.tp3veilletechnologique

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.view.View
import android.webkit.WebView.FindListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tp3veilletechnologique.databinding.LoginLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginLayoutBinding
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("LOGIN", MODE_PRIVATE)

        if(sharedPreferences.contains("EMAIL")){
            val userEmail = sharedPreferences.getString("EMAIL", "")
            val password = sharedPreferences.getString("MDP", "")


            if (userEmail != null) {
                if (password != null) {
                    signInUser(userEmail, password)
                }
            }
        }

        binding.connexion.setOnClickListener{
            val email = binding.username.text.toString()
            val motDePasse = binding.password.text.toString()

            if(email.isNotEmpty() && motDePasse.isNotEmpty()) {
                signInUser(email, motDePasse)
            }else{
                Toast.makeText(this, "Veillez entrer une addresse courriel et un mot de passe", Toast.LENGTH_SHORT).show()
            }
        }

        val register = Intent(this@LoginActivity, RegisterActivity::class.java)

        binding.inscrire.setOnClickListener{
            startActivity(register)
        }
    }

    private fun signInUser(email: String, password: String){
        val maps = Intent(this@LoginActivity, MapsActivity::class.java)
        binding.connexion.isEnabled = false
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                task -> if(task.isSuccessful){
                    if(binding.remember.isChecked){
                        val sharedPreferences = getSharedPreferences("LOGIN", MODE_PRIVATE)
                        with(sharedPreferences.edit()){
                            putString("EMAIL", email)
                            putString("MDP", password)
                            apply()
                        }
                    }
                    startActivity(maps)
                    finish()
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "Login not successful", Toast.LENGTH_SHORT).show()
            if(task.exception?.message.toString().contains("FirebaseAuthUserNotFoundException")){
                Toast.makeText(this, "Login not successful", Toast.LENGTH_SHORT).show()
            }
            binding.connexion.isEnabled = true
        }
        }
    }
}