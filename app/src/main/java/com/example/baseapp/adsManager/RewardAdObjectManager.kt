package com.example.baseapp.adsManager

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.example.baseapp.utils.Constants
import com.example.baseapp.utils.FirebaseCustomEvents
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

@SuppressLint("StaticFieldLeak")
object RewardAdObjectManager {
    var mRewardedAd: RewardedAd? = null
    var mContext: Context? = null
    var mActivity: Activity? = null
    var rewardedID: String? = null
    var mActionOnAdClosedListener: ActionRewardAdClose? = null
    var isAdDecided: Boolean = false
    var isRewardedShowing: Boolean = false
    var isProcessing: Boolean = false
    var stopRewarded: Boolean = false
    var timerCalled: Boolean = false
    val handler = Handler(Looper.getMainLooper())
    var dialog: Dialog? = null
    var loader: ProgressBar? = null
    var coin=0

    fun requestRewardedAd(
        context: Context?,
        activity: Activity?,
        showSuccessEvent: String = null.toString(),
        actionOnAdClosedListenersm: ActionRewardAdClose?,
    ) {

        if (PrefUtil(context!!).getBool("is_premium") || PrefUtil(context).getBool(
                "is_reward",
                false
            )
        ) {
            mActionOnAdClosedListener = actionOnAdClosedListenersm

            performAction()
            return
        }
        if (isProcessing) {
            Log.e("TestTag", "request_interstitial: Dialog is showing..")
            return
        }
        isProcessing = true
        mContext = context
        mActivity = activity
        rewardedID = AddIds.rewardId()

        mActionOnAdClosedListener = actionOnAdClosedListenersm
        isAdDecided = false
        Log.e("TestTag", "rewardedID: ${rewardedID}")

        if (RewardedTimerClass.isEligibleForAd) {
            if (PrefUtil(context!!).getBool("is_premium") || PrefUtil(context).getBool(
                    "is_reward",
                    false
                )
            ) {
                Log.e("TestTag", "if not eligible: ${rewardedID}")

                mActionOnAdClosedListener = actionOnAdClosedListenersm
                performAction()
            } else {
                load_rewarded(showSuccessEvent)
            }
        } else {
            Log.e("TestTag", "else not eligible: ${rewardedID}")

            performAction()
        }
    }

