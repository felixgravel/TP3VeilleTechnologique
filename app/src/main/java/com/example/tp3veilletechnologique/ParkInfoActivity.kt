package com.example.tp3veilletechnologique

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tp3veilletechnologique.parsers.ParseCSV
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.Date
import java.text.SimpleDateFormat
import java.util.Locale

class ParkInfoActivity: AppCompatActivity() {
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.park_info)

        val parkName = intent.getStringExtra("parkName")
        val location = intent.getStringExtra("parkLocation")
        val latitude = intent.getDoubleExtra("parkLatitude", 0.0)
        val longitude = intent.getDoubleExtra("parkLongitude", 0.0)
        val parkId = intent.getStringExtra("parkId").toString()

        findViewById<TextView>(R.id.ParkName).text = parkName
        findViewById<TextView>(R.id.Location).text = location
        findViewById<TextView>(R.id.Latitude).text = latitude.toString()
        findViewById<TextView>(R.id.Longitude).text = longitude.toString()

        val favorisButton = findViewById<Button>(R.id.FavorisButton)
        val commenterButton = findViewById<Button>(R.id.CommenterButton)
        val commentText = findViewById<EditText>(R.id.CommenterText)

        favorisButton.setOnClickListener {
            addFavorites(parkId)
        }

        commenterButton.setOnClickListener {
            val comment = commentText.text.toString().trim()
            if(comment.isNotEmpty()){
                addComment(parkId, comment)
            }
            else{
                Toast.makeText(this, "Veuillez écrire un commentaire!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
private fun addComment(parkId: String, comment: String){
    val user = auth.currentUser
    user?.let { firebaseUser ->
        val userEmail = firebaseUser.email
        userEmail?.let { email ->
            val commentairesCollection = firebaseFirestore.collection("commentaires")
            val timestamp = System.currentTimeMillis()

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val formattedDate = sdf.format(java.util.Date(timestamp))

            val commentData = hashMapOf(
                "parkId" to parkId,
                "userEmail" to email,
                "comment" to comment,
                "date" to formattedDate

            )
            commentairesCollection.add(commentData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Commentaire ajouter avec succès!", Toast.LENGTH_SHORT).show()
                    findViewById<EditText>(R.id.CommenterText).setText("")
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erreur lors de l'ajout de commentaire", Toast.LENGTH_SHORT).show()
                }
        }
}
}
    private fun addFavorites(parkId: String){
        val user = auth.currentUser
        user?.let { firebaseUser ->
            val userEmail = firebaseUser.email
            userEmail?.let { email ->
                firebaseFirestore.collection("favorites")
                    .whereEqualTo("userEmail", email)
                    .whereEqualTo("parkId", parkId)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            val favoritesCollection = firebaseFirestore.collection("favorites")
                            val favoriteData = hashMapOf(
                                "parkId" to parkId,
                                "userEmail" to email
                            )
                            favoritesCollection.add(favoriteData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Favori ajouter!", Toast.LENGTH_SHORT)
                                        .show()

                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(
                                        this,
                                        exception.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            Toast.makeText(
                                this,
                                "Vous avez deja ce parc dans vos favoris!",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            this,
                            exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }
}