package com.example.baseapp.schedule


import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.baseapp.Application.MessagingForegroundService
import com.example.baseapp.Application.MyApplication.Companion.bannerCollapsible
import com.example.baseapp.Application.MyApplication.Companion.bannerEnabled
import com.example.baseapp.Application.MyApplication.Companion.done_enabled
import com.example.baseapp.Application.MyApplication.Companion.exitnativeEnabled
import com.example.baseapp.Application.MyApplication.Companion.isEnabled
import com.example.baseapp.Application.MyApplication.Companion.noadshow
import com.example.baseapp.Application.MyApplication.Companion.review
import com.example.baseapp.Application.RingingForegroundService
import com.example.baseapp.adsManager.ActionOnAdClosedListener
import com.example.baseapp.adsManager.AddIds
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.adsManager.Load_inter
import com.example.baseapp.alarmManagers.AlarmReceiver
import com.example.baseapp.alarmManagers.CustomVideoAlarmReciver
import com.example.baseapp.alarmManagers.CustomcallReciver
import com.example.baseapp.alarmManagers.VideoAlarmReciver
import com.example.baseapp.secondPermission.SecondPermissionActivity
import com.example.baseapp.utils.Constants.dialogbox
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivityScheduleBinding
import com.example.iwidgets.Utils.AppInfoUtils
import java.util.Locale

class ScheduleActivity : AppCompatActivity() {
    lateinit var binding: ActivityScheduleBinding
    var count = 1
    lateinit var prefutil: PrefUtil
    var keyValue = ""
    var itemSelcted = false
    var check = false
    var firstclick = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        prefutil = PrefUtil(this)
        //removed !if and set false in scehdule btn and backbtn

        if (prefutil.getBool("item1")) {
            firstclick = false
            Log.e("TestTag", "Inside true: ${prefutil.getBool("item1")}")
            count = prefutil.getInt("count", 1)

            binding.scheduleafter.setChecked(true);
            binding.durationSpinner.visibility = View.VISIBLE

            binding.scheduleimmediate.setChecked(false);
            binding.durationSpinner.isEnabled = true

            itemSelcted = prefutil.getBool("item1")


        } else {
            Log.e("TestTag", "Inside False: $itemSelcted")
            count = 1
            binding.durationSpinner.visibility = View.INVISIBLE

            binding.scheduleimmediate.setChecked(true);
            binding.scheduleafter.setChecked(false);
            binding.durationSpinner.isEnabled = false
            binding.schedulenbtn.text = getString(R.string.done)



        }
        Log.e("TestTag", "Saved")


        if (prefutil.getString("savedString") == null) {
            Log.e("TestTag", "Saved")
            var savedString = prefutil.getString("savedString")
            binding?.durationSpinner?.text = savedString
            Log.e("TestTag", "savedString: $savedString")




        } else {
            Log.e("TestTag", "Outside Saved")
            binding?.durationSpinner?.text = getString(R.string.seconds)

        }
        setContentView(binding.root)

        Log.d("SheduleAdaptive", "onCreate: Enter in loop1")

        if (PrefUtil(this).getBool("is_premium", false) || !isEnabled || !bannerEnabled) {
            binding.layoutNative.visibility = View.GONE
        } else {
            //  binding.layoutNative.setBackgroundColor(ContextCompat.getColor(this@ScheduleActivity, R.color.white))
                Log.d("SheduleAdaptive", "onCreate: Enter in loop")
                if (!bannerCollapsible) {
                    Log.d("SheduleAdaptive", "onCreate: adaptiveloaded in Schedule")
                    AdsManager.loadAdaptiveBannerAd(
                        binding.layoutNative!!, this@ScheduleActivity,
                        object : AdsManager.AdmobBannerAdListener {
                            override fun onAdLoaded() {
                                binding.layoutNative.setBackgroundColor(
                                    ContextCompat.getColor(this@ScheduleActivity, R.color.white)
                                )
                                binding.musicDetailsShimmer.hideShimmer()
                                binding.musicDetailsShimmer.stopShimmer()
                            }

                            override fun onAdFailed() {
                                binding.layoutNative.setBackgroundColor(
                                    ContextCompat.getColor(this@ScheduleActivity, R.color.gray1)
                                )
                                binding.musicDetailsShimmer.hideShimmer()
                                binding.musicDetailsShimmer.stopShimmer()
                            }
                        }
                    )
                } else {
                    AdsManager.loadCollapsible(
                        binding.layoutNative!!, this@ScheduleActivity,
                        object : AdsManager.AdmobBannerAdListener {
                            override fun onAdLoaded() {
                                binding.layoutNative.setBackgroundColor(
                                    ContextCompat.getColor(this@ScheduleActivity, R.color.white)
                                )
                                binding.musicDetailsShimmer.hideShimmer()
                                binding.musicDetailsShimmer.stopShimmer()
                            }

                            override fun onAdFailed() {
                                binding.layoutNative.setBackgroundColor(
                                    ContextCompat.getColor(this@ScheduleActivity, R.color.gray1)
                                )
                                binding.musicDetailsShimmer.hideShimmer()
                                binding.musicDetailsShimmer.stopShimmer()
                            }
                        }
                    )
                }

        }





