package com.example.rsptestskill

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
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
import com.example.rsptestskill.utils.HelperUtils
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*


/**
 * call this method in onCreate
 * onLocationResult call when location is changed
 */
class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val loginStoryViewModel: LoginStoryViewModel by viewModels {
        LoginStoryViewModelFactory((application as LoginStoryApplication).repository)
    }
    private val listLoginStory = ArrayList<LoginStory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //uncomment for delete all inside table
        // loginStoryViewModel.delete()
        onEvent()
    }

    private fun onEvent() {
        HelperUtils.checkPermission(this)
        initializeLocation()
        startLocationUpdates()
        getLocationUpdates()
        loginStoryViewModel.allLoginStory.observe(this) {}
        buttonEvent()
    }

    private fun initializeLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()
        locationCallback = LocationCallback()
    }





    private fun buttonEvent() {
        btnLogin.setOnClickListener {
            if (etLoginEmail.text.isBlank()) {
                etLoginEmail.error = "Email tidak boleh kosong"
            } else {
                loginStoryViewModel.allLoginStory.observe(this) { loginStory ->
                    // Update the cached copy of the words in the adapter.
                    loginStory.map {
                        listLoginStory.add(it)
                    }
                }
                var updatedLoginStory: LoginStory? = null
                listLoginStory.filter {
                    it.email == etLoginEmail.text.toString()
                }.forEach {
                    updatedLoginStory = LoginStory(it.id, it.email, it.username, it.loginCount + 1)
                }
                if(updatedLoginStory == null){
                    Toast.makeText(
                        this@MainActivity,
                        "Tidak ada Database, Register untuk membuat database",
                        Toast.LENGTH_SHORT)
                        .show()
                }else {
                    if(updatedLoginStory!!.email.isBlank()){
                        Toast.makeText(
                            this@MainActivity,
                            "Email belum terdaftar",
                            Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        loginStoryViewModel.update(updatedLoginStory!!)
                        Toast.makeText(
                            this@MainActivity,
                            "Anda telah login menggunakan ${updatedLoginStory!!.username} : ${updatedLoginStory!!.email}  sebanyak ${updatedLoginStory!!.loginCount}",
                            Toast.LENGTH_SHORT)
                            .show()
                        listLoginStory.clear()
                    }
                }

            }
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }


    private fun getLocationUpdates() {
        locationRequest.interval = 50000
        locationRequest.fastestInterval = 50000
        locationRequest.smallestDisplacement = 170f // 170 m = 0.1 mile
        locationRequest.priority =
            LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
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
        HelperUtils.checkPermission(this)
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