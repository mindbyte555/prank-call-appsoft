package com.example.baseapp.adsManager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.NonNull
import com.example.baseapp.Application.MyApplication.Companion.bannerEnabled
import com.example.baseapp.Application.MyApplication.Companion.bannerRecEnabled
import com.example.baseapp.Application.MyApplication.Companion.interstitialEnabled
import com.example.baseapp.Application.MyApplication.Companion.isEnabled
import com.example.baseapp.Application.MyApplication.Companion.nativeEnabled
import com.example.baseapp.adsManager.AddIds.Companion.MEDIUM_RECTANGLE
import com.example.baseapp.adsManager.AddIds.Companion.MEDIUM_RECTANGLE_Test
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.BuildConfig
import com.example.fakecall.R
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions


@Suppress("DEPRECATION")
class AdsManager(val context: Context) {
    companion object {
        private var adView: AdView? = null
        private var reloadones: Boolean = true
        var native_id = ""
        var adCount = 0
        var configcounter = 6
        var showNative = true
        var showCollape = true
        var showBanner = true
        var showAppopen = true
        var countInc = false
        public var reloadOnceFailed = true
        var isInter = false

        var bannerLoaded: Boolean = false

        private var hasRetriedLoad = false

        fun getAdSize(ad_view_container: RelativeLayout, context: Context): AdSize {
            val display = (context as Activity).windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = ad_view_container.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }
            val adWidth = (adWidthPixels / density).toInt()
//            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, 360)
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
        }


        fun loadBannerAd(
            adLayout: RelativeLayout?, context: Context, listener: AdmobBannerAdListener
        ): AdView? {
            if (PrefUtil(context).getBool("is_premium", false) || !bannerEnabled) {
                Log.e("TESTTAG", "showBannerAd not showed to premium users")
                adLayout?.visibility = View.GONE
                return null
            }
            if (adLayout == null) return null
            adLayout.removeAllViews()
            adLayout.visibility = View.VISIBLE
            val adRequest = AdRequest.Builder().build()

            var adView = AdView(context)

            if (BuildConfig.DEBUG) {
                adView!!.adUnitId = AddIds.BANNER_TEST
            } else {
                adView!!.adUnitId = AddIds.BANNER
            }
            adView.setAdSize(getAdSize(adLayout, context))

            adLayout.addView(adView)
            adView.loadAd(adRequest)
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    bannerLoaded = true
                    Log.e("TESTTAG", "collapsible banner loaded")
                    listener.onAdLoaded()
                    adLayout.visibility = View.VISIBLE
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    Log.e("TESTTAG", "collapsible banner load failed")
                    super.onAdFailedToLoad(p0)
                    listener.onAdFailed()
                }
            }
            return adView
        }

        fun loadMediumRectangleBannerAd(
            adLayout: RelativeLayout?, context: Context, listener: AdmobBannerAdListener
        ): AdView? {
            if (PrefUtil(context).getBool("is_premium", false) || !bannerRecEnabled) {
                Log.e("TESTTAG", "loadMediumRectangleBannerAd not shown to premium users")
                adLayout?.visibility = View.GONE
                return null
            }
            if (adLayout == null) return null
            adLayout.removeAllViews()
            adLayout.visibility = View.VISIBLE
            val adRequest = AdRequest.Builder().build()
            var adView = AdView(context)
//            adView = AdView(context)

            if (BuildConfig.DEBUG) {
                adView.adUnitId = MEDIUM_RECTANGLE_Test
            } else {
                adView.adUnitId = MEDIUM_RECTANGLE
            }

            adView.setAdSize(AdSize.MEDIUM_RECTANGLE)

            adLayout.addView(adView)
            adView.loadAd(adRequest)
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    bannerLoaded = true
                    Log.e("TESTTAG", "AdManager Banner Ad Loaded")
                    listener.onAdLoaded()
                    adLayout.visibility = View.VISIBLE
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    Log.e("TESTTAG", "AdManager Banner Ad Failed To Load")
                    super.onAdFailedToLoad(p0)
                    listener.onAdFailed()
                }
            }
            return adView
