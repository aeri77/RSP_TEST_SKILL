package com.example.rsptestskill.dialog

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import com.example.rsptestskill.R
import java.io.File

private const val FILE_NAME = "photo_profile_"
private const val TAG = "ProfilePhotoDialog"
@Suppress("SameParameterValue")
class ProfilePhotoDialog(private val appCompatActivity: AppCompatActivity) {
    private var dialog: Dialog = Dialog(appCompatActivity)
    private var btnGallery: ImageButton
    private var btnCamera: ImageButton
    private lateinit var filePhoto: File

    init {
        dialog.setContentView(R.layout.dialog_layout_profile)
        btnCamera = dialog.findViewById(R.id.btnCamera)
        btnGallery = dialog.findViewById(R.id.btnGallery)
        btnCamera.setOnClickListener {
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            filePhoto = getPhotoFile(FILE_NAME)

            val providerFile = FileProvider.getUriForFile(appCompatActivity,"com.example.rsptestskill.fileprovider", filePhoto)


            val sharedPreference = appCompatActivity.getSharedPreferences("CAMERA_PREFERENCE", Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.putString("image", providerFile.toString())
            editor.apply()

            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)
            if (takePhotoIntent.resolveActivity(appCompatActivity.packageManager) != null){
                appCompatActivity.startActivityForResult(takePhotoIntent, 102)
            }else {
                Toast.makeText(appCompatActivity,"Camera could not open", Toast.LENGTH_SHORT).show()
            }
        }
        btnGallery.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (appCompatActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    appCompatActivity.requestPermissions(permissions, 1001)
                } else {
                    chooseImageGallery()
                }
            } else {
                chooseImageGallery()
            }
        }
    }

    private fun chooseImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        appCompatActivity.startActivityForResult(intent, 100)
    }

    fun showDialog(){
        dialog.show()
    }

    fun getBtnGallery(): ImageButton {
        return btnGallery
    }

    fun getBtnCamera(): ImageButton {
        return btnCamera
    }

    fun dismissDialog(){
        dialog.dismiss()
    }


    private fun getPhotoFile(fileName: String): File {
        val directoryStorage = appCompatActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directoryStorage)
    }
}