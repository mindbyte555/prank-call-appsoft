package com.example.baseapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.baseapp.Application.MessagingForegroundService
import com.example.baseapp.Application.MyApplication.Companion.bannerCollapsible
import com.example.baseapp.Application.MyApplication.Companion.bannerEnabled
import com.example.baseapp.Application.MyApplication.Companion.exitnativeEnabled
import com.example.baseapp.Application.MyApplication.Companion.internetbox
import com.example.baseapp.Application.MyApplication.Companion.isEnabled
import com.example.baseapp.Application.MyApplication.Companion.noadshow
import com.example.baseapp.Application.MyApplication.Companion.review
import com.example.baseapp.adsManager.AddIds.Companion.Exit_Native
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.adsManager.AdsManager.Companion.showCollape
import com.example.baseapp.adsManager.GdprConsentClass
import com.example.baseapp.utils.EventNames.EXIT_DIALOG
import com.example.baseapp.utils.FirebaseCustomEvents
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivityMainBinding
import com.example.iwidgets.Utils.AppInfoUtils
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdView
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import java.util.Locale


class MainActivity : AppCompatActivity() {
    val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val prefutil: PrefUtil = PrefUtil(this)
    var manager: ReviewManager? = null
    var adView: AdView? = null
    //    var appUpdateManager: AppUpdateManager? = null
    val appInfoUtils = AppInfoUtils(this)
    var appUpdateManager: AppUpdateManager? = null
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
                // You can now show notifications
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    var isConnected: Boolean = false

    //GDPR
    private lateinit var googleMobileAdsConsentManager: GdprConsentClass


    val navController by lazy {
        navController(R.id.frame_container)
    }

