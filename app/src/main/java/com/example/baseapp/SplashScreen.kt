package com.example.baseapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.baseapp.Application.MyApplication
import com.example.baseapp.Application.MyApplication.Companion.appOpenEnabled
import com.example.baseapp.Application.MyApplication.Companion.isNextShow
import com.example.baseapp.Application.MyApplication.Companion.lang_screenenb
import com.example.baseapp.adsManager.AddIds
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.adsManager.AdsManager.Companion.adCount
import com.example.baseapp.adsManager.AdsManager.Companion.showAppopen
import com.example.baseapp.adsManager.GdprConsentClass
import com.example.baseapp.inAppPurchases.GooglePlayBuySubscription
import com.example.baseapp.inAppPurchases.InAppPurchases
import com.example.baseapp.localization.LocalizationActivity
import com.example.baseapp.onbording.onbording
import com.example.baseapp.utils.FirebaseCustomEvents
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivitySplashScreenBinding
import com.example.iwidgets.Utils.AppInfoUtils
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

class SplashScreen : AppCompatActivity() {
    var checksetting = false

    private var handler: Handler? = null
    private var runnable: Runnable? = null


    lateinit var splashbind: ActivitySplashScreenBinding
    val prefutil: PrefUtil = PrefUtil(this)
    val appInfoUtils = AppInfoUtils(this)

    //openad
    private var isAdLoaded = false
    private var isAlReadyShow = false
    private var isTimeUp = false
    private var isAdFailed = false

    private var addfill = false


    var openAd: AppOpenAd? = null


    //gdpr work
    private var fullScreenContentCallback: FullScreenContentCallback? = null
    private val isMobileAdsInitializeCalled = AtomicBoolean(false)
    private lateinit var googleMobileAdsConsentManager: GdprConsentClass
    private var currentTierIndex = 0
    private val tierPriority = listOf(
        AddIds.PriceTier.HIGH,
        AddIds.PriceTier.MEDIUM,
        AddIds.PriceTier.NORMAL
    )

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var selectedLanguage = PrefUtil(this).getString("Language_Localization", "en")
        val locale = Locale(selectedLanguage)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)


        adCount = 0
//        FirebaseApp.initializeApp(this)
        FirebaseCustomEvents(this).createFirebaseEvents(
            "app_launch_splashscreen", "true"
        )
        splashbind = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashbind.root)
        supportActionBar?.hide()
        //init billing client
        GooglePlayBuySubscription.initBillingClient(this)
        GooglePlayBuySubscription.makeGooglePlayConnectionRequest()
        googleMobileAdsConsentManager = GdprConsentClass.getInstance(this@SplashScreen)


