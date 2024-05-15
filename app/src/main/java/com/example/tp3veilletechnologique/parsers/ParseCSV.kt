package com.example.tp3veilletechnologique.parsers

import com.google.firebase.firestore.FirebaseFirestore
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object ParseCSV {
    data class Parc(val id: String, val name: String, val longitude: Double, val latitude: Double, val location: String){}
    private var parks: MutableList<Parc> = mutableListOf()
    private fun parseParkLine(line: String): Parc? {
        val parts = line.split('|')
        if (parts.size < 6) return null

        val id = parts[0]
        val name = parts[1]
        val location = parts[2]
        val longitude = parts[4].toDoubleOrNull() ?: 0.0
        val latitude = parts[5].toDoubleOrNull() ?: 0.0

        return Parc(id, name, longitude, latitude, location)
    }

    fun parseParks(filePath: InputStream) {
        val reader = BufferedReader(InputStreamReader(filePath))
        var index = 0
        for (record in reader.lineSequence()) {
            if (index > 0) {
                val park = parseParkLine(record)
                if (park != null) {
                    parks.add(park)
                }
            }
            index++
        }
    }



    fun ListParks(): List<Parc> {
        return parks.toList()
    }
}