package com.example.tp3veilletechnologique
import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.tp3veilletechnologique.databinding.ActivityMapsBinding
import com.example.tp3veilletechnologique.parsers.ParseCSV
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.data.kml.KmlLayer

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val sharedPreferences = getSharedPreferences("KML", MODE_PRIVATE)

        binding.settingButton.setOnClickListener {
            val settings = Intent(this, SettingsActivity::class.java)
            startActivity(settings)
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Check if permission to access location is granted
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mMap.isMyLocationEnabled = true
            getLastLocation()
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
        mMap.uiSettings.isZoomControlsEnabled = true;
        addCustomPins()
        addKML()
    }

    private fun addCustomPins(){
        ParseCSV.parseParks(resources.openRawResource(R.raw.structrec))

        val parcs = ParseCSV.ListParks()
        addParksToCollection(parcs)
        for (park in parcs) {
            val marker = MarkerOptions()
                .position(LatLng(park.latitude, park.longitude))
                .title(park.name)
            mMap.addMarker(marker)
        }
    }
    private fun addKML(){
        val pistesCyclable = KmlLayer(mMap, R.raw.pistes, this)
        pistesCyclable.addLayerToMap()
    }

    private fun getLastLocation() {
        // Get last known location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
    }

   private fun addParksToCollection(parks: List<ParseCSV.Parc>){
        val batch = firebaseFirestore.batch()
        parks.forEach { module ->
            val documentReference = firebaseFirestore.collection("parks").document(module.id)
            val moduleMap = hashMapOf<String, String>()
            moduleMap.put("Name", module.name)
            moduleMap.put("Location", module.location)
            moduleMap.put("Longitude", module.longitude.toString())
            moduleMap.put("Latitude", module.latitude.toString())
            batch.set(documentReference, moduleMap)
        }
        batch.commit()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Parks added successfully!")
                } else {
                    Log.w(TAG, "Error adding parks to Firestore", task.exception)
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    mMap.isMyLocationEnabled = true
                    getLastLocation()
                }
            }
        }
    }
}











