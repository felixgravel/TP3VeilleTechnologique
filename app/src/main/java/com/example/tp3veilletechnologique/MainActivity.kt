package com.example.tp3veilletechnologique

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.view.View
import android.webkit.WebView.FindListener
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
class MainActivity : AppCompatActivity() {
    val firebaseAuth = FirebaseAuth.getInstance()
    lateinit var username : EditText
    lateinit var password : EditText
    lateinit var connexion : Button
    lateinit var register : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        connexion = findViewById(R.id.connexion)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        connexion.setOnClickListener{
            val email = username.toString().trim()
            val motDePasse = password.toString()
            signInUser(email, motDePasse)
        }

        register =findViewById(R.id.inscrire)
        register.setOnClickListener{
            setContentView(R.layout.register_layout)
        }
    }

    private fun signInUser(email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            task -> if(task.isSuccessful){
        }
        else {
            if(task.exception?.message.toString().contains("FirebaseAuthUserNotFoundException")){
                setContentView(R.layout.register_layout)
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
