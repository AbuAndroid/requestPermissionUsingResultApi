package com.example.requestprmissionsresultapi

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.requestprmissionsresultapi.databinding.ActivityMainBinding
import java.io.File
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    private val bind by viewBinding(ActivityMainBinding::inflate)
    var imageUri: Uri? = null

    /* requesting only single  camera permissions */
    val requestPermissionLauncher by lazy {
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                openresult()
            }
        }
    }

    /* requesting multiple permissions*/
//    val requestMultiplePermissions by lazy {
//        registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            permissions.entries.forEach {
//                val permissionName = it.key
//                val isGranted = it.value
//                if (isGranted) {
//                    toastMessage()
//                } else {
//
//                }
//            }
//        }
//    }
    /* request to open camera app and  click picture */

    val requestTakePicture by lazy {
        registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) {
            if (it) {
//                setPictureToPictureView(imageUri)
                bind.uiIvUserPicture.setImageURI(imageUri)
                Log.d("image", "" + imageUri)
            }
        }
    }
    /* request to write internal storage and get picture fgsfdgs */

    val requestGetPicture by lazy {
        registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                if (it != null) {
                    bind.uiIvUserPicture.setImageURI(it)
                }
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher
//        requestMultiplePermissions
        requestTakePicture
        requestGetPicture
        imageUri = createImageUri()
        setUpListeners()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpListeners() {

        // button fof check single camera permission
        bind.uiBtCheckPermissions.setOnClickListener {
            Log.d("permission", shouldShowRequestPermissionRationale(Manifest.permission.CAMERA).toString())

            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        // button for check multiple permissions
//        bind.uiBtCheckMultiplePermissions.setOnClickListener {
//            requestMultiplePermissions.launch(
//                arrayOf(
//
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.CAMERA jkjjjlkjkljkljklvczxvc  gfgagag
//                )
//            )
//        }
        //button for take open camera app and take picture
        bind.uiBtTakePicture.setOnClickListener {
            Log.d("permission", shouldShowRequestPermissionRationale(Manifest.permission.CAMERA).toString())
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package",packageName,null)
                intent.setData(uri)
                startActivity(intent)
                Toast.makeText(this,"camera permission required...",Toast.LENGTH_LONG).show()
                Log.d("permission", shouldShowRequestPermissionRationale(Manifest.permission.CAMERA).toString())

            }else{
                Log.d("permission", shouldShowRequestPermissionRationale(Manifest.permission.CAMERA).toString())

                startCamera()
            }

        }
        //button for read photo in internal storage
        bind.uiBtGetPhotoFromGallery.setOnClickListener {
            requestGetPicture.launch("image/*")
        }
    }

    private fun startCamera() {
        requestTakePicture.launch(imageUri)
    }

    private fun openresult() {
        Toast.makeText(this, "permission granted..", Toast.LENGTH_LONG).show()
    }

    private fun toastMessage() {
        Toast.makeText(this, "please check multiple permissions...", Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createImageUri(): Uri? {
        val image = File(applicationContext.filesDir, "${LocalDateTime.now()}.png")
        Log.d("image", "" + image)
        return FileProvider.getUriForFile(
            applicationContext,
            "com.example.requestprmissionsresultapi.fileprovider",
            image
        )
    }
}
