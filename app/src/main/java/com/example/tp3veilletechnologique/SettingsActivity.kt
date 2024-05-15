package com.example.tp3veilletechnologique

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tp3veilletechnologique.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val currentUser = auth.currentUser
        currentUser?.email.let {email ->
            binding.nomUtilisateur.text = email
        }
        val sharedPreferences = getSharedPreferences("KML", MODE_PRIVATE)

        if(sharedPreferences.contains("ADDKML")){
            val isKML = sharedPreferences.getBoolean("ADDKML", false)
            if(isKML){
                binding.pistesCyclables.isActivated
            }
        }

        binding.pistesCyclables.setOnClickListener {
            with(sharedPreferences.edit()){
                putBoolean("ADDKML", true )
                apply()
            }
        }

        binding.deconnexion.setOnClickListener {
            val sharedPreferences = getSharedPreferences("LOGIN", MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                remove("EMAIL")
                remove("MDP")
                apply()
            }
            val login = Intent(this, LoginActivity::class.java)
            startActivity(login)
            finish()
        }
    }
}