    companion object {
        var intercounter = 0
        var seconcounter = 0
        var test=false

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("showCollape", "Newly Created}")

        askNotificationPermission()


        var selectedLanguage = prefutil.getString("Language_Localization")
        changlang(selectedLanguage!!)
        googleMobileAdsConsentManager = GdprConsentClass.getInstance(this)


        isConnected = AppInfoUtils(this@MainActivity).isInternetAvailable()
        setContentView(binding.root)
        if(!isConnected && internetbox){

//            dialogbox()
//            val rootView = findViewById<View>(android.R.id.content)
//            val snackbar = Snackbar.make(rootView, "Please Connect Internet", Snackbar.LENGTH_SHORT)
//            snackbar.setBackgroundTint( ContextCompat.getColor(this@MainActivity, R.color.tutorial_text))
//            snackbar.setTextColor(   ContextCompat.getColor(this@MainActivity, R.color.white))
//            snackbar.show()
        }

//        binding.mainhead.isSelected=true
        if (PrefUtil(this).getBool("is_premium", false)||!isEnabled || !bannerEnabled) {
            Log.d("showCollape", "Visibility set to: ${binding.bannerAdmain.visibility}")

                binding.bannerAdmain.visibility = View.GONE
        }
        else {
//
            if (isEnabled ) {
                binding.bannerAdmain.visibility = View.VISIBLE

                if (bannerCollapsible) {
                    adView = AdsManager.loadCollapsible(
                        binding.bannerAdmain,
                        this@MainActivity,
                        object : AdsManager.AdmobBannerAdListener {
                            override fun onAdFailed() {
                                Log.e("showCollape", "onAdFailed: Collapsible")
                                binding.bannerAdmain.setBackgroundColor(
                                    ContextCompat.getColor(this@MainActivity, R.color.trans)
                                )
                            }

                            override fun onAdLoaded() {
                                Log.e("showCollape", "onAdLoaded: Collapsible")
                                binding.bannerAdmain.setBackgroundColor(
                                    ContextCompat.getColor(this@MainActivity, R.color.trans)
                                )
                            }
                        }
                    )
                } else {
                    adView = AdsManager.loadAdaptiveBannerAd(
                        binding.bannerAdmain,
                        this@MainActivity,
                        object : AdsManager.AdmobBannerAdListener {
                            override fun onAdFailed() {
                                Log.e("showCollape", "onAdFailed: Normal Banner")
                                binding.bannerAdmain.setBackgroundColor(
                                    ContextCompat.getColor(this@MainActivity, R.color.trans)
                                )
                            }

                            override fun onAdLoaded() {
                                Log.e("showCollape", "onAdLoaded: Normal Banner")
                                binding.bannerAdmain.setBackgroundColor(
                                    ContextCompat.getColor(this@MainActivity, R.color.trans)
                                )
                            }
                        }
                    )
                }
            } else {
                binding.bannerAdmain.visibility = View.GONE
            }

//            }
//            else
//            {
//                binding.bannerAdmain.visibility=View.GONE
//                Log.e("showCollape", "onCreate:$showCollape" )
//            }
        }

//        binding.drawerimg.setOnClickListener {
////            binding.bannerAdmain.visibility=View.GONE
//            // Hide the keyboard
//            val inputMethodManager =
//                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            currentFocus?.let { view ->
//                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
//            }
//
//            // Clear the focus
//            binding.root.clearFocus()
//
//            // Toggle the drawer
//            if (!binding.mainconsy.isDrawerOpen(GravityCompat.START)) {
//                binding.indrawerMenu.textViewpriv1.isSelected=true
//                binding.indrawerMenu.textViewupdate2.isSelected=true
//                binding.mainconsy.openDrawer(GravityCompat.START)
//            } else {
//                binding.indrawerMenu.textViewpriv1.isSelected=false
//                binding.indrawerMenu.textViewupdate2.isSelected=false
//                binding.mainconsy.closeDrawer(GravityCompat.START)
//            }
//        }
//
//        binding.premiumiconmain.setOnClickListener {
////            Toast.makeText(this@MainActivity, "Coming Soon", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, InAppPurchases::class.java)
//            intent.putExtra("skip_btn", true)
//            intent.putExtra("user", "from_home")
//            startActivity(intent)
//        }
//        binding.addpeopleimg.setOnClickListener {
//            FirebaseCustomEvents(this).createFirebaseEvents(
//                "Custom_fragment_$version",
//                "true"
//            )
//            binding.homeImg.setImageResource(R.drawable.ic_homeunselect)
//            binding.addpeopleimg.setImageResource(R.drawable.ic_personselect)
//            binding.settingicon.setImageResource(R.drawable.ic_settingunsele)
//
//            navController.navigate(R.id.customFragment)
//
//
//        }
        binding.homeImg.setOnClickListener {
            binding.homeImg.setImageResource(R.drawable.ic_homeselect)
//            binding.addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
            binding.settingicon.setImageResource(R.drawable.ic_settingunsele)

            navController.navigate(R.id.mainFragment)

        }
        binding.settingicon.setOnClickListener {
            binding.homeImg.setImageResource(R.drawable.ic_homegrey)
//            binding.addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
            binding.settingicon.setImageResource(R.drawable.ic_setting_select)
            navController.navigate(R.id.settingFragment)

        }

        binding.indrawerMenu.settingDrawer.setOnClickListener {
            startActivity(Intent(this, SettingActivity2::class.java))
            binding.mainconsy.closeDrawer(GravityCompat.START)
        }
        binding.indrawerMenu.priv.setOnClickListener {
            noadshow=false
            appInfoUtils.openPrivacy()
            binding.mainconsy.closeDrawer(GravityCompat.START)
        }
        binding.indrawerMenu.rateus.setOnClickListener {
            noadshow=false
            appInfoUtils.rateApp()
            binding.mainconsy.closeDrawer(GravityCompat.START)
        }
        binding.indrawerMenu.share.setOnClickListener {
            noadshow=false
            appInfoUtils.shareApp()
            binding.mainconsy.closeDrawer(GravityCompat.START)
        }
        binding.indrawerMenu.update.setOnClickListener {

            noadshow=false

            appInfoUtils.checkUpdate()
            binding.mainconsy.closeDrawer(GravityCompat.START)
        }
        if (googleMobileAdsConsentManager.isConsentAvailable) {
            binding.indrawerMenu.consent.visibility = View.VISIBLE

        } else {
            binding.indrawerMenu.consent.visibility = View.GONE
        }

        binding.indrawerMenu.consent.setOnClickListener {
            // Hide ad before showing consent form
            binding.bannerAdmain.visibility = View.GONE

            googleMobileAdsConsentManager.showPrivacyOptionsForm(this) { formError ->
                if (formError != null) {
                    Toast.makeText(this, formError.message, Toast.LENGTH_SHORT).show()
                } else {
                    // ✅ Consent form closed successfully — reload the banner ad
                    if (googleMobileAdsConsentManager.canRequestAds) {
                      // 👈 You should define this function to load banner
                        binding.bannerAdmain.visibility = View.VISIBLE
                    }
                }
            }
        }





        try {
            val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
                val window = this.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.scedhuleing_bg)
                window.navigationBarColor = resources.getColor(R.color.white, theme)

                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                    isAppearanceLightNavigationBars = true
                }
            } else {
                val window = this.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.scedhuleing_bg)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                window.navigationBarColor = resources.getColor(R.color.white, theme)

                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                }
            }

        } catch (e: Exception) {
        }
