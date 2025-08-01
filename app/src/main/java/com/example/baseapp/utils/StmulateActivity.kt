package com.example.baseapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.baseapp.utils.Constants.clickWithDebounce
import com.example.baseapp.utils.EventNames.SIMULATE_CALLMAIN
import com.example.fakecall.databinding.ActivityStmulateBinding
import android.widget.Toast
import android.util.Log
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.baseapp.MainActivity
import com.example.baseapp.Application.MyApplication.Companion.onstop

class StmulateActivity : AppCompatActivity() {
    lateinit var binding:ActivityStmulateBinding
    var imguri="alexandra"
    private var isImageChooserOpen = false
    private val PERMISSION_REQUEST_STORAGE = 1001
    private val PICK_IMAGE_REQUEST = 1002
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityStmulateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.stimcallImg.setOnClickListener {

            checkGalleryPermissionAndOpenChooser()
        }
        binding.stmbackbtn.clickWithDebounce {
            finish()
        }
        binding.simulatebtn.clickWithDebounce {

            if (binding.editnumber.text.toString().isNotEmpty() && binding.editname.text.toString().isNotEmpty()&&imguri!="alexandra") {
                FirebaseCustomEvents(this).createFirebaseEvents(
                    SIMULATE_CALLMAIN,
                    "true"
                )

                PrefUtil(this).setString("profile", binding.editname.text.toString())
                PrefUtil(this).setInt("profile_int", 1)


                var intent = Intent(this, MainActivity::class.java)
                intent.putExtra("customprofileuri",imguri)
                intent.putExtra("customprofile",binding.editname.text.toString())
                intent.action="customcall"
                startActivity(intent)

                hideKeyboard()
                binding.editname.text.clear()
                binding.editnumber.text.clear()
                count++
                finish()







            } else {
                Toast.makeText(this, "Enter Name,Number & Image", Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun hideKeyboard() {
        val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = this.currentFocus
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
    fun openImageChooser() {
        if (!isImageChooserOpen) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
            isImageChooserOpen = true
        }
    }
    fun checkGalleryPermissionAndOpenChooser() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            openImageChooser()
        } else {
            requestPermissions(arrayOf(permission), PERMISSION_REQUEST_STORAGE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        isImageChooserOpen = false
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {

            try {
                val imageUri: Uri? = data.data
                if (imageUri != null) {
                    Glide.with(this)
                        .load(imageUri)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding.stimcallImg)
                    imguri=imageUri.toString()
                    PrefUtil(this).setString("stimulate_img", imageUri.toString())
                    onstop=true

                }
            } catch (e: Exception) {
                Log.e("ImageLoadingError", "Error loading image: ${e.message}")
            }
        }
    }
    companion object {
        var count = 1

    }
}