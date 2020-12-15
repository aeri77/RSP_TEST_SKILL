package com.example.rsptestskill

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*


private const val TAG = "MainActivity"
// declare a global variable FusedLocationProviderClient
private lateinit var fusedLocationClient: FusedLocationProviderClient

// in onCreate() initialize FusedLocationProviderClient


// globally declare LocationRequest
private lateinit var locationRequest: LocationRequest

// globally declare LocationCallback
private lateinit var locationCallback: LocationCallback


/**
 * call this method in onCreate
 * onLocationResult call when location is changed
 */
class   MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //location initialize
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()
        locationCallback = LocationCallback()
        //
        val database = baseContext.openOrCreateDatabase("login_story", Context.MODE_PRIVATE, null)
        var sql = "CREATE TABLE IF NOT EXISTS login_story(_id INTEGER PRIMARY KEY NOT NULL, email TEXT, count_login INTEGER)"
        Log.d(TAG, "onCreate: sql is $sql")
        database.execSQL(sql)
        sql = "INSERT INTO login_story(email, count_login) VALUES('tim@hisemail.com', 1)"
        Log.d(TAG, "onCreate: sql is $sql")
        database.execSQL(sql)
        val values = ContentValues().apply {
            put("email", "fred@nurk.com")
            put("count_login", 12)
        }
        val generatedId = database.insert("login_story", null, values)
        Log.d(TAG, "onCreate: record added with id $generatedId")
        //
//
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (this as Activity?)!!,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                101
            )
        }
//
        startLocationUpdates()
        getLocationUpdates()

    }

    private fun getLocationUpdates()
    {
        locationRequest.interval = 50000
        locationRequest.fastestInterval = 50000
        locationRequest.smallestDisplacement = 170f // 170 m = 0.1 mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {
                    // get latest location
                    val location =
                        locationResult.lastLocation
                    // use your location object
                    // get latitude , longitude and other info from this
                    tvLatitude.text = "Latitude : ${location.latitude}"
                    tvLongitude.text = "Longitude : ${location.longitude}"
                }


            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (this as Activity?)!!,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                101
            )
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    // stop location updates
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // stop receiving location update when activity not visible/foreground
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    // start receiving location update when activity  visible/foreground
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }
}