//        val handler = Handler(Looper.getMainLooper())
//            handler.postDelayed({
//                nextActivity()
//            }, 5000)

        if (isNetworkAvailable(this)) {
//            AdsManager(applicationContext)
//            loadSplashOpenAd()
            googleMobileAdsConsentManager.gatherConsent(this@SplashScreen) { consentError ->
                if (consentError != null) {
                    Log.e(
                        "TESTTAG",
                        "Error code Consent ${consentError.errorCode}: ${consentError.message}"
                    )
                }

                Log.e(
                    "TESTTAG",
                    "Consent Available ${googleMobileAdsConsentManager.isConsentAvailable}"
                )

                if (googleMobileAdsConsentManager.canRequestAds) {
                    Log.e(
                        "TESTTAG",
                        "Consent can request${googleMobileAdsConsentManager.canRequestAds}"
                    )

                    if (googleMobileAdsConsentManager.isConsentAvailable) {

                        Log.e("TESTTAG", "Consent if")
                        calledonlyInter()


                    } else {
                        Log.e("TESTTAG", "Consent else")
                        callboth()


                    }
                }

                if (googleMobileAdsConsentManager.isPrivacyOptionsRequired) {
                    invalidateOptionsMenu()
                }
            }

        } else {
            Log.e("TESTTAG", "No Internet Available")

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                nextActivity()
            }, 3000)
        }
        if (googleMobileAdsConsentManager.canRequestAds) {
            if (googleMobileAdsConsentManager.isConsentAvailable) {
                Log.e("TESTTAG", "Consent if")
                calledonlyInter()
            } else {
                callboth()
                Log.e("TESTTAG", "Consent else")
            }
        }

        checksetting = prefutil.getBool("Permission")


        val content = getString(R.string.priv)
        val spannableString = SpannableString(content)
        spannableString.setSpan(
            UnderlineSpan(), 0, content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        splashbind.privacy.text = spannableString
        splashbind.version.text = "Version:${getAppVersion(this)}"

        try {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.white_call)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            window.navigationBarColor = resources.getColor(R.color.white_call, theme)

            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = true
            }
        } catch (e: Exception) {
        }


        splashbind.privacy.setOnClickListener {
            appInfoUtils.openPrivacy()
        }


    }

    fun getAppVersion(context: Context): String? {
        return try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            "${pInfo.versionName}"
        } catch (e: PackageManager.NameNotFoundException) {
            "V 1.0"
        }
    }


    private fun arePermissionsGranted(context: Context): Boolean {
        var permissioncheck = false
        val requiredPermissions = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(
                android.Manifest.permission.CAMERA,
            )

            Build.VERSION.SDK_INT == Build.VERSION_CODES.S -> arrayOf(
                android.Manifest.permission.CAMERA,
            )

            else -> arrayOf(
                android.Manifest.permission.CAMERA,
            )
        }

        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    context, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("TestTag", "$ permission not${permission}")


                permissioncheck = false
            } else {
                permissioncheck = true

            }
        }
        return permissioncheck
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

    fun loadSplashOpenAdWithFlooring() {
        PrefUtil(this).setBool("isShowed", false)
        if (!MyApplication.isEnabled || !appOpenEnabled || !showAppopen) return

        if (MyApplication.isFlooring) {
            loadNextTierAd()
        } else
            loadSplashOpenAd()
    }

    fun loadSplashOpenAd() {
        PrefUtil(this).setBool("isShowed", false)
        if (!MyApplication.isEnabled || !appOpenEnabled || !showAppopen) {
            return
        }
        val loadCallback: AppOpenAd.AppOpenAdLoadCallback =
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.e("remote", "onAdLoaded:")
                    isAdLoaded = true
                    openAd = ad
                    showSplashOpenAd()
                    // Set the flag to indicate that the ad is loaded
                    Log.e("TESTTAG", "onAdLoaded Appopen status: $openAd")
                    Log.e("TESTTAG", "onAdLoaded Appopen status: $isAdLoaded")
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.e("TESTTAG", "onAdFailedToLoad: $p0")
//                    addfill=true
                    isAdFailed = true
//                    nextActivity()
//                    isAdFailed = true
//                    cancelHandler()
                    Log.e("TESTTAG", "onAdFailedToLoad isAdFailed status: $isAdFailed")

                    // Handle ad loading failure
                }
            }
        val request: AdRequest = AdRequest.Builder().build()
        AppOpenAd.load(
            this,
            AddIds.openAppId(),
            request,
//            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            loadCallback
        )
    }

    private fun loadNextTierAd() {
        if (currentTierIndex >= tierPriority.size) {
            Log.e("AppOpenAd", "All pricing tiers failed.")
            return
        }

        val tier = tierPriority[currentTierIndex]
        val adUnitId = AddIds.openAppId(tier)
        Log.e("AppOpenAd", "Trying tier: $tier with adUnitId: $adUnitId")

        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            this,
            adUnitId,
            request,
//            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.e("AppOpenAd", "Ad loaded from tier: $tier")
                    FirebaseCustomEvents(this@SplashScreen).createFirebaseEvents(
                        "appopen_loaded_$version",
                        "$tier"
                    )
                    isAdLoaded = true
                    openAd = ad
                    showSplashOpenAd()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(
                        "AppOpenAd",
                        "Failed to load from tier: $tier, trying next. Error: $error"
                    )

                    currentTierIndex++
                    loadNextTierAd()


                }
            }
        )
    }

    fun showSplashOpenAd() {
        if (isAlReadyShow) return
        Log.e("TESTTAG", "isNextShow $isNextShow: ")

        if (!isNextShow) {
            if (isTimeUp) {
                Log.e("TESTTAG", "isTimeUp true: ")
                return
            }
        }

//        if (this@SplashActivity !is SplashActivity)
//            Log.e("AdManagerAds", "not SplashActivity:")
//            return

        fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                appOpenShow = true
                Log.e("TESTTAG", "onAddismissed")
//                if (!isNextShow) {}
                if (isTimeUp) {
                } else {
                    nextActivity()
                }
                Log.e("TESTTAG", "isTimeUp true: ")
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                Log.e("TESTTAG", "onAdFailedToShowFullScreenContent")
                appOpenShow = true
                if (!isFinishing) {
//                    nextActivity()
                }
            }

            override fun onAdShowedFullScreenContent() {
//                splashbind.splashContainer.setBackgroundColor(Color.BLACK)
//                window.statusBarColor = Color.BLACK
//                window.navigationBarColor = Color.BLACK
//                window.decorView.setBackgroundColor(Color.BLACK)
                appOpenShow = false
//                splashbind.imageView4.visibility = View.GONE
//                splashbind.textView7.visibility = View.GONE
//                splashbind.mainhead.visibility = View.GONE
//                splashbind.textView10.visibility = View.GONE
//                splashbind.permissionbtn.visibility = View.GONE
//                splashbind.animationView.visibility = View.GONE
//                splashbind.privacy.visibility = View.GONE
//                splashbind.version.visibility = View.GONE


                Log.e("TESTTAG", "onAdShowedFullScreenContent")
            }
        }
        openAd?.fullScreenContentCallback = fullScreenContentCallback
        openAd!!.show(this)
        isAlReadyShow = true
    }

    fun calledonlyInter() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            Log.e("TESTTAG", "called for only one when consent")

            return
        }
        AdsManager(applicationContext)

        if (!PrefUtil(this).getBool("is_premium", false)) {
            Handler().postDelayed({

                AdsManager.loadInterstitial(this)


                if (showAppopen) {
                    Log.e("remote", "showAppopen true: ")
                    loadSplashOpenAdWithFlooring()
                }
            }, 3300)
        }
        // load app open after giving consent


        proceedToMainActivity()
    }


    fun callboth() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            Log.e("TESTTAG", "1called other than consent")
            return
        }
        Log.e("TESTTAG", "called other than consent")
        AdsManager(applicationContext)
        if (!PrefUtil(this).getBool("is_premium", false)) {

            Handler().postDelayed({

                AdsManager.loadInterstitial(this)

                if (showAppopen) {
                    Log.e("remote", "showAppopen true: ")
                    loadSplashOpenAdWithFlooring()
                }
            }, 3300)
        }
        proceedToMainActivity()
    }

    fun nextActivity() {
        Log.d("TestTag", "$ permission${arePermissionsGranted(this)}")

//        if (arePermissionsGranted(this)) {
//            if (Settings.System.canWrite(this)) {

        if (prefutil.getInt("onboardingTrue", 0) == 1) {
            if (PrefUtil(this).getBool("is_premium", false)) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {

                startActivity(Intent(this, InAppPurchases::class.java))
                finish()
            }

        } else {

            if(lang_screenenb)
            {
                val intent = Intent(this, LocalizationActivity::class.java)
                intent.putExtra("splash", true)
                startActivity(intent)
                finish()

            }
            else{
                startActivity(Intent(this, onbording::class.java))
                finish()
            }


        }
        //
        isTimeUp = true

    }


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }


    private fun cancelHandler() {
        handler?.removeCallbacks(runnable!!)
    }

    fun proceedToMainActivity() {

        Handler().postDelayed({
            Log.e("TESTTAG", "isAdFailed before if: $isAdFailed")

            if (!isAdFailed) {
                Log.e("TESTTAG", "isAdLoaded before if: $isAdLoaded")

                if (!isAdLoaded) {
                    Log.e("TESTTAG", "isAdLoaded if: $isAdLoaded")
                    nextActivity()
                    isTimeUp = true

                }
            } else {
                Log.e("AddCall", "going in else of no add")
                nextActivity()
            }
        }, 17000)
    }

    override fun onBackPressed() {
    }


    companion object {
        var version = "19"
        var appOpenShow = false

    }


}