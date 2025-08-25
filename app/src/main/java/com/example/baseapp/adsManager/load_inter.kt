package com.example.baseapp.adsManager

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Handler
import android.util.Log
import android.widget.TextView
import com.example.baseapp.utils.FirebaseCustomEvents
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object Load_inter
{
    var mInterstitialAd: InterstitialAd? = null
    var mContext: Context? = null
    var mActivity: Activity? = null
    var mInterstitialID: String? = null
    var logTag: String = "ADS_MANAGER"
    var mActionOnAdClosedListener: ActionOnAdClosedListener? = null
    var isAdDecided: Boolean = false
    var isInterstitalIsShowing: Boolean = false
    var isProcessing: Boolean = false

    var stopInterstitial: Boolean = false
    var timerCalled: Boolean = false
    var dialog: Dialog? = null


    fun request_interstitial(
        context: Context?,
        activity: Activity?,
        interstitial_id: String?,
        showSuccessEvent: String = null.toString(),
        actionOnAdClosedListenersm: ActionOnAdClosedListener?,

        ) {

        if (PrefUtil(context!!).getBool("is_premium") || PrefUtil(context!!).getBool(
                "is_reward",
                false
            )
        ) {
            mActionOnAdClosedListener = actionOnAdClosedListenersm

            performAction()
            return
        }
        if (isProcessing) {
            Log.e(logTag, "request_interstitial: Dialog is showing..")
            return
        }
        isProcessing = true
        mContext = context
        mActivity = activity
        mInterstitialID = interstitial_id
        mActionOnAdClosedListener = actionOnAdClosedListenersm
        isAdDecided = false

        if (InterAdTimerClass.isEligibleForAd()) {
            if (PrefUtil(context).getBool("is_premium") || PrefUtil(context).getBool(
                    "is_reward",
                    false
                )
            ) {
                mActionOnAdClosedListener = actionOnAdClosedListenersm
                performAction()
            } else {
                load_interstitial(showSuccessEvent)
            }
        } else {
            performAction()
        }
    }

    fun load_interstitial(
        showSuccessEvent: String = null.toString(),
    ) {
        if (mInterstitialAd == null) {
            Log.e(logTag, "Main Insterstitial Request Send.")
            mContext?.let {
                FirebaseCustomEvents(it).createFirebaseEvents(
                    "${showSuccessEvent}_load_req_sent", "true"
                )
            }
            showAdDialog()
            stopInterstitial = false
            timerCalled = false
            val adRequest_interstitial = AdRequest.Builder().build()
            InterstitialAd.load(
                mContext!!,
                mInterstitialID!!,
                adRequest_interstitial,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        interstitialAd.setImmersiveMode(true)
                        mInterstitialAd = interstitialAd
                        isAdDecided = true
                        Log.e(logTag, "note Insterstitial Loaded.")
                        mContext?.let {
                            FirebaseCustomEvents(it).createFirebaseEvents(
                                "${showSuccessEvent}_inter_loaded", "true"
                            )
                        }
                        if (!timerCalled) {
                            closeAdDialog()
                            show_interstitial(showSuccessEvent)
                        }
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Handle the error
                        Log.e(logTag, "note Interstitial Failed to Load." + loadAdError.message)
                        mContext?.let {
                            FirebaseCustomEvents(it).createFirebaseEvents(
                                "${showSuccessEvent}_inter_failed_load", "true"
                            )
                        }
                        mInterstitialAd = null
                        isAdDecided = true
                        if (!timerCalled) {
                            closeAdDialog()
                            performAction()
                        }
                    }
                })
            timerAdDecided(showSuccessEvent)
        } else {
            Log.e(logTag, " note Ad was already loaded.: ")
            stopInterstitial = false
            showAdDialog()
            Handler().postDelayed({
                closeAdDialog()
                show_interstitial(showSuccessEvent)
            }, 2000)
        }
    }

    fun timerAdDecided(
        showSuccessEvent: String = null.toString(),
    ) {
        Handler().postDelayed({
            if (!isAdDecided) {
                stopInterstitial = true
                timerCalled = true
                Log.e(logTag, "Handler Cancel.")
                InterAdTimerClass.cancelTimer()
                closeAdDialog()
                show_interstitial(showSuccessEvent)

            }
        }, 9000)
    }

    @SuppressLint("SetTextI18n")
    fun showAdDialog() {
        if (mActivity != null && !mActivity!!.isFinishing) {
            isInterstitalIsShowing = true
            dialog = Dialog(mActivity!!)
            dialog?.setContentView(R.layout.custom_reward_dialog)
            dialog?.setCancelable(false)
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val tvTitle = dialog?.findViewById<TextView>(R.id.tvTitle)
            val tvMessage = dialog?.findViewById<TextView>(R.id.tvMessage)
            tvTitle?.text = "Please Wait."
            tvMessage?.text = "Full Screen Ad is expected to Show."
            try {
                dialog?.show()
            } catch (e: Exception) {
                Log.e("AdDialog", "Error showing custom dialog: ${e.message}")
            }
            dialog = dialog
        }
    }

    fun closeAdDialog() {
        isInterstitalIsShowing = false
        try {
            if (mActivity != null && !mActivity!!.isFinishing) {
                if (Build.VERSION.SDK_INT >= 24) {
                    if (dialog != null && dialog!!.isShowing) {
                        dialog!!.dismiss()
                    }

                } else {
                    if (dialog != null && dialog!!.isShowing) {
                        dialog!!.dismiss()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(logTag, "notecloseAdDialog: Exception")
        }
    }


    fun show_interstitial(
        showSuccessEvent: String = null.toString(),
    ) {
        if (mInterstitialAd != null && stopInterstitial == false) {
            isInterstitalIsShowing = true
            mInterstitialAd!!.show(mActivity!!)
            mInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {

                    super.onAdFailedToShowFullScreenContent(adError)
                    Log.e(logTag, "note Insterstitial Failed to Show.")
                    mContext?.let {
                        FirebaseCustomEvents(it).createFirebaseEvents(
                            "${showSuccessEvent}_inter_show_failed", "true"
                        )
                    }
                    mInterstitialAd = null
                    isInterstitalIsShowing = false
                    performAction()
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    mContext?.let {
                        FirebaseCustomEvents(it).createFirebaseEvents(
                            "${showSuccessEvent}_inter_showed", "true"
                        )
                    }
                    Log.e(logTag, "note Insterstitial Shown.")
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    Log.e(logTag, "note Insterstitial Dismissed.")
                    mContext?.let {
                        FirebaseCustomEvents(it).createFirebaseEvents(
                            "${showSuccessEvent}_inter_dismissed", "true"
                        )
                    }
                    mInterstitialAd = null
                    isInterstitalIsShowing = false
                    performAction()
                }
            }
        } else {
            performAction()
        }
    }

    fun performAction() {
        Log.e(logTag, "performAction: Moving next")
        isInterstitalIsShowing = false
        mActionOnAdClosedListener?.ActionAfterAd()
        Handler().postDelayed({ isProcessing = false }, 1000)
    }
}