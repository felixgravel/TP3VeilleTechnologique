package com.example.tp3veilletechnologique.parsers

import android.util.Log
import java.io.File
class ParseCSV {
    data class Module(val id: String, val name: String, val longitude: Double, val latitude: Double, val location: String){}
    val filePath = "sampledata/structrec.csv"
    public fun ParseParks(): List<Module> {
        val file = File(filePath)
        if(file.exists()){
            return emptyList();
        }
        val csvData = file.readText();
        val modules = csvData.lines().drop(1).map { line ->
            val parts = line.split("|")
            if (parts.size < 6) return@map null
            Module(
                id = parts[0],
                name = parts[1],
                location = parts[2],
                longitude = parts[4].toDouble(),
                latitude = parts[5].toDouble()
                )
        }.filterNotNull()
//        Log.d(TAG, "")
        return modules;
    }
}