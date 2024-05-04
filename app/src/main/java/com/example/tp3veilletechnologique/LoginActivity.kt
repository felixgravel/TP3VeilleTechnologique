package com.example.tp3veilletechnologique

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    val firebaseAuth = FirebaseAuth.getInstance()
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var connexion: Button
    lateinit var register: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        connexion = findViewById(R.id.connexion)
        register = findViewById(R.id.inscrire)  // Assuming your register button has this ID

        connexion.setOnClickListener {
            Log.d("LoginActivity", "Connexion button clicked!")
            val email = username.text.toString().trim()
            val motDePasse = password.text.toString()
            signInUser(email, motDePasse)
        }

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    private fun signInUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = task.exception?.message.toString()
                    Toast.makeText(this, "Login Failed: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
