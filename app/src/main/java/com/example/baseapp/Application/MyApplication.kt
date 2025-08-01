package com.example.baseapp.Application

import android.app.Application
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.baseapp.Firebase.RemoteConfigManager
import com.example.baseapp.adsManager.AddIds.Companion.APP_OPEN_HIGH
import com.example.baseapp.adsManager.AddIds.Companion.APP_OPEN_MEDIUM
import com.example.baseapp.adsManager.AddIds.Companion.APP_OPEN_NORMAL
import com.example.baseapp.adsManager.AddIds.Companion.BANNER
import com.example.baseapp.adsManager.AddIds.Companion.Collapse_BANNER
import com.example.baseapp.adsManager.AddIds.Companion.Exit_Native
import com.example.baseapp.adsManager.AddIds.Companion.INTERSTITIAL_RELEASE
import com.example.baseapp.adsManager.AddIds.Companion.MEDIUM_RECTANGLE
import com.example.baseapp.adsManager.AddIds.Companion.Native
import com.example.baseapp.utils.FirebaseCustomEvents
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MyApplication : Application(), LifecycleObserver {
    private lateinit var remoteConfigManager: RemoteConfigManager
    val prefutil: PrefUtil = PrefUtil(this)
    var mode: String = ""
    lateinit var appLifecycleCallbacks: AppLifecycleCallbacks

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        FirebaseApp.initializeApp(applicationContext)
        FirebaseCustomEvents(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        appLifecycleCallbacks = AppLifecycleCallbacks()
        registerActivityLifecycleCallbacks(appLifecycleCallbacks)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        remoteConfigManager = RemoteConfigManager()
        fetchAndApplyRemoteConfig()
        setupConfigUpdateListener()
    }

    //ANR Logs find out
//        val policy = StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build()
//        StrictMode.setThreadPolicy(policy)

    //
//        if (prefutil.getString("night") != null) {
//            mode = prefutil.getString("night")!!
//        }
//
//
//        if (mode == "day") {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//
//        } else if (mode == "dark") {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//
//        }
//
//
//    }
//    private fun setupConfigUpdateListener() {
//        val keysToWatch = setOf("adLoadCounter","idNative","idBanner","idInterstitial","idBannerCollapse","showNative","showInterstitial","showBanner","showcollapse","idAppopen","showAppopen")
//
//        // Set up the listener with the dynamic keys
//        remoteConfigManager.addConfigUpdateListener(
//            keysToWatch = keysToWatch,
//            onUpdate = { updatedKeys ->
//                updatedKeys.forEach { key ->
//                    Log.e("remote", "$key updated successfully")
//                }
//                applyRemoteConfigValues() // Called once after handling all keys
//            },
//            onError = { error ->
//                Log.w("splash", "Config update error", error)
//
//            }
//        )
//    }
//    private fun fetchAndApplyRemoteConfig() {
//        remoteConfigManager.fetchRemoteConfig { isSuccess ->
//            if (isSuccess) {
//                Log.e("remote", "fetched sccessfully")
//                applyRemoteConfigValues()
//
//            } else {
//                configcounter =6
//                BANNER="ca-app-pub-3173814755166534/1481963124"
//                INTERSTITIAL_RELEASE = "ca-app-pub-3173814755166534/8685065687"
//                Native = "ca-app-pub-3173814755166534/8944085987"
//                showNative = true
//                showInterstitial = true
//                showBanner = true
//                showCollape = true
//                Collapse_BANNER="ca-app-pub-3173814755166534/4905940454"
//                APP_OPEN = "ca-app-pub-3173814755166534/1381290733"
//                showAppopen=true
//            }
//        }
//    }
//    private fun applyRemoteConfigValues() {
//
//        configcounter = remoteConfigManager.getString("adLoadCounter").trim().toInt()
//        BANNER = remoteConfigManager.getString("idBanner")
//        INTERSTITIAL_RELEASE = remoteConfigManager.getString("idInterstitial")
//        Collapse_BANNER=remoteConfigManager.getString("idBannerCollapse")
//        Native = remoteConfigManager.getString("idNative")
//        showNative = remoteConfigManager.getBoolean("showNative")
//        showInterstitial = remoteConfigManager.getBoolean("showInterstitial")
//        showBanner = remoteConfigManager.getBoolean("showBanner")
//        showCollape = remoteConfigManager.getBoolean("showcollapse")
//        APP_OPEN = remoteConfigManager.getString("idAppopen")
//        showAppopen=remoteConfigManager.getBoolean("showAppopen")
//
//
//        Log.e("remote", "configcounter : $configcounter" )
//        Log.e("remote", "BANNER : $BANNER" )
//        Log.e("remote", "INTERSTITIAL_RELEASE : $INTERSTITIAL_RELEASE" )
//        Log.e("remote", "Native : $Native" )
//        Log.e("remote", "Collapse_BANNER : $Collapse_BANNER" )
//        Log.e("remote", "showNative : $showNative" )
//        Log.e("remote", "showInterstitial : $showInterstitial" )
//        Log.e("remote", "showBanner : $showBanner" )
//        Log.e("remote", "showCollape : $showCollape" )
//        Log.e("remote", "APP_OPEN : $APP_OPEN" )
//        Log.e("remote", "showAppopen : $showAppopen" )
//
//    }
//    companion object {
//        fun View.clickWithDebounce(debounceTime: Long = 400L, action: () -> Unit) {
//            this.setOnClickListener(object : View.OnClickListener {
//                private var lastClickTime: Long = 0
//
//                override fun onClick(v: View) {
//                    if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
//                    else action()
//
//                    lastClickTime = SystemClock.elapsedRealtime()
//                }
//            })
//        }
    private fun fetchAndApplyRemoteConfig() {
        remoteConfigManager.fetchRemoteConfig { isSuccess ->
            if (isSuccess) {
                applyRemoteConfigValues()
            }
        }
    }

    private fun applyRemoteConfigValues() {
        CoroutineScope(Dispatchers.IO).launch {
            parseAdConfig(remoteConfigManager.getString("ads_config"))
        }
    }

    private fun parseAdConfig(jsonString: String) {
        try {
            val jsonObject = JSONObject(jsonString)

            isEnabled = jsonObject.getBoolean("enabled")
            if (isEnabled) {
                val appOpen = jsonObject.getJSONObject("AppOpen")
                appOpenEnabled = appOpen.getBoolean("enabled")
                isFlooring = appOpen.getBoolean("isFlooring")
                isNextShow = appOpen.getBoolean("isNextShow")
                APP_OPEN_NORMAL = appOpen.getString("ad_unit_id_all_prices")
                APP_OPEN_MEDIUM = appOpen.getString("ad_unit_id_medium")
                APP_OPEN_HIGH = appOpen.getString("ad_unit_id_high")


                val interstitial = jsonObject.getJSONObject("Interstitial")
                interstitialEnabled = interstitial.getBoolean("enabled")
                INTERSTITIAL_RELEASE = interstitial.getString("ad_unit_id")
                interstitialCounter = interstitial.getInt("load_counter")

                val banner = jsonObject.getJSONObject("Banner")
                bannerEnabled = banner.getBoolean("enabled")
                BANNER = banner.getString("ad_unit_id")
                bannerCollapsible = banner.getBoolean("Banner_Collapsible_Enabled")

                val nativeAd = jsonObject.getJSONObject("Native")
                nativeEnabled = nativeAd.getBoolean("enabled")
                Native = nativeAd.getString("ad_unit_id")

                val bannerRect = jsonObject.getJSONObject("RectBanner")
                MEDIUM_RECTANGLE = bannerRect.getString("ad_unit_id")
                bannerRecEnabled = bannerRect.getBoolean("enabled")


                val ExitNative = jsonObject.getJSONObject("Exit_banner")
                Exit_Native = ExitNative.getString("ad_unit_id")
                exitnativeEnabled = ExitNative.getBoolean("enabled")


                val doneInter = jsonObject.getJSONObject("Done_inter")
                done_id = doneInter.getString("ad_unit_id")
                done_enabled = doneInter.getBoolean("enabled")

                val lang_screen = jsonObject.getJSONObject("Lang_screen")
                lang_screenenb = lang_screen.getBoolean("enabled")


                val unlocked = jsonObject.getJSONObject("Unlockscreen")
                unlockscreen = unlocked.getBoolean("enabled")

                val internet = jsonObject.getJSONObject("InternetDialog")
                internetbox = internet.getBoolean("enabled")

                val appOpen_control = jsonObject.getJSONObject("Appopen_Resume")
                appopenresumeid = appOpen_control.getString("ad_unit_id")
                count = appOpen_control.getInt("load_counter")
                appopen_resume = appOpen_control.getBoolean("enabled")

            }
            // Logging values
            if (BuildConfig.DEBUG) {
                Log.d("TAG", "Enabled: $isEnabled")
                Log.d("TAG", "App Open - Enabled: $appOpenEnabled")
                Log.d(
                    "TAG",
                    "Interstitial - Enabled: $interstitialEnabled, Counter: $interstitialCounter"
                )
                Log.d(
                    "TAG", "Banner - Enabled: $bannerEnabled, Collapsible: $bannerCollapsible"
                )
                Log.d("TAG", "Native - Enabled: $nativeEnabled")
                Log.d("TAG", "Banner Ret language - Enabled: $bannerRecEnabled")
                Log.d(
                    "TESTTAG",
                    "ADS IDS appOpenID: $APP_OPEN_HIGH medium $APP_OPEN_MEDIUM normal $APP_OPEN_NORMAL, inter:$INTERSTITIAL_RELEASE, banner: $BANNER, collapsible:$Collapse_BANNER, native: $Native, rectangular: $MEDIUM_RECTANGLE"
                )
            }

        } catch (e: Exception) {
            Log.e("TAG", "Error parsing JSON: ${e.message}")
        }
    }

    private fun setupConfigUpdateListener() {
        val keysToWatch = setOf("ads_config")
        remoteConfigManager.addConfigUpdateListener(keysToWatch = keysToWatch,
            onUpdate = { updatedKeys ->
                when {
                    updatedKeys.contains("ads_config") -> {
                        applyRemoteConfigValues()
                    }
                }
            },
            onError = { error ->
                Log.w("MainActivity", "Config update error", error)

            })
    }

    companion object {
        var isEnabled = false
        var appOpenEnabled = true
        var isFlooring = true
        var isNextShow = false
        var interstitialEnabled = false
        var interstitialCounter = 5
        var bannerEnabled = false
        var bannerCollapsible = false
        var nativeEnabled = false
        var bannerRecEnabled = false
        var exitnativeEnabled = false
        var done_enabled = false
        var done_id = ""
        var lang_screenenb = true
        var review = false
        var appopenresumeid="ca-app-pub-3173814755166534/6834069254"
        var count = 2
        var appopen_resume = true
        var unlockscreen=true
        var internetbox=true
        var noadshow=true
        var onstop = false

        var isAdaptiveBannerEnabled = true
        fun View.clickWithDebounce(debounceTime: Long = 400L, action: () -> Unit) {
            this.setOnClickListener(object : View.OnClickListener {
                private var lastClickTime: Long = 0
                override fun onClick(v: View?) {
                    if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
                    else action()

                    lastClickTime = SystemClock.elapsedRealtime()
                }


            })
        }
    }
}