//        requestReviewDialog()
        updateApp()


    }


    //    @SuppressLint("MissingSuperCall")
//    override fun onBackPressed() {
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_container)
//        if (currentFragment is MainFragment) {
//            openExitDialog()
//
//
////            if (binding.mainconsy.isDrawerOpen(GravityCompat.START)) {
////                binding.mainconsy.closeDrawer(GravityCompat.END)
////            } else {
////            }
//        } else {
//            val homeFragment = MainFragment()
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, homeFragment)
//                .commit()
//        }
//
//    }
    override fun onResume() {
        Log.d("Testtag", "onResume review$review ")
        isConnected = AppInfoUtils(this@MainActivity).isInternetAvailable()

        if (!isEnabled||!bannerEnabled) {
            Log.e("Testtag", "if connected$isConnected")

            runOnUiThread {
                binding.bannerAdmain.visibility = View.GONE

            }
            if(review){
                Log.e("Testtag", "ifreview:$showCollape")

                requestReviewDialog()
                if(!isConnected && internetbox){
                    Log.e("Testtag", "elsereview:$showCollape")

//                    dialogbox()
                }
            }
            else{
                if(!isConnected && internetbox){
//                    dialogbox()
                }
            }

            Log.e("Testtag", "onCreate:$showCollape")
        }
        else{
            Log.e("Testtag", "else connected$isConnected")

            if(review){

                requestReviewDialog()
                if(!isConnected && internetbox){
//                    dialogbox()
                }
            }
            else{
                if(!isConnected && internetbox){
//                    dialogbox()
                }
            }





        }
        super.onResume()
    }


    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frame_container) as NavHostFragment
        val navController = navHostFragment.navController

        Log.e(
            "TESTTAG",
            "onBackPressed: currentDestination :${navController.currentDestination?.label}",
        )
        if (navController.currentDestination?.label == "fragment_main") {

            if (binding.mainconsy.isDrawerOpen(GravityCompat.START)) {
                binding.mainconsy.closeDrawer(GravityCompat.START)
            } else {
                openExitDialog()
            }
        } else if (navController.currentDestination?.label == "fragment_chat") {
            Log.d("TESTTAG", "here")
            navController.navigate(R.id.mainFragment)
        } else if (navController.currentDestination?.label == "fragment_scedhule") {
            navController.navigate(R.id.mainFragment)
        } else if (navController.currentDestination?.label == "fragment_setting") {
            binding.homeImg.setImageResource(R.drawable.ic_homeselect)
//            binding.addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
            binding.settingicon.setImageResource(R.drawable.ic_settingunsele)

            navController.navigate(R.id.mainFragment)
        } else if (navController.currentDestination?.label == "fragment_custom") {
            binding.homeImg.setImageResource(R.drawable.ic_homeselect)
//            binding.addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
            binding.settingicon.setImageResource(R.drawable.ic_settingunsele)

            navController.navigate(R.id.mainFragment)
        } else {
//            navController.popBackStack(R.id.mainFragment, false)
//            navController.popBackStack()
        }
    }

