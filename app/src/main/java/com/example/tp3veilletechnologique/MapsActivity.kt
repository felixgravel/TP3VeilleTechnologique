package com.example.tp3veilletechnologique
import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.data.kml.KmlLayer

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var parksRecyclerView: RecyclerView
    private lateinit var parksRecyclerViewAdapter: ParksRecyclerViewAdapter
    private lateinit var bottomSheet: LinearLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

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

        setupRecyclerView()
        loadParks()
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
        mMap.setOnMarkerClickListener(this)
        addCustomPins()
//        addKML()
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

    private fun setupRecyclerView() {
        parksRecyclerView = findViewById(R.id.mRecyclerView)
        parksRecyclerView.layoutManager = LinearLayoutManager(this)
    }

//    private fun loadParks() {
//        ParseCSV.parseParks(resources.openRawResource(R.raw.structrec))
//        val parcs = ParseCSV.ListParks()
//        Log.d(TAG, "Number of parks: ${parcs.size}")
//        val parksAdapter = ParksRecyclerViewAdapter(parcs) { parc: ParseCSV.Parc -> }
//        parksRecyclerView.adapter = parksAdapter
//    }

    private fun loadParks(clickedPark: ParseCSV.Parc? = null) {
        ParseCSV.parseParks(resources.openRawResource(R.raw.structrec))

        val parcs = ParseCSV.ListParks()

        Log.d(TAG, "Number of parks: ${parcs.size}")

        // Create a new list to store the modified order of parks
        val modifiedParcs = mutableListOf<ParseCSV.Parc>()

        // Add the clicked park at the top if it is not null
        clickedPark?.let {
            if (!parcs.contains(clickedPark)) {
                modifiedParcs.add(clickedPark)
            }
        }

        // Add all other parks to the list
        modifiedParcs.addAll(parcs)

        // Update RecyclerView with the modified parks list
        val parksAdapter = ParksRecyclerViewAdapter(modifiedParcs) { parc: ParseCSV.Parc -> }
        parksRecyclerView.adapter = parksAdapter
        parksAdapter.notifyDataSetChanged()
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

//    override fun onMarkerClick(p0: Marker): Boolean {
//        if(p0.title != null){
//            bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//            return false
//        }
//        return true
//    }
override fun onMarkerClick(p0: Marker): Boolean {
    if (p0.title != null) {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        // Find the clicked park from the list
        val clickedPark = ParseCSV.ListParks().find { it.name == p0.title }
        // Load parks with the clicked park at the top
        loadParks(clickedPark)
        return false
    }
    return true
}

}











