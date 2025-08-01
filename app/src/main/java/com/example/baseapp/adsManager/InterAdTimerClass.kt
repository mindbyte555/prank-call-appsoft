package com.example.baseapp.adsManager

import android.util.Log
import java.util.Timer
import java.util.TimerTask

object InterAdTimerClass {
    private var counter = 0
    private var isFirstTimeClicked = true
    private var myTimer: TimerTask? = null

    fun isEligibleForAd(): Boolean {
        if (isFirstTimeClicked) {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    myTimer = this
                    if (counter >= 1) {
                        cancelTimer()
                    } else {
                        isFirstTimeClicked = false
                    }
                    counter++
                    Log.d("Ads_", ": Counter second: $counter")
                }
            }, 0, 1000)
        }

        return isFirstTimeClicked
    }

    fun cancelTimer() {
        counter = -1
        isFirstTimeClicked = true
        myTimer?.cancel()
    }
}
