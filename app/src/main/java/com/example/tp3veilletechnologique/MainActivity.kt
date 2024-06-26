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
class MainActivity : AppCompatActivity() {
    private lateinit var binding: LoginLayoutBinding
    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.connexion.setOnClickListener{
            val email = binding.username.text.toString()
            val motDePasse = binding.password.text.toString()

            if(email.isNotEmpty() && motDePasse.isNotEmpty()) {
                signInUser(email, motDePasse)
            }else{
                Toast.makeText(this, "Veillez entrer une addresse courriel et un mot de passe", Toast.LENGTH_SHORT).show()
            }
        }

        val register = Intent(this@MainActivity, RegisterActivity::class.java)

        binding.inscrire.setOnClickListener{
            startActivity(register)
        }
    }

    private fun signInUser(email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            task -> if(task.isSuccessful){
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

        }
        else {
            Toast.makeText(this, "Login not successful", Toast.LENGTH_SHORT).show()
            if(task.exception?.message.toString().contains("FirebaseAuthUserNotFoundException")){
                Toast.makeText(this, "Login not successful", Toast.LENGTH_SHORT).show()
            }
        }
        }
    }
}

/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}



@Preview(showBackground = false)
@Composable
fun GreetingPreview() {
    TP3VeilleTechnologiqueTheme {
        Greeting("papa")
    }
}*/
