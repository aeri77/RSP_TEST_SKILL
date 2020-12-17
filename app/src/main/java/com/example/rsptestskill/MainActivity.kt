package com.example.rsptestskill

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.rsptestskill.room.LoginStory
import com.example.rsptestskill.room.LoginStoryApplication
import com.example.rsptestskill.room.LoginStoryViewModel
import com.example.rsptestskill.room.LoginStoryViewModelFactory
import com.example.rsptestskill.utils.Constants
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*




/**
 * call this method in onCreate
 * onLocationResult call when location is changed
 */
class   MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val loginStoryViewModel: LoginStoryViewModel by viewModels {
        LoginStoryViewModelFactory((application as LoginStoryApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //uncomment for delete all inside table
        // loginStoryViewModel.delete()

        loginStoryViewModel.allLoginStory.observe(this) { loginStory ->
            // Update the cached copy of the words in the adapter.
//            loginStory.let { Log.d(TAG, "oncreate loginstory ${it[1]}") }
        }
        //location initialize
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()
        locationCallback = LocationCallback()
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
                Constants.REQ_CODE_101
            )
        }
//
        startLocationUpdates()
        getLocationUpdates()
        btnLogin.setOnClickListener {
            val listLoginStory = ArrayList<LoginStory>()
            if(etLoginEmail.text.isBlank()){
                Toast.makeText(this,"Email kosong", Toast.LENGTH_LONG).show()
            }else{
                loginStoryViewModel.allLoginStory.observe(this) { loginStory ->
                    // Update the cached copy of the words in the adapter.
                    loginStory.map {
                        listLoginStory.add(it)
                    }
                    loginStory.let {
                    }
                }
                listLoginStory.map {
                    if(it.email == etLoginEmail.text.toString()){
                        loginStoryViewModel.update(LoginStory(it.id, it.email, it.username, it.loginCount + 1))
                    }
                }
            }
        }
        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
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
                Constants.REQ_CODE_101
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

    fun getLocationPermissions(){

    }
}