//            adLayout.visibility = View.VISIBLE
//
//            val adRequest = AdRequest.Builder().build()
//
//            val adView = AdView(context)
//
//            adView.adUnitId = if (BuildConfig.DEBUG) {
//                AddIds.MEDIUM_RECTANGLE_Test
//            } else {
//                AddIds.MEDIUM_RECTANGLE
//            }
//            adView.setAdSize(AdSize.MEDIUM_RECTANGLE)
//
//            adLayout.addView(adView)
//            adView.loadAd(adRequest)
//            adView.adListener = object : AdListener() {
//                override fun onAdLoaded() {
//                    super.onAdLoaded()
//                    bannerLoaded = true
//                    Log.e("TESTTAG", "Medium Rectangle banner loaded")
//                    listener.onAdLoaded()
//                    adLayout.visibility = View.VISIBLE
//                }
//
//                override fun onAdFailedToLoad(p0: LoadAdError) {
//                    Log.e("TESTTAG", "Medium Rectangle banner load failed")
//                    super.onAdFailedToLoad(p0)
//                    listener.onAdFailed()
//                }
//            }
//            return adView
        }


        fun loadCollapsible(
            adLayout: RelativeLayout, context: Context, listener: AdmobBannerAdListener,
        ): AdView? {

            if (PrefUtil(context).getBool("is_premium", false) || !isEnabled ||  !showCollape) {
                adLayout?.visibility = View.GONE
                return null
            }


            if (adLayout == null) return null
            adLayout.removeAllViews()
            Log.e("TESTTAG", "adLayout$adLayout")

            adLayout.visibility = View.VISIBLE
            adView = context?.let { AdView(it) }

            if (BuildConfig.DEBUG) {
                adView!!.adUnitId = AddIds.BANNER_TEST
            } else {
                adView!!.adUnitId = AddIds.BANNER
                //adView!!.adUnitId = AddIds.Collapse_BANNER
            }

//            adLayout.addView(adView)


//            adLayout.post {
//                // Get proper ad size after layout is measured
//                getAdSize(adLayout, context)?.let { adSize ->
//                    adView!!.setAdSize(adSize)
//
//                    // Load the ad after size is set
//                    val extras = Bundle()
//                    extras.putString("collapsible", "top")
//                    val adRequest =
//                        AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter::class.java, extras).build()
//
//                    adView!!.loadAd(adRequest)
//                }
//            }

            getAdSize(adLayout, context)?.let { adView!!.setAdSize(it) }

            val extras = Bundle()
            extras.putString("collapsible", "top")
            val adRequest =
                AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter::class.java, extras).build()

            adLayout.addView(adView)
            adView!!.loadAd(adRequest)
            adView!!.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.e("TESTTAG", "Collapsible Banner Ad Loaded")
                    listener.onAdLoaded()
                    adLayout.visibility = View.VISIBLE
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    Log.e("TESTTAG", "onAdFailedToLoad$p0")

                    Log.e("TESTTAG", "Collapsible Banner Ad Failed To Load")
                    super.onAdFailedToLoad(p0)
                    listener.onAdFailed()
                }
            }
            return adView
        }

        private var isAdLoading = false

        fun loadInterstitial(context: Context) {
            if (!interstitialEnabled || mInterstitialAd   != null || !isEnabled  || PrefUtil(context).getBool("is_premium", false))
                {
                    Log.e("iaminins", "loadInterstitial: return" )
                    return
                }

                isAdLoading = true
                val adRequest = AdRequest.Builder().build()
                val adsId =
                    if (BuildConfig.DEBUG) AddIds.INTERSTITIAL_TEST else AddIds.INTERSTITIAL_RELEASE


                InterstitialAd.load(
                    context,
                    adsId,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(ad: InterstitialAd) {
                            mInterstitialAd = ad
                            isAdLoading = false
                            hasRetriedLoad = false
                            Log.e("iaminins", "interstitial loaded")
                        }

                        override fun onAdFailedToLoad(error: LoadAdError) {
                            mInterstitialAd = null
                            isAdLoading = false
                            Log.e("iaminins", "interstitial failed: ${error.message}")

                            // Retry loading only once
                            if (!hasRetriedLoad) {
                                hasRetriedLoad = true
                                loadInterstitial(context)
                            }
                        }
                    })
            }


        fun showInterstitial(
            re: Boolean = true,
            activity: Activity,
            listener: InterstitialAdListener
        ) {
            // 💎 Skip ad for premium users
            if (PrefUtil(activity).getBool("is_premium", false))
            {
                Log.e("iaminins", "premium user, interstitial not shown")
                listener.onAdClosed()
                return
            }

            if (mInterstitialAd == null) {
                Log.e("iaminins", "elseInterstitial (no ad)")
                listener.onAdClosed()
                return
            }

            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    Log.e("iaminins", "interstitial Ad showed fullscreen content.")
                    isInterstitialVisible = true
                    mInterstitialAd = null
                    isInter=true

                    // Preload next ad immediately
                    if (re) loadInterstitial(activity)
                }

                override fun onAdDismissedFullScreenContent() {
                    isInter=false

                    Log.e("iaminins", "interstitial Ad was dismissed.")
                    isInterstitialVisible = false
                    listener.onAdClosed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    isInter=false

                    Log.e("iaminins", "interstitial Ad failed to show.")
                    isInterstitialVisible = false
                    mInterstitialAd = null

                    // Retry load
                    loadInterstitial(activity)
                    listener.onAdClosed()
                }
            }

            Log.e("iaminins", "ifInterstitial")
            mInterstitialAd?.show(activity)
        }


        //        fun loadInterstitial(context: Context) {
