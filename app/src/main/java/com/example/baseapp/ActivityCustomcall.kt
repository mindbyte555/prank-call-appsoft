package com.example.baseapp
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.baseapp.Application.MyApplication
import com.example.baseapp.Application.MyApplication.Companion.bannerEnabled
import com.example.baseapp.Application.MyApplication.Companion.isEnabled
import com.example.baseapp.adsManager.AdsManager
import com.example.fakecall.R
import com.example.baseapp.tabadapter.TabAdapter
import com.example.baseapp.fragments.AudioCallFragment
import com.example.baseapp.fragments.CustomFragment.CustomSmsfragment
import com.example.baseapp.fragments.CustomFragment.customCallFragment.CustomCallFragment
import com.example.baseapp.fragments.CustomFragment.customVideoFragment.CustomVideoCallFragment
import com.example.baseapp.fragments.VideoCall.VideoCallFragment
import com.example.baseapp.utils.Constants.dialogbox
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.databinding.ActivityCustomcallBinding
import com.example.iwidgets.Utils.AppInfoUtils

class ActivityCustomcall : AppCompatActivity() {

    private lateinit var binding: ActivityCustomcallBinding
    private lateinit var prefUtil: PrefUtil
    private var isImageChooserOpen = false
    var sms=false


    companion object {
        private const val PICK_IMAGE_REQUEST = 123
        private val PERMISSION_REQUEST_STORAGE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCustomcallBinding.inflate(layoutInflater)
        sms=intent.getBooleanExtra("sms",false)


        setContentView(binding.root)
        Log.d("ThemeActivity", "sms$sms")

        prefUtil = PrefUtil(this)
        if (isEnabled && bannerEnabled) {
            binding.layoutNative.visibility=View.GONE
            if (!MyApplication.bannerCollapsible) {
                AdsManager.loadAdaptiveBannerAd(
                    binding.layoutNative, this,
                    object : AdsManager.AdmobBannerAdListener {
                        override fun onAdLoaded() {
                            Log.d("ThemeActivity", "Adaptive Banner Ad Loaded")
                            binding.layoutNative.visibility = View.VISIBLE
                        }

                        override fun onAdFailed() {
                            Log.d("ThemeActivity", "Adaptive Banner Ad Failed to Load")
                            binding.layoutNative.visibility = View.GONE
                        }
                    }
                )
            } else {
                AdsManager.loadCollapsible(
                    binding.layoutNative, this,
                    object : AdsManager.AdmobBannerAdListener {
                        override fun onAdLoaded() {
                            Log.d("ThemeActivity", "Standard Banner Ad Loaded")
                            binding.layoutNative.visibility = View.VISIBLE
                        }

                        override fun onAdFailed() {
                            Log.d("ThemeActivity", "Standard Banner Ad Failed to Load")
                            binding.layoutNative.visibility = View.GONE
                        }
                    }
                )
            }
        } else {
            // Both flags are false, so hide the ad container
            binding.layoutNative.visibility = View.GONE
        }



        // Hide some views from another activity
       // hideActionBarElements()

        var isConnected = AppInfoUtils(this).isInternetAvailable()

        if(!isConnected){
            dialogbox(this)

        }

        setupTabs()

        binding.customeeditname.setOnLongClickListener {
            // Do something or don't
            true
        }
        PrefUtil(this).setString("profile_img","")


        binding.imagecall.setOnClickListener {
            checkGalleryPermissionAndOpenChooser()
        }

        // Save the name and number in SharedPreferences
        saveTextToPreferences()

        // Add Text Watcher to update preferences
        setupTextWatchers()
    }

    private fun setupTabs() {
        val tabAdapter = TabAdapter(supportFragmentManager)
        binding.scheduletabLayout.setupWithViewPager(binding.callschduleviewpager)

        tabAdapter.addFragment(CustomVideoCallFragment(), getString(R.string.video_call))
        tabAdapter.addFragment(CustomCallFragment(), getString(R.string.audio_call))
        tabAdapter.addFragment(CustomSmsfragment(), getString(R.string.sms))

//        tabAdapter.addFragment(CustomVideoCallFragment(), "Video Call")
//        tabAdapter.addFragment(CustomCallFragment(), "Audio Call")

        binding.callschduleviewpager.offscreenPageLimit = 3
        binding.callschduleviewpager.adapter = tabAdapter

        if(sms){
            binding.callschduleviewpager.currentItem = 2
            PrefUtil(this).setBool("sms", false)


        }
    }

    private fun setupTextWatchers() {
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                prefUtil.setString("custm_profile", binding.customeeditname.text.toString())
            }
        }

        val textWatcherNum: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                prefUtil.setString("custm_num", binding.customeeditnumber.text.toString())
            }
        }

        binding.customeeditname.addTextChangedListener(textWatcher)
        binding.customeeditnumber.addTextChangedListener(textWatcherNum)
    }

    private fun saveTextToPreferences() {
        prefUtil.setString("custm_profile", binding.customeeditname.text.toString())
        prefUtil.setString("custm_num", binding.customeeditnumber.text.toString())
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
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser()
            } else {
                Toast.makeText(this, "Permission required to access gallery", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        isImageChooserOpen = false
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            if (imageUri != null) {
                Glide.with(this)
                    .load(imageUri)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)
                prefUtil.setString("profile_img", imageUri.toString())
            }
        }
    }

    override fun onResume() {
        super.onResume()
       // hideActionBarElements()
    }

    override fun onDestroy() {
        super.onDestroy()
        isImageChooserOpen = false
    }
}
