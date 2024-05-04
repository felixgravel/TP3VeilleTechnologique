package com.example.tp3veilletechnologique

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.view.View
import android.webkit.WebView.FindListener
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var username : EditText
    lateinit var password : EditText
    lateinit var passwordCheck : EditText
    lateinit var connexion : Button
    lateinit var register : Button
    lateinit var registrationLayout : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        register =findViewById(R.id.inscrire)
        register.setOnClickListener{
            setContentView(R.layout.register_layout)
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