        try {
            val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
                Log.e(" ", "onCreate1: $nightMode")
//                binding.scheduleimmediate.isChecked = true
//                binding.scheduleafter.isChecked = false


                val window = this.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor =
                    ContextCompat.getColor(this, com.example.fakecall.R.color.white)
                window.navigationBarColor =
                    resources.getColor(com.example.fakecall.R.color.white, theme)

                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                    isAppearanceLightNavigationBars = true
                }
            } else {
                Log.e("TAG", "onCreate2: $nightMode")

//                binding.scheduleimmediate.isChecked = true
//                binding.scheduleafter.isChecked = false


                val window = this.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor =
                    ContextCompat.getColor(this, com.example.fakecall.R.color.white)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                window.navigationBarColor =
                    resources.getColor(com.example.fakecall.R.color.white, theme)

                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                }
            }

        } catch (e: Exception) {
        }
        if (intent.hasExtra("Key")) {
            keyValue = intent.getStringExtra("Key").toString()
            Log.d("TestTag", "$keyValue :keyvalue")
            if(keyValue == "sms" ||keyValue =="customsms"){
                binding.scheduleimmediate.text="SMS Me Now"
                binding.scheduleafter.text="SMS Me After"
            }

        } else {
            Log.d("TestTag", "No keyvalue")

        }



        binding?.durationSpinner?.setOnClickListener {

            val popupMenu = PopupMenu(
                this@ScheduleActivity, binding?.durationSpinner, 0, 0, R.style.myListPopupWindow
            )
            popupMenu.menuInflater.inflate(R.menu.popup, popupMenu.menu)

//            val inflater = menuInflater
//            inflater.inflate(R.menu.popup, popupMenu.menu) // Replace with your menu resource ID

            val textColor =
                ContextCompat.getColor(this, R.color.black) // Replace with your desired color

            for (i in 0 until popupMenu.menu.size()) {
                val item = popupMenu.menu.getItem(i)
                val spannableString = SpannableString(item.title)
                spannableString.setSpan(
                    ForegroundColorSpan(textColor),
                    0,
                    spannableString.length,
                    0
                )
                item.title = spannableString
            }

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.item1 -> {
                        count = 2
                        binding?.durationSpinner?.text = "10 sec"
                        prefutil.setString("savedString", "10 sec")
                        prefutil.setInt("count", 2)

                        menuItem.isChecked = true
                        true
                    }

                    R.id.item2 -> {
                        binding?.durationSpinner?.text = "30 sec"
                        prefutil.setString("savedString", "30 sec")

                        prefutil.setInt("count", 3)

                        count = 3
                        true

                    }

                    R.id.item3
                    -> {
                        binding?.durationSpinner?.text = "1 min"
                        prefutil.setString("savedString", "1 min")
                        prefutil.setInt("count", 4)


                        count = 4
                        true
                    }

                    R.id.item4
                    -> {
                        binding?.durationSpinner?.text = "5 min"
                        prefutil.setString("savedString", "5 min")
                        prefutil.setInt("count", 5)


                        count = 5
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }

//        binding.schedulenbtn.text = "Call Now"


        binding.scheduleimmediate.setOnClickListener {
            noadshow =true

            firstclick = true
            binding.schedulenbtn.text = getString(R.string.done)

            binding.durationSpinner.isEnabled = false
            binding.durationSpinner.visibility = View.INVISIBLE
            count = 1
            prefutil.setBool("item1", false)
//            prefutil.setBool("item1", true)
        }
        binding.scheduleafter.setOnClickListener {
            binding.durationSpinner.visibility = View.VISIBLE
            noadshow =false

            firstclick = false
            if (Settings.canDrawOverlays(this)) {
                if (prefutil.getInt("count", 0) == 1) {
                    prefutil.setInt("count", 2)

                }
                binding.schedulenbtn.text = getString(R.string.done)

                binding.durationSpinner.isEnabled = true
                count = 2
                prefutil.setBool("item1", true)
            } else {

                Toast.makeText(this, "Please Give OverlayPermission", Toast.LENGTH_SHORT).show()
//                checkOverlayPermission(this)
                val intent = Intent(it.context, SecondPermissionActivity::class.java)
                intent.putExtra("key", "overlay")
                it.context.startActivity(intent)
            }
//            if (prefutil.getInt("count",0)==1){
//                prefutil.setInt("count",2)
//
//            }
//            binding.schedulenbtn.text = "Done"
//            binding.durationSpinner.isEnabled = true
//            count = 2
//            prefutil.setBool("item1", true)
//            prefutil.setBool("item1", false)

        }
        binding.schedulebackbtn.setOnClickListener {
            prefutil.setInt("count", 1)
            prefutil.setBool("item1", false)
            prefutil.setString("savedString", "10 sec")




            finish()
        }
        binding.schedulenbtn.setOnClickListener {
            var isConnected = AppInfoUtils(this).isInternetAvailable()


            if (!isConnected){
            }
            else{
                if(done_enabled){

                    prefutil.setInt("count", 1)
                    prefutil.setBool("item1", false)
                    prefutil.setString("savedString", "10 sec")
                    Load_inter.request_interstitial(
                        this@ScheduleActivity,
                        this@ScheduleActivity,
                        AddIds.NoteId(), "dashboard_note", object :
                            ActionOnAdClosedListener {
                            override fun ActionAfterAd() {


                                calls()

                            }
                        })


                }
                else{
                    prefutil.setInt("count", 1)
                    prefutil.setBool("item1", false)
                    prefutil.setString("savedString", "10 sec")

                    calls()

                }
            }






        }


    }

    fun checkOverlayPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            )
            context.startActivity(intent)
            check = false