    fun load_rewarded(
        showSuccessEvent: String = null.toString(),
    ) {
        if (mRewardedAd == null) {
            Log.e("TestTag", "Premium Insterstitial Request Send.")
            mContext?.let {
                FirebaseCustomEvents(it).createFirebaseEvents(
                    "${showSuccessEvent}_load_req_sent", "true"
                )
            }
            showAdDialog()
            stopRewarded = false
            timerCalled = false
            Log.e("TestTag", "load rewardedID: ${rewardedID}")

            val rewardedAdRequest = AdRequest.Builder().build()
            RewardedAd.load(
                mContext!!,
                rewardedID!!,
                rewardedAdRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        mRewardedAd = ad
                        isAdDecided = true
                        Log.e("TestTag", "Rewarded ad Loaded.")
                        mContext?.let {
                            FirebaseCustomEvents(it).createFirebaseEvents(
                                "${showSuccessEvent}_load_success", "true"
                            )
                        }
                        if (!timerCalled) {
                            Log.e("TestTag", "Timer issue")

                            closeAdDialog()
                            showRewarded(showSuccessEvent)
                        }



                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Handle the error
                        Log.e(
                            "TestTag",
                            "Premium Interstitial Failed to Load." + loadAdError.message
                        )
                        mContext?.let {
                            FirebaseCustomEvents(it).createFirebaseEvents(
                                "${showSuccessEvent}_load_failed", "true"
                            )
                        }
                        mRewardedAd = null
                        isAdDecided = true
                        if (!timerCalled) {
                            Log.e("TestTag", " timer load ")

                            closeAdDialog()
                            performAction()
                        }



                    }
                })
            timerAdDecided(showSuccessEvent)
        } else {
            Log.e("TestTag", " Premium Ad was already loaded.: ")
            stopRewarded = false
            showAdDialog()
            Handler().postDelayed({
                closeAdDialog()
                showRewarded(showSuccessEvent)
            }, 2000)
        }
    }

    fun timerAdDecided(
        showSuccessEvent: String = null.toString(),
    ) {
        Handler().postDelayed({
            if (!isAdDecided) {
                stopRewarded = true
                timerCalled = true
                Log.e("TestTag", "Handler Cancel.")
                RewardedTimerClass.cancelTimer()
                closeAdDialog()
                showRewarded(showSuccessEvent)

            }
        }, 5000)
    }

    fun showAdDialog() {
        if (mActivity != null && !mActivity!!.isFinishing) {
            isRewardedShowing = true
            dialog = Dialog(mActivity!!)
            dialog?.setContentView(R.layout.custom_reward_dialog)
            dialog?.setCancelable(false)
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val tvTitle = dialog?.findViewById<TextView>(R.id.tvTitle)
            val tvMessage = dialog?.findViewById<TextView>(R.id.tvMessage)
            Constants.startMarqueeEffect(
                handler, listOf(
                    tvTitle as View,
                    tvMessage as View

                )
            )
            loader = dialog?.findViewById(R.id.rewardloader)
//            loader?.playAnimation()
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
        isRewardedShowing = false
        try {
            if (mActivity != null && !mActivity!!.isFinishing) {
                if (Build.VERSION.SDK_INT >= 24) {
                    Log.e("TestTag", "rewarded closeAdDialog: ")
                    if (dialog != null && dialog!!.isShowing) {
//                        loader?.pauseAnimation()
                        dialog!!.dismiss()
                    }

                } else {
                    if (dialog != null && dialog!!.isShowing) {
//                        loader?.pauseAnimation()
                        dialog!!.dismiss()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("TestTag", "closeAdDialog: Exception")
        }
    }

    fun showRewarded(
        showSuccessEvent: String = null.toString(),
    ) {
        mRewardedAd?.let { ad ->
            ad.show(mActivity!!, OnUserEarnedRewardListener { rewardItem ->
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
                Log.d(
                    "TestTag",
                    "User earned the reward rewardAmount:$rewardAmount and rewardType:$rewardType"
                )

                mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        Log.d("TestTag", "Rewarded Ad was clicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        Log.d("TestTag", "Rewarded Ad dismissed fullscreen content.")
                        mRewardedAd = null
                        FirebaseCustomEvents(mActivity!!).createFirebaseEvents(
                            "${showSuccessEvent}_dismissed", "true"
                        )
                        performAction()

                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        Log.e("TestTag", "Rewarded Ad failed to show fullscreen content.")
                        mRewardedAd = null
                        FirebaseCustomEvents(mActivity!!).createFirebaseEvents(
                            "${showSuccessEvent}_failed_to_show", "true"
                        )
                        performAction()
                    }

                    override fun onAdImpression() {
                        Log.d("TestTag", "Rewarded Ad recorded an impression.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        FirebaseCustomEvents(mActivity!!).createFirebaseEvents(
                            "${showSuccessEvent}_showed", "true"
                        )
                        Log.d("TestTag", "Rewarded Ad showed fullscreen content.")
                    }
                }
            })
        } ?: run {
            Log.d("TestTag", "Rewarded The rewarded ad wasn't ready yet.")
            performAction()
        }
    }

    fun performAction() {
        Log.e("TestTag", "performAction: Moving next")
        isRewardedShowing = false
        mActionOnAdClosedListener?.ActionAfterAd()
        Handler().postDelayed({ isProcessing = false }, 1000)
    }
}
interface ActionRewardAdClose {
    fun ActionAfterAd()
}