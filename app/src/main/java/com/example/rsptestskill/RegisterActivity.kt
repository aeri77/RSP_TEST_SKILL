package com.example.rsptestskill

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.rsptestskill.dialog.ProfilePhotoDialog
import com.example.rsptestskill.room.LoginStory
import com.example.rsptestskill.room.LoginStoryApplication
import com.example.rsptestskill.room.LoginStoryViewModel
import com.example.rsptestskill.room.LoginStoryViewModelFactory
import kotlinx.android.synthetic.main.activity_register.*
import java.io.File


class RegisterActivity : AppCompatActivity(){

    private val loginStoryViewModel: LoginStoryViewModel by viewModels {
        LoginStoryViewModelFactory((application as LoginStoryApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        onClickRegister()
    }

    private fun onClickRegister(){
        btnRegisterApply.setOnClickListener {
            var isEmpty = false
            var isRegistered = false
            val listLoginStory = ArrayList<LoginStory>()
            loginStoryViewModel.allLoginStory.observe(this) { loginStory ->
                if (loginStory.isEmpty()){
                    isEmpty = loginStory.isEmpty()
                } else {
                    loginStory.map{
                        listLoginStory.add(it)
                    }
                }
            }
            listLoginStory.map {
                if (it.email == etRegisterEmail.text.toString()){
                    isRegistered = true
                    Toast.makeText(this,"email has already register", Toast.LENGTH_LONG).show()
                }
            }
            if (!isRegistered){
                loginStoryViewModel.insert(LoginStory( null, etRegisterEmail.text.toString(), etRegisterUsername.text.toString(), 0))
            }
        }
        onImagePick()
    }

    private fun onImagePick() {

        rlProfileWrapper.setOnClickListener {
            ProfilePhotoDialog(this).showDialog()
        }
    }

    private fun chooseImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1001 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseImageGallery()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            Log.d(TAG, "onresult Gallery ${data?.data}")
        }
        if(requestCode == 102 && resultCode == Activity.RESULT_OK){
            val sharedPreference =  getSharedPreferences("CAMERA_PREFERENCE", Context.MODE_PRIVATE)
            Log.d(TAG, "onresult Camera ${sharedPreference.getString("image", null)}")
        }
    }
}

private const val TAG = "RegisterActivity"