//            granted=true
            prefutil.setBool("Permission", false)
            PrefUtil(this).setBool("Allowed", true)

        } else {
            check = true
        }
        return check
    }

    override fun onResume() {
        super.onResume()
        if (firstclick == false) {
            if (Settings.canDrawOverlays(this)) {
                if (prefutil.getInt("count", 0) == 1) {
                    prefutil.setInt("count", 2)

                }
                binding.schedulenbtn.text = getString(R.string.done)


                binding.durationSpinner.isEnabled = true
                count = 2
                prefutil.setBool("item1", true)
            } else {
                binding.scheduleimmediate.setChecked(true);
                binding.scheduleafter.setChecked(false);
                binding.durationSpinner.visibility = View.INVISIBLE

            }
        }

    }

    override fun attachBaseContext(newBase: Context?) {
        var selectedLanguage = PrefUtil(newBase!!).getString("Language_Localization")
        Log.d("Lang", selectedLanguage!!)

        val locale = Locale(selectedLanguage)
        val config = Configuration()
        config.setLocale(locale)
        applyOverrideConfiguration(config)
        super.attachBaseContext(newBase)
    }
    fun calls(){
        if (keyValue == "audio") {

            when (count) {
                1 -> {
                    val intent = Intent(this@ScheduleActivity, AlarmReceiver::class.java)
                    sendBroadcast(intent)
                    finish()
                }

                2 -> {
                    prefutil?.setInt("audiotime", 1)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    finish()
                }

                3 -> {
                    prefutil.setInt("audiotime", 2)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    finish()
                }

                4 -> {
                    prefutil?.setInt("audiotime", 3)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                5 -> {
                    prefutil?.setInt("audiotime", 4)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                else -> Toast.makeText(
                    this@ScheduleActivity,
                    "Please select time in Schedule",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        else if (keyValue == "customvideo")
        {
            when (count) {
                1 -> {
                    Log.d("Testtag", "Inside custom video")
                    val intent = Intent(this@ScheduleActivity, CustomVideoAlarmReciver::class.java)
                    sendBroadcast(intent)
                    finish()
                }

                2 -> {
                    prefutil?.setInt("customvideotime", 1)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                3 -> {
                    prefutil.setInt("customvideotime", 2)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                4 -> {
                    prefutil?.setInt("customvideotime", 3)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                5 -> {
                    prefutil?.setInt("customvideotime", 4)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                else -> Toast.makeText(
                    this@ScheduleActivity,
                    "Please select time in Schedule",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        else if (keyValue == "customcall") {
            when (count)
            {
                1 -> {
                    Log.d("Testtag", "Inside custom call")
                    val intent = Intent(this@ScheduleActivity, CustomcallReciver::class.java)
                    sendBroadcast(intent)
                    finish()
                }

                2 -> {
                    prefutil?.setInt("customaudiotime", 1)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                3 -> {
                    prefutil.setInt("customaudiotime", 2)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                4 -> {
                    prefutil?.setInt("customaudiotime", 3)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                5 -> {
                    prefutil?.setInt("customaudiotime", 4)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                else -> Toast.makeText(
                    this@ScheduleActivity,
                    "Please select time in Schedule",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        else if (keyValue == "sms") {
            when (count)
            {
                1 -> {
                    var  profile=PrefUtil(this).getString("profile")
                    val profilenum =PrefUtil(this).getInt("profile_int",0)
                    Log.d("Testtag", "profilenum$profilenum")

                    val serviceIntent =
                        Intent(this, MessagingForegroundService::class.java).apply {
                            putExtra("profile_name", profile)
                            putExtra("profile_num",profilenum)
                        }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        this.startForegroundService(serviceIntent)
                    } else {
                        this.startService(serviceIntent)
                    }

                    Log.d("Testtag", "Inside sms")

                }

                2 -> {
                    prefutil?.setInt("sms_count", 1)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule SMS Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                3 -> {
                    prefutil.setInt("sms_count", 2)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule SMS Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                4 -> {
                    prefutil?.setInt("sms_count", 3)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule SMS Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                5 -> {
                    prefutil?.setInt("sms_count", 4)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule SMS Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                else -> Toast.makeText(
                    this@ScheduleActivity,
                    "Please select time in Schedule",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        else if (keyValue == "customsms") {
            when (count)
            {
                1 -> {
                    var  profile=PrefUtil(this).getString("custm_profile")

                    val serviceIntent =
                        Intent(this, MessagingForegroundService::class.java).apply {
                            putExtra("profile_name", profile)
                            putExtra("profile_num",-1)
                        }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        this.startForegroundService(serviceIntent)
                    } else {
                        this.startService(serviceIntent)
                    }

                    Log.d("Testtag", "Inside sms")

                }

                2 -> {
                    prefutil?.setInt("sms_count", 1)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule SMS Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                3 -> {
                    prefutil.setInt("sms_count", 2)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule SMS Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                4 -> {
                    prefutil?.setInt("sms_count", 3)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule SMS Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                5 -> {
                    prefutil?.setInt("sms_count", 4)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule SMS Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                else -> Toast.makeText(
                    this@ScheduleActivity,
                    "Please select time in Schedule",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        else
        {
            when (count) {
                1 -> {
                    val intent = Intent(this@ScheduleActivity, VideoAlarmReciver::class.java)
                    sendBroadcast(intent)
                    finish()
                }

                2 -> {
                    prefutil?.setInt("videotime", 1)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                3 -> {
                    prefutil.setInt("videotime", 2)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                4 -> {
                    prefutil?.setInt("videotime", 3)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                5 -> {
                    prefutil?.setInt("videotime", 4)
                    Toast.makeText(this@ScheduleActivity, "Click the Schedule Call Button", Toast.LENGTH_LONG)
                        .show()
                    count = 0
                    finish()
                }

                else -> Toast.makeText(
                    this@ScheduleActivity,
                    "Please select time in Schedule",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}



