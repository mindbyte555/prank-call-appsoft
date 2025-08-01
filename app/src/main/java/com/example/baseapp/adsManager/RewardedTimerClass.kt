package com.example.baseapp.adsManager

import android.util.Log
import java.util.Timer
import java.util.TimerTask

object RewardedTimerClass {
    var counter = 0
    var isFirstTimeClicked = true
    var myTimer: TimerTask? = null
    val isEligibleForAd: Boolean
        get() {
            if (isFirstTimeClicked) {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        //your method
                        myTimer = this
                        if (counter >= 1) {
                            cancelTimer()
                        } else {
                            isFirstTimeClicked = false
                        }
                        counter++
                        Log.d("TestTag", ": Counter second: " + counter)
                    }
                }, 0, 1000)
            }
            return if (isFirstTimeClicked) {
                true
            } else false
        }

    fun cancelTimer() {
        counter = -1
        isFirstTimeClicked = true
        myTimer!!.cancel()
    }
}