//    private fun getCurrentFragment(): String? {
//        val navController = findNavController()
//        val currentDestination = navController.currentDestination
//        // The currentDestination will give you the ID of the current fragment
//        // You can use this ID to identify the current fragment
//        return currentDestination?.label.toString()
//    }

    override fun attachBaseContext(newBase: Context?) {
        var selectedLanguage = PrefUtil(newBase!!).getString("Language_Localization")
        Log.d("Lang", selectedLanguage!!)

        val locale = Locale(selectedLanguage)
        val config = Configuration()
        config.setLocale(locale)
        applyOverrideConfiguration(config)
        super.attachBaseContext(newBase)
    }

    private fun requestReviewDialog() {
        manager = ReviewManagerFactory.create(this)
        val request: Task<ReviewInfo?> = manager!!.requestReviewFlow()
        request.addOnCompleteListener { task: Task<ReviewInfo?> ->
            if (task.isSuccessful) {
                // We can get the ReviewInfo object
                val reviewInfo = task.result
                launchReview(reviewInfo!!)
            }
        }
    }

    private fun launchReview(reviewInfo: ReviewInfo) {
        val flow = manager!!.launchReviewFlow(this, reviewInfo)
        flow.addOnCompleteListener { task: Task<Void?>? ->
            review=false

        }
    }

    private fun updateApp() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager!!.appUpdateInfo.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                    AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/
                )
            ) {
                try {
                    appUpdateManager!!.startUpdateFlowForResult(
                        appUpdateInfo, AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/, this, 18
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate()
            } else {
                //nothing
            }
        }
    }


    private fun openExitDialog() {
        // Make background dim
        val dimAmount = 0.7f // Adjust this value (0.0f to 1.0f) for desired dim level
        val params = window.attributes
        params.alpha = 1.0f - dimAmount // Invert since alpha is transparency (1.0 = fully opaque)
        window.attributes = params


        FirebaseCustomEvents(this).createFirebaseEvents(
            EXIT_DIALOG,
            "true"
        )
        // Hide banner ad if visible
        val wasBannerVisible = binding.bannerAdmain.visibility == View.VISIBLE
        if (wasBannerVisible) {
            binding.bannerAdmain.visibility = View.GONE
        }

        val viewGroup: ViewGroup = this@MainActivity.findViewById(android.R.id.content)
        val dialogView: View = LayoutInflater.from(this@MainActivity).inflate(R.layout.exitdialog, viewGroup, false)

        val builder = android.app.AlertDialog.Builder(this)
        builder.setView(dialogView)

        val alertDialog = Dialog(
            this,
            android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
        )
        alertDialog.setCancelable(false)

        alertDialog.setContentView(dialogView)
        alertDialog.window?.decorView?.setPadding(0, 0, 0, 0)


        alertDialog.setContentView(dialogView)
        alertDialog.window?.decorView?.setPadding(0, 0, 0, 0)

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        alertDialog.window!!.attributes.windowAnimations = R.style.DialogTheme


        if (exitnativeEnabled && !PrefUtil(this).getBool("is_premium", false)){
            val shimmerexit = dialogView.findViewById<ShimmerFrameLayout>(R.id.musicDetailsShimmer)
            val nativeexit = dialogView.findViewById<RelativeLayout>(R.id.bannerAdexit)
            shimmerexit.visibility = View.VISIBLE
            nativeexit.visibility = View.VISIBLE

            AdsManager.ExitloadNative(nativeexit,
                this@MainActivity, Exit_Native,
                object : AdsManager.AdmobBannerAdListener {
                    override fun onAdFailed() {
                        nativeexit.setBackgroundColor(
                            ContextCompat.getColor(
                                this@MainActivity, R.color.white
                            )
                        )
                        shimmerexit.hideShimmer()
                        shimmerexit.stopShimmer()
                    }

                    override fun onAdLoaded() {
                        nativeexit.setBackgroundColor(
                            ContextCompat.getColor(
                                this@MainActivity, R.color.white
                            )
                        )
                        shimmerexit.hideShimmer()
                        shimmerexit.stopShimmer()
                    }
                },AdsManager.NativeAdType.MEDIUM)
            val exitLabelNo = dialogView.findViewById<TextView>(R.id.exit_label_no)
            exitLabelNo.setOnClickListener {
                alertDialog.dismiss()
                intercounter = 0
                seconcounter = 0
            }

            val exitLabelYes = dialogView.findViewById<TextView>(R.id.exit_label_yes)
            exitLabelYes.setOnClickListener {
                alertDialog.dismiss()
                this@MainActivity.finishAffinity()
            }
            exitLabelYes.visibility = View.INVISIBLE
            exitLabelNo.visibility = View.INVISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                fadeIn(exitLabelYes)
                fadeIn(exitLabelNo)
            }, 3000)
        }



        // Restore visibility when dialog is dismissed
        alertDialog.setOnDismissListener {
            // Restore window alpha
            val restoreParams = window.attributes
            restoreParams.alpha = 1.0f
            window.attributes = restoreParams

            // Restore banner ad if it was visible
            if (wasBannerVisible && isEnabled && bannerEnabled && !PrefUtil(this).getBool("is_premium", false)) {
                binding.bannerAdmain.visibility = View.VISIBLE
            }
        }

        alertDialog.show()

        val exitLabelNo = dialogView.findViewById<TextView>(R.id.exit_label_no)
        exitLabelNo.setOnClickListener {
            alertDialog.dismiss()
            intercounter = 0
            seconcounter = 0
        }

        val exitLabelYes = dialogView.findViewById<TextView>(R.id.exit_label_yes)
        exitLabelYes.setOnClickListener {
            alertDialog.dismiss()
            this@MainActivity.finishAffinity()
        }

    }


    private fun popupSnackBarForCompleteUpdate() {
        val snackBar = Snackbar.make(
            findViewById<View>(android.R.id.content),
            "App Update successful",
            Snackbar.LENGTH_INDEFINITE
        )
        snackBar.setAction("Install") {
            appUpdateManager!!.completeUpdate()
        }
        snackBar.show()
    }

    fun changlang(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)


    }
    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission is already granted
                }

                shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Optionally show UI to explain why you need this permission
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }

                else -> {
                    // Directly request permission
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }



    private fun handleIntent(intent: Intent) {
        val profile = intent.getStringExtra("profile")
        val profileInt = intent.getIntExtra("profile_int", 0)
        val customprofile = intent.getStringExtra("customprofile")
        val customno = intent.getStringExtra("customprofileno")
        val customuri = intent.getStringExtra("customprofileuri")
        val chatprofile = intent.getStringExtra("profilechat")
        val chatprofileInt = intent.getIntExtra("chatpic", 0)
        val callprofile = intent.getStringExtra("profilecall")
        val callprofileInt = intent.getIntExtra("profilepic", 0)
        val callprofileNo = intent.getLongExtra("profilenumber", 0)
        val replyText = intent.getStringExtra("replyText")
        Log.d("Testtag", "InsideMAin of call ${callprofileNo}")
        Log.d("Testtag", "Intent ${intent.action}")

        if (intent.action == "audiocall") {
            val bundle = Bundle().apply {
                putString("profilename", profile)
                putInt("profiileno", profileInt)
            }
            navController.navigate(R.id.ringingFragment, bundle)
        }
        if (intent.action == "videocall") {
            val bundle = Bundle().apply {
                putString("profilename", profile)
                putInt("profiileno", profileInt)
            }
            navController.navigate(R.id.videoRingingFragment, bundle)
        }
        if (intent.action == "customvideocall") {

            Log.d("Testtag", "Uri in Main $customuri")
            val bundle = Bundle().apply {
                putString("custom_uri", customuri)
                putString("custom_profile", customprofile)
                putString("custom_profiileno", customno)
            }
            navController.navigate(R.id.customVideoRingingFragment, bundle)
        }
        if (intent.action == "customcall") {

            Log.d("Testtag", "InsideMAin of customcall $customuri")
            val bundle = Bundle().apply {
                putString("custom_uri", customuri)
                putString("custom_profile", customprofile)
            }
            navController.navigate(R.id.customCallRingingFragment, bundle)
        }
        if (intent.action == "chat1") {

            Log.d("Testtag", "Insdie chat")

            val bundle = Bundle().apply {
                Log.d("Testtag", "Insdie bundle")

                Log.d("Testtag", "value of profile${chatprofile}")
                Log.d("Testtag", "value of pic${chatprofileInt}")
                putString("chatprofile", chatprofile)
                putInt("chatno", chatprofileInt)
                putBoolean("fromChatActivity", true)

            }
            navController.navigate(R.id.mainFragment, bundle)
        }
        if (intent.action == "Call1") {
            Log.d("Testtag", "InsideMAin of call ")


            val bundle = Bundle().apply {
                putString("profilecall", callprofile)
                putInt("profilepic", callprofileInt)
                putLong("profilenumber", callprofileNo)
                putBoolean("fromCallActivity", true)
                prefutil.setBool("calls", true)

            }
            navController.navigate(R.id.mainFragment, bundle)
        }
        if (intent.action == "exit") {

            Log.d("Testtag", "InsideMAin of exit")

            navController.navigate(R.id.mainFragment)
        }
        if(intent.action=="sms_chat"){
            PrefUtil(this).setString("reply","")

            Log.d("Testtag", "InsideMAin of sms_chat")
            Log.d("Testtag", "InsideMAin of profile${profile}")
            Log.d("Testtag", "InsideMAin of profileInt${profileInt}")
            Log.d("Testtag", "InsideMAin of reply${replyText}")

            PrefUtil(this).setString("reply",replyText)
            val bundle = Bundle().apply {
                putString("chatprofile", profile)
                putInt("chatno", profileInt)
                putBoolean("fromsms_chat", true)

            }
            navController.navigate(R.id.mainFragment, bundle)
        }
        if(intent.action=="sms_shit"){
            val serviceIntent =
                Intent(this, MessagingForegroundService::class.java).apply {
                    putExtra("profile_name", profile)
                    putExtra("profile_num",profileInt)
                }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.startForegroundService(serviceIntent)
            } else {
                this.startService(serviceIntent)
            }
        }


    }

    override fun onDestroy() {
        intercounter = 0
        seconcounter = 0
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent) {

        Log.d("Testtag", "InsideMAin new itnent ")

        super.onNewIntent(intent)
        Log.d("Testtag", "InsideMAin after itnent ")
        Log.d("Testtag", "Intent${intent} ")
        if (intent.getBooleanExtra("refresh", false) == true) {
            recreate() // Or any refresh logic
        }
        handleIntent(intent)
    }

    fun fadeIn(view: View, duration: Long = 2000) {
        view.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(duration)
                .setListener(null)
        }
    }



}



fun FragmentActivity.navController(navControllerr: Int): NavController {
    return (supportFragmentManager.findFragmentById(navControllerr) as NavHostFragment).navController
}

