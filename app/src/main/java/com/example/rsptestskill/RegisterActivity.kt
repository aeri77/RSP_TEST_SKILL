package com.example.rsptestskill

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.rsptestskill.dialog.ProfilePhotoDialog
import com.example.rsptestskill.room.LoginStory
import com.example.rsptestskill.room.LoginStoryApplication
import com.example.rsptestskill.room.LoginStoryViewModel
import com.example.rsptestskill.room.LoginStoryViewModelFactory
import com.example.rsptestskill.utils.Constants
import kotlinx.android.synthetic.main.activity_register.*


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
        startActivityForResult(intent, Constants.REQ_CODE_100)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            Constants.REQ_CODE_1001 -> {
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
        if(requestCode == Constants.REQ_CODE_100 && resultCode == Activity.RESULT_OK){
            Glide
                .with(this)
                .load(data?.data)
                .centerCrop()
                .override(960, 1280)
                .into(ivRegisterProfile)
        }
        if(requestCode == Constants.REQ_CODE_102 && resultCode == Activity.RESULT_OK){
            val sharedPreference =  getSharedPreferences(Constants.CAMERA_PREFERENCE, Context.MODE_PRIVATE)
            Glide
                .with(this)
                .load(sharedPreference.getString(Constants.KEY_IMAGE, null)!!.toUri())
                .centerCrop()
                .override(960, 1280)
                .into(ivRegisterProfile)
        }
    }
}
