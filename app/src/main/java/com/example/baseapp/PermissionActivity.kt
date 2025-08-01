package com.example.baseapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.baseapp.SplashScreen.Companion.version
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.inAppPurchases.InAppPurchases
import com.example.baseapp.onbording.onbording
import com.example.baseapp.utils.FirebaseCustomEvents

import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivityPermissionBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PermissionActivity : AppCompatActivity() {
    lateinit var permissionbind: ActivityPermissionBinding
    val prefutil: PrefUtil = PrefUtil(this)
    var local = false
    var granted = false
    var firsttime = false
    var permissionsGranted = false
    var cameracount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        cameracount = PrefUtil(this).getInt("cameracount", 0)
        permissionbind = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(permissionbind.root)




        try {
            val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
                val window = this.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.white)
                window.navigationBarColor = resources.getColor(R.color.white, theme)

                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                    isAppearanceLightNavigationBars = true
                }
            } else {
                val window = this.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.white)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                window.navigationBarColor = resources.getColor(R.color.white, theme)

                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                }
            }

        } catch (e: Exception) {
        }

        if (Settings.canDrawOverlays(this)) {
            permissionbind.projection.isChecked = true
        }
        if (checkCameraPermission()) {
            permissionbind.camera.isChecked = true
            permissionbind.camera.isClickable = false

        }
        if (checkAndRequestWriteSettingsPermission()) {
            permissionbind.Write.isChecked = true
            permissionbind.Write.isClickable = false

        }

        permissionbind.camera.setOnClickListener {
            cameracount++
            if (!checkCameraPermission()) {
                requestCameraPermission()

            } else {

                return@setOnClickListener
            }
        }
        permissionbind.Write.setOnClickListener {

            if (!checkAndRequestWriteSettingsPermission()) {

                val intent = Intent(
                    Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:${this.packageName}")
                )
                startActivityForResult(intent, Write_REQ_CODE)
            } else {


                return@setOnClickListener
            }
        }
        permissionbind.projection.setOnClickListener {
            try {

                if (!Settings.canDrawOverlays(this)) {
                    // If not granted, request the permission
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${this.packageName}")
                    )
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE)
                } else {
                    // Permission already granted or running on a lower Android version
                    permissionbind.projection.isChecked = true
                    permissionbind.projection.isClickable = false

                    return@setOnClickListener
//                val intent = Intent(requireContext(), CameraActivity::class.java)
//                startActivity(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        permissionbind.permissionbtn.clickWithDebounce {
            FirebaseCustomEvents(this).createFirebaseEvents(
                "Permission_Granted_btn_$version",
                "true"
            )
            AdsManager.loadInterstitial(this)
            PrefUtil(this).setBool("Allowed", true)

//    permissioGrantedDialog()
            permissionbind.plasewait.visibility = View.VISIBLE
            permissionbind.animationView.visibility = View.VISIBLE
            permissionbind.permissionbtn.visibility = View.INVISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                // Start MainMenuActivity
                val intent = Intent(this, InAppPurchases::class.java)
                intent.putExtra("user", "from_splash")
                startActivity(intent)
                finish()
            }, 5000)
        }


    }


    fun View.clickWithDebounce(debounceTime: Long = 400L, action: () -> Unit) {
        this.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime: Long = 0

            override fun onClick(v: View) {
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
                else action()

                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }


    override fun onResume() {
        super.onResume()


        Log.d("TestTag", "inside OnResume")
        if (checkCameraPermission()) {
            Log.d("TestTag", "inside Camera Onresume")

            permissionbind.camera.isClickable = false
            permissionbind.camera.isChecked = true
        } else if (Settings.System.canWrite(this)) {
            permissionbind.Write.isChecked = true
            permissionbind.Write.isClickable = false

        } else if (!Settings.System.canWrite(this)) {
            permissionbind.Write.isChecked = false
        }

        if (checkCameraPermission() && checkAndRequestWriteSettingsPermission()) {
            PrefUtil(this).setBool("Allowed", true)
            Log.d("TestTag", "inside permisison granted")

//                permissioGrantedDialog()
//                AdsManager.loadInterstitial(this)
            permissionbind.permissionbtn.visibility = View.VISIBLE

        } else {

        }
    }


    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        if (Settings.System.canWrite(this)) {
            permissionbind.Write.isClickable = false
            permissionbind.Write.isChecked = true
//            Toast.makeText(this, "Overlay Granted", Toast.LENGTH_SHORT).show()

        } else if (checkCameraPermission()) {
            permissionbind.camera.isClickable = false
            permissionbind.camera.isChecked = true
        } else if (!Settings.System.canWrite(this)) {
            permissionbind.Write.isChecked = false
        } else {

        }
    }

    @SuppressLint("SuspiciousIndentation")


    private fun checkCameraPermission(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        )
        return cameraPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.CAMERA,
            ),
            CAMERA_PERMISSION_CODE
        )
    }


    private fun permissioGrantedDialog() {
        // Check if the activity is still in a valid state
        if (!isFinishing && !isDestroyed) {
            val viewGroup: ViewGroup = findViewById(android.R.id.content)

            val dialogView: View = LayoutInflater.from(this)
                .inflate(R.layout.permissiondialog, viewGroup, false)

            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView)
            val alertDialog = builder.create()
            alertDialog.setCancelable(false)

            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            alertDialog.show()

//            val exitLabelNo = dialogView.findViewById<TextView>(R.id.exit_label_no)
//            exitLabelNo.setOnClickListener {
//                alertDialog.dismiss()
//            }

            val exitLabelYes = dialogView.findViewById<Button>(R.id.btn_done)
            exitLabelYes.setOnClickListener {
                AdsManager.loadInterstitial(this)
                permissionbind.plasewait.visibility = View.VISIBLE
                permissionbind.animationView.visibility = View.VISIBLE
                permissionbind.permissionbtn.visibility = View.INVISIBLE
                alertDialog.dismiss()
                Handler(Looper.getMainLooper()).postDelayed({
                    // Start MainMenuActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 5000)


            }
        }
    }

    private fun checkAndRequestWriteSettingsPermission(): Boolean {
        return if (Settings.System.canWrite(this)) {
            permissionbind.Write.isClickable = false
            permissionbind.Write.isChecked = true

            true
        } else {
            permissionbind.Write.isChecked = false
            false
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                permissionbind.camera.isChecked = true
                permissionbind.camera.isClickable = false

            } else {
                // Permission denied
                permissionbind.camera.isChecked = false
                if (cameracount > 2) {
                    PrefUtil(this).setInt("cameracount", 2)
                    showSettingDialog()
                }

            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun shouldShowSettings() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CAMERA
            )

        ) {
            showSettingDialog()
        }
    }

    fun showSettingDialog() {
        try {
            val exitDialog = MaterialAlertDialogBuilder(this@PermissionActivity)
                .setTitle("permission_Required")
                .setMessage("Permission Requires to Proceed with app")
                .setNegativeButton(
                    "Cancel"
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(
                    "Open Settings"
                ) { dialog, _ ->
                    dialog.dismiss()
                    openSettings()
                }
                .create()

            // Set colors for both buttons (Open Settings and Cancel) in a single OnShowListener
            exitDialog.setOnShowListener {
                val positiveButton = exitDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                val negativeButton = exitDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)

                positiveButton.setTextColor(ContextCompat.getColor(this, R.color.black))
                negativeButton.setTextColor(ContextCompat.getColor(this, R.color.black))
            }

            exitDialog.show()
        } catch (e: Exception) {
        }

    }


    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    companion object {
        //PERMISSION request constant, assign any value
        const val CAMERA_PERMISSION_CODE = 101
        const val OVERLAY_PERMISSION_REQ_CODE = 123
        const val Write_REQ_CODE = 123

        const val TAG = "PERMISSION_TAG"
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cameracount == 1) {
            PrefUtil(this).setInt("cameracount", 1)

        }
    }

}