//            if (adCount >= configcounter) {
//                return
//            }
//            var adsid: String
//            if (BuildConfig.DEBUG) {
//                adsid = AddIds.INTERSTITIAL_TEST
//            } else {
//                adsid = AddIds.INTERSTITIAL_RELEASE
//            }
//            var a = AdRequest.Builder().build()
//            InterstitialAd.load(context, adsid, a, object : InterstitialAdLoadCallback() {
//                override fun onAdLoaded(ad: InterstitialAd) {
//
//                    mInterstitialAd = ad
//                    adCount++
//                    Log.e("iaminins", "interstitial loaded")
//                    isInterstitialVisible = false
//                }
//
//                override fun onAdFailedToLoad(p0: LoadAdError) {
//                    if (reloadOnceFailed) {
//                        loadInterstitial(context)
//                        reloadOnceFailed = false
//                    }
//                    mInterstitialAd = null
//                    Log.e("iaminins", "interstitial failed")
//                    isInterstitialVisible = false
//                    //
//                }
//            })
//        }
//
//        fun showInterstitial(
//            re: Boolean = true, activity: Activity, listener: InterstitialAdListener
//        ) {
//            reload = re
////            if (PrefUtil(activity).getBool("is_premium", false) || !showInterstitial) {
////                Log.e("iaminins", "interstitial not showed to premium users")
////
////                listener.onAdClosed()
////                return
////            }
//
//            if (mInterstitialAd != null) {
//                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
//                    override fun onAdDismissedFullScreenContent() {
//                        countInc = true
//
//                        Log.e("iaminins", "interstitial Ad was dismissed.")
//                        mInterstitialAd = null
//                        listener.onAdClosed()
////                        if (reload)
//                        loadInterstitial(activity)
//                        isInterstitialVisible = false
//
//                    }
//
//                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
//                        Log.e("iaminins", "interstitial Ad failed to show.")
//                        countInc = false
//                        super.onAdFailedToShowFullScreenContent(p0)
//                        mInterstitialAd = null
//                        isInterstitialVisible = false
//
//                        // 🔁 Load new ad since it failed to show
//                        loadInterstitial(activity)
//                    }
//
//                    override fun onAdShowedFullScreenContent() {
//                        Log.e("iaminins", "interstitial Ad showed fullscreen content.")
//                        mInterstitialAd = null
//                        isInterstitialVisible = true
//
//                        // 🔁 Load next ad immediately after showing current one
//                        loadInterstitial(activity)
//                    }
//
//                }
//                mInterstitialAd!!.show(activity)
//                Log.e("iaminins", "ifInterstitial")
//            } else {
//                countInc = false
//
//                Log.e("iaminins", "elseInterstitial")
//                listener.onAdClosed()
//            }
//        }
        fun loadAdaptiveBannerAd(
            adLayout: RelativeLayout?, context: Context, listener: AdmobBannerAdListener
        ): AdView? {
            if (PrefUtil(context).getBool("is_premium", false)|| !isEnabled || !showBanner) {
                Log.e("TESTTAG", "loadAdaptiveBannerAd not showed to premium users")
                adLayout?.visibility = View.GONE
                return null
            }
            if (adLayout == null) return null
            adLayout.removeAllViews()
            adLayout.visibility = View.VISIBLE

            adView = context.let { AdView(it) }

            if (BuildConfig.DEBUG) {
                adView!!.adUnitId = AddIds.BANNER_TEST // Use a test ID
            } else {
                adView!!.adUnitId =
                    AddIds.ADAPTIVE_BANNER // Replace with your actual Adaptive Banner Ad Unit ID
            }

            adView!!.setAdSize(getAdSize(adLayout, context))

            adLayout.addView(adView)
            adView!!.loadAd(AdRequest.Builder().build())
            adView!!.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    bannerLoaded = true
                    Log.e("TESTTAG", "Adaptive banner loaded")
                    listener.onAdLoaded()
                    adLayout.visibility = View.VISIBLE
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    Log.e("TESTTAG", "Adaptive banner load failed: ${p0.message}")
                    super.onAdFailedToLoad(p0)
                    listener.onAdFailed()
                }
            }
            return adView
        }


        @SuppressLint("ResourceType")
        fun loadNative(
            container: RelativeLayout,
            activity: Activity,
            listener: AdmobBannerAdListener,
            type: NativeAdType
        ) {
            if (PrefUtil(activity).getBool("is_premium", false)|| !isEnabled || !showNative||!nativeEnabled) {
                Log.e("TESTTAG", "native not showed to premium users")
                return
            }
            if (BuildConfig.DEBUG) {
                native_id = AddIds.Native_TEST
            } else {
                native_id = AddIds.Native
            }
            Log.e("TESTTAG", "Native Ad Load Caled")
            var builder: AdLoader.Builder
            builder = AdLoader.Builder(activity, native_id)

            val layoutId2 =
                if (type === NativeAdType.MEDIUM) R.layout.native_ad_medium else if (type === NativeAdType.SMALL) R.layout.native_ad_small else R.layout.native_ad_small
            val view: View = activity.layoutInflater.inflate(layoutId2, null)
            val templateView: TemplateView = view.findViewById(R.id.templateView)
            builder.forNativeAd { nativeAd: NativeAd ->
                if (activity.isDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                    nativeAd.destroy()
                    return@forNativeAd
                }
                templateView.setNativeAd(nativeAd)
                container.removeAllViews()
                container.addView(view)
            }
            val videoOptions = VideoOptions.Builder().setStartMuted(true).build()
            val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()
            builder.withNativeAdOptions(adOptions)
            val adLoader = builder.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(@NonNull loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    Log.e("TESTTAG", "Native Ad failed to show. ${loadAdError}")
                    listener.onAdFailed()
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    listener.onAdLoaded()
                    Log.e("TESTTAG", "Native Ad Loaded")
                }
            }).build()
            adLoader.loadAd(AdRequest.Builder().build())
        }

        @SuppressLint("ResourceType")
        fun ExitloadNative(
            container: RelativeLayout,
            activity: Activity,
            id: String,
            listener: AdmobBannerAdListener,
            type: NativeAdType
        ) {
            if (PrefUtil(activity).getBool("is_premium", false)|| !isEnabled || !showNative||!nativeEnabled) {
                Log.e("TESTTAG", "native not showed to premium users")
                return
            }
            if (BuildConfig.DEBUG) {
                native_id = AddIds.Native_TEST
            } else {
                native_id = AddIds.Native
            }
            Log.e("TESTTAG", "Native Ad Load Caled")
            var builder: AdLoader.Builder
            builder = AdLoader.Builder(activity, native_id)

            val layoutId2 =
                if (type === NativeAdType.MEDIUM) R.layout.native_ad_medium else if (type === NativeAdType.SMALL) R.layout.native_ad_small else R.layout.native_ad_small
            val view: View = activity.layoutInflater.inflate(layoutId2, null)
            val templateView: TemplateView = view.findViewById(R.id.templateView)
            builder.forNativeAd { nativeAd: NativeAd ->
                if (activity.isDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                    nativeAd.destroy()
                    return@forNativeAd
                }
                templateView.setNativeAd(nativeAd)
                container.removeAllViews()
                container.addView(view)
            }
            val videoOptions = VideoOptions.Builder().setStartMuted(true).build()
            val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()
            builder.withNativeAdOptions(adOptions)
            val adLoader = builder.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(@NonNull loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    Log.e("TESTTAG", "Native Ad failed to show. ${loadAdError}")
                    listener.onAdFailed()
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    listener.onAdLoaded()
                    Log.e("TESTTAG", "Native Ad Loaded")
                }
            }).build()
            adLoader.loadAd(AdRequest.Builder().build())
        }


        var reload = true
        private var myNativeAd: NativeAd? = null
        var mInterstitialAd: InterstitialAd? = null
        var mInterstitialGetStartedAd: InterstitialAd? = null
        private var adsManagerInstance: AdsManager? = null
        private var mContext: Context? = null
        var isInterstitialVisible = false

    }

    val instance: AdsManager
        get() {
            if (adsManagerInstance == null) {
                context.let {
                    adsManagerInstance = AdsManager(it)
                }
            }
            return adsManagerInstance!!
        }

    init {
        if (mContext == null) {
            mContext = context
            MobileAds.initialize(context) { initializationStatus ->
                val statusMap = initializationStatus.adapterStatusMap
                for (adapterClass in statusMap.keys) {
                    val status = statusMap[adapterClass]
                    Log.e(
                        "MyApp", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status!!.description, status.latency
                        )
                    )
                }
            }

            MobileAds.openAdInspector(context) { error ->
                // Error will be non-null if ad inspector closed due to an error.
            }
        }
    }


    interface InterstitialAdListener {
        fun onAdClosed()
    }

    interface AdmobBannerAdListener {
        fun onAdLoaded()
        fun onAdFailed()
    }

    enum class NativeAdType {
        MEDIUM, SMALL, SMALLBANNER
    }
}