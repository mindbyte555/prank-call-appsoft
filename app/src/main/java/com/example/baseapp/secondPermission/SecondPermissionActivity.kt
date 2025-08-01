package com.example.baseapp.secondPermission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.baseapp.Application.MyApplication.Companion.noadshow
import com.example.baseapp.PermissionActivity
import com.example.baseapp.alarmManagers.VideoAlarmReciver
import com.example.baseapp.ringtones.RingtoneActivity
import com.example.baseapp.schedule.ScheduleActivity
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivityPermissionBinding
import com.example.fakecall.databinding.ActivitySecondPermissionBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SecondPermissionActivity : AppCompatActivity() {
    lateinit var permissionbind:ActivitySecondPermissionBinding
    var intentperm=""
    var cameracount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        permissionbind = ActivitySecondPermissionBinding.inflate(layoutInflater)
        setContentView(permissionbind.root)
        intentperm=intent.getStringExtra("key")?:""
        cameracount = PrefUtil(this).getInt("cameracount", 0)
        if(intentperm=="chat"|| intentperm=="cameravideo"||intentperm=="customvideo"){
            permissionbind.cameraConstraint.visibility= View.VISIBLE

        }
        else if(intentperm=="audiocall"){
            permissionbind.writeConstraint.visibility= View.VISIBLE

        }
        else if(intentperm=="overlay"){
            permissionbind.projectionConstraint.visibility= View.VISIBLE

        }
        permissionbind.camera.setOnClickListener {
            permissionbind.camera.isChecked=false
            cameracount++
            if (!checkCameraPermission()) {
                requestCameraPermission()

            } else {

                return@setOnClickListener
            }
        }
        permissionbind.Write.setOnClickListener {
            permissionbind.Write.isChecked = false
            if (!checkAndRequestWriteSettingsPermission()) {
                noadshow =false

                val intent = Intent(
                    Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:${this.packageName}")
                )
                startActivityForResult(intent, Write_REQ_CODE)
            } else {
                noadshow=false


                return@setOnClickListener
            }
        }
        permissionbind.projection.setOnClickListener {

            try {

                if (!Settings.canDrawOverlays(this)) {
                    permissionbind.projection.isChecked = false

                    // If not granted, request the permission
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${this.packageName}")
                    )
                    startActivityForResult(intent, PermissionActivity.OVERLAY_PERMISSION_REQ_CODE)
                } else {
                    permissionbind.projection.isChecked = true
                    permissionbind.projection.isClickable = false

                    return@setOnClickListener

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.CAMERA,
            ),
            PermissionActivity.CAMERA_PERMISSION_CODE
        )
    }
    private fun checkCameraPermission(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        )
        return cameraPermission == PackageManager.PERMISSION_GRANTED
    }
    override fun onDestroy() {
        super.onDestroy()
        if (cameracount == 1) {
            PrefUtil(this).setInt("cameracount", 1)

        }
    }
    fun showSettingDialog() {
        try {
            val exitDialog = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)

                .setTitle("Permission_Required")
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

            exitDialog.window?.setBackgroundDrawableResource(R.color.white)

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
//fun showSettingDialog() {
//    try {
//        val exitDialog = MaterialAlertDialogBuilder(this@SecondPermissionActivity)
//            .setNegativeButton("Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }
//            .setPositiveButton("Open Settings") { dialog, _ ->
//                dialog.dismiss()
//                openSettings()
//            }
//            .create()
//
//        // Customize the title and message text
//        exitDialog.setOnShowListener {
//            val titleTextView = TextView(this).apply {
//                text = "permission_Required"
//                setTextColor(ContextCompat.getColor(this@SecondPermissionActivity, R.color.black))
//                textSize = 20f // Adjust as needed
//                setPadding(40, 40, 40, 10) // Adjust padding as needed
//                gravity = Gravity.CENTER // Optional: Align center
//            }
//
//            val messageTextView = TextView(this).apply {
//                text = "Permission Requires to Proceed with app"
//                setTextColor(ContextCompat.getColor(this@SecondPermissionActivity, R.color.black))
//                textSize = 16f // Adjust as needed
//                setPadding(40, 10, 40, 40) // Adjust padding as needed
//                gravity = Gravity.CENTER // Optional: Align center
//            }
//
//            // Set custom title and message to the dialog
//            exitDialog.setCustomTitle(titleTextView)
//            exitDialog.setView(messageTextView)
//
//            // Customize buttons' colors
//            val positiveButton = exitDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
//            val negativeButton = exitDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
//
//            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.black))
//            negativeButton.setTextColor(ContextCompat.getColor(this, R.color.black))
//        }
//
//        // Show the dialog
//        exitDialog.show()
//
//        // Set the background color to white
//        exitDialog.window?.setBackgroundDrawableResource(R.color.white)
//    } catch (e: Exception) {
//        // Handle exception if necessary
//    }
//}

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if (intentperm=="chat") {
            if (checkCameraPermission()){
                val intent = Intent(this, VideoAlarmReciver::class.java)
                sendBroadcast(intent)
                finish()
            }
        }
        else if (intentperm=="cameravideo") {
            if (checkCameraPermission()){
                val intent = Intent(this, ScheduleActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
            else if (intentperm=="customvideo") {
            if (checkCameraPermission()){
                val intent = Intent(this, ScheduleActivity::class.java)
                intent.putExtra("Key", "customvideo")
                startActivity(intent)
                finish()
            }
        }
        else if (intentperm=="audiocall") {
            if (Settings.System.canWrite(this)) {
                permissionbind.Write.isClickable = false
                permissionbind.Write.isChecked = true
                val intent = Intent(this, RingtoneActivity::class.java)
                startActivity(intent)
                finish()


            } else if (!Settings.System.canWrite(this)) {
                permissionbind.Write.isChecked = false
            } else {

            }
        }
        else if (intentperm=="overlay") {
            if (Settings.canDrawOverlays(this)) {
                permissionbind.overlay.isClickable = false
                permissionbind.overlay.isChecked = true
                finish()


            }
            else if (!Settings.canDrawOverlays(this)) {
                permissionbind.overlay.isChecked = false
            } else {

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (intentperm=="chat"){
            if (requestCode == PermissionActivity.CAMERA_PERMISSION_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    permissionbind.camera.isChecked = true
                    permissionbind.camera.isClickable = false
                    val intent = Intent(this, VideoAlarmReciver::class.java)
                    sendBroadcast(intent)
                    finish()

                } else {
                    // Permission denied
                    permissionbind.camera.isChecked = false
                    if (cameracount > 2) {
                        PrefUtil(this).setInt("cameracount", 2)
                        showSettingDialog()
                    }

                }

            }

        }
       else if (intentperm=="cameravideo"){
            if (requestCode == PermissionActivity.CAMERA_PERMISSION_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    permissionbind.camera.isChecked = true
                    permissionbind.camera.isClickable = false
                    val intent = Intent(this, ScheduleActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // Permission denied
                    permissionbind.camera.isChecked = false
                    if (cameracount > 2) {
                        PrefUtil(this).setInt("cameracount", 2)
                        showSettingDialog()
                    }

                }

            }

        } else if (intentperm=="customvideo"){
            if (requestCode == PermissionActivity.CAMERA_PERMISSION_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    permissionbind.camera.isChecked = true
                    permissionbind.camera.isClickable = false
                    val intent = Intent(this, ScheduleActivity::class.java)
                    intent.putExtra("Key", "customvideo")
                    startActivity(intent)
                    finish()

                } else {
                    // Permission denied
                    permissionbind.camera.isChecked = false
                    if (cameracount > 2) {
                        PrefUtil(this).setInt("cameracount", 2)
                        showSettingDialog()
                    }

                }

            }

        }
        else if (intentperm=="audiocall"){
            if (Settings.System.canWrite(this)) {
                permissionbind.Write.isClickable = false
                permissionbind.Write.isChecked = true
                val intent = Intent(this, RingtoneActivity::class.java)
                startActivity(intent)
                finish()


            } else if (!Settings.System.canWrite(this)) {
                permissionbind.Write.isChecked = false
            } else {

            }

        }
        else if (intentperm=="overlay") {
            if (Settings.canDrawOverlays(this)) {
                permissionbind.overlay.isClickable = false
                permissionbind.overlay.isChecked = true
                finish()


            } else if (!Settings.canDrawOverlays(this)) {
                permissionbind.overlay.isChecked = false
            } else {

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        if(intentperm=="audiocall"){
            if (Settings.System.canWrite(this)) {
                permissionbind.Write.isClickable = false
                permissionbind.Write.isChecked = true
                val intent = Intent(this, RingtoneActivity::class.java)
                startActivity(intent)
                finish()


            } else if (!Settings.System.canWrite(this)) {
                permissionbind.Write.isChecked = false
            } else {

            }
        }
        else if (intentperm=="overlay") {
            if (Settings.canDrawOverlays(this)) {
                permissionbind.overlay.isClickable = false
                permissionbind.overlay.isChecked = true
                finish()


            } else if (!Settings.canDrawOverlays(this)) {
                permissionbind.overlay.isChecked = false
            } else {

            }
        }


    }
    companion object{
        const val Write_REQ_CODE = 123

    }
}
