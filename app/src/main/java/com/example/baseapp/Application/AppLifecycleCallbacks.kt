package com.example.baseapp.Application

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.baseapp.Application.MyApplication.Companion.appopen_resume
import com.example.baseapp.Application.MyApplication.Companion.count
import com.example.baseapp.Application.MyApplication.Companion.noadshow
import com.example.baseapp.Application.MyApplication.Companion.onstop
import com.example.baseapp.MainActivity
import com.example.baseapp.adsManager.AddIds
import com.example.baseapp.adsManager.AdsManager.Companion.isInter
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd

class AppLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    private var isAdShowing = false
    var counter=0

    //app open
    private var isAdLoaded = false // Flag to check if the open ad is loaded
    private var isAlReadyShow = false
    var openAd: AppOpenAd? = null
    private var isTimeUP = false
    private var fullScreenContentCallback: FullScreenContentCallback? = null

    companion object{
        var currentActivity: Activity?=null
    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // This is called when the activity is created
    }

    override fun onActivityStarted(activity: Activity) {
        // This is called when the activity is started
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
        Log.d("TestTag", "currentActivity${currentActivity}")

        val excludedActivities = setOf("InAppPurchases", "SplashScreen","onbording","AdActivity","VungleActivity","SecondPermissionActivity","AppLovinFullscreenActivity")

        if (!excludedActivities.contains(activity.javaClass.simpleName)) {
//            Log.d("TestTag", "It goes in Resume}")
            Log.d("TestTag", "onstop$onstop")
            Log.d("TestTag", "noadshow$noadshow")

            if (onstop && counter <= count && noadshow && appopen_resume) {
                counter++
                Log.d("TestTag", "Intent to here")
                Log.d("TestTag", "noadshow$noadshow")

                onstop = false
                loadSplashOpenAd(activity)


            }
            else{
                Log.d("TestTag", "noadshow$noadshow")

                noadshow=true
            }

        } else {
            Log.d("TestTag", "Activity AppPurchaseActivity")
        }
    }


    override fun onActivityPaused(activity: Activity) {
        if (isAdShowing) {
//            Log.d("TestTag", "Activity Paused due to Ad: ${activity.javaClass.simpleName}")
            onstop = false

        } else {
//            Log.d("TestTag", "Activity ${activity.javaClass.simpleName} inPause InApp")
            onstop = false

        }
    }

    override fun onActivityStopped(activity: Activity) {
//        Log.d("TestTag", "Activity Stopped")
        onstop = true

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // This is called to save the activity's state
    }

    override fun onActivityDestroyed(activity: Activity) {
        // This is called when the activity is destroyed
        Log.d("TestTag", "Activity Destroyed")
        onstop = false

    }
    fun loadSplashOpenAd(activity: Activity) {
        if (PrefUtil(activity).getBool("is_premium", false) ) {
            Log.e("TESTTAG", "app open not loaded due to premium")
            return
        }

        val loadCallback: AppOpenAd.AppOpenAdLoadCallback =
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.e("TESTTAG", "app open onAdLoaded: $ad")
                    openAd = ad
                    isAdLoaded = true // Set the flag to indicate that the ad is loaded
                    val excludedFragments = setOf("RingingFragment", "VideoRingingFragment","CustomCallRingingFragment")

                    if (activity is MainActivity) {
                        // Get the fragment currently shown inside frame_container
                        val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.frame_container)
                        val currentFragment = navHostFragment?.childFragmentManager?.primaryNavigationFragment
                        val currentFragmentName = currentFragment?.javaClass?.simpleName
                        Log.d("AppLifecycle", "Ad skipped due to fragment: $currentFragmentName")

                        if (currentFragmentName !in excludedFragments) {
                            showSplashOpenAd(activity)
                        } else {
                            Log.d("AppLifecycle", "Ad skipped due to fragment: $currentFragmentName")
                        }
                    } else {
                        // Show ad for other activities
                        showSplashOpenAd(activity)
                    }

                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.e("TESTTAG", "app open onAdFailedToLoad: $p0")
                    // Handle ad loading failure
                }
            }
        val request: AdRequest = AdRequest.Builder().build()
        AppOpenAd.load(
            activity,
            AddIds.onResumeopenAppId(),
            request,
            loadCallback
        )
    }
    fun showSplashOpenAd(activity: Activity) {
        if (PrefUtil(activity).getBool("is_premium", false)) {
            Log.e("TESTTAG", "app open not showed due to Premium")
            return
        }
        if (isAlReadyShow) {
            Log.e("TESTTAG", "app open isAlReadyShow")
            return
        }

        if (isInter) {
            Log.e("TESTTAG", "app open not showed due to isInter")
            return
        }

        Log.e("TESTTAG", "app open called")
        fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {

            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {


            }

            override fun onAdShowedFullScreenContent() {



            }
        }
        openAd?.fullScreenContentCallback = fullScreenContentCallback
        openAd!!.show(activity)
    }



}
