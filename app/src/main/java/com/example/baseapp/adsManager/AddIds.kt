package com.example.baseapp.adsManager

import com.example.baseapp.Application.MyApplication.Companion.appopenresumeid
import com.example.baseapp.Application.MyApplication.Companion.done_id
import com.example.fakecall.BuildConfig

class AddIds {
    enum class PriceTier {
        HIGH, MEDIUM, NORMAL
    }

    companion object {
        var BANNER = "ca-app-pub-3173814755166534/1481963124"
        var Collapse_BANNER = "ca-app-pub-3173814755166534/4905940454"

        //const val BANNER = "ca-app-pub-3173814755166534/2977684683"
        const val BANNER_TEST = "ca-app-pub-3940256099942544/2014213617"

        const val INTERSTITIAL_TEST = "/6499/example/interstitial"
        var INTERSTITIAL_RELEASE = "ca-app-pub-3173814755166534/8685065687"

        var APP_OPEN = "ca-app-pub-3173814755166534/1381290733"
        const val APP_OPEN_TEST = "ca-app-pub-3940256099942544/9257395921"

        var APP_OPEN_HIGH = "ca-app-pub-3173814755166534/2532318049"  // High eCPM
        var APP_OPEN_MEDIUM = "ca-app-pub-3173814755166534/3786831601" // Medium eCPM
        var APP_OPEN_NORMAL = "ca-app-pub-3173814755166534/1381290733" // Test Ad (Normal)


        var Native = "ca-app-pub-3173814755166534/8944085987"
        const val Native_TEST = "/6499/example/native"

        //Rectangle Banner Ads
        var MEDIUM_RECTANGLE_Test = "ca-app-pub-3940256099942544/6300978111"
        var MEDIUM_RECTANGLE = "ca-app-pub-3173814755166534/5884037016"

        //Rectangle Banner Ads
        var Exit_Native_TEST = "ca-app-pub-3940256099942544/6300978111"
        var Exit_Native = "ca-app-pub-3173814755166534/5884037016"

        //Adaptive Banner Ads
        const val ADAPTIVE_BANNER = "ca-app-pub-3940256099942544/9214589781"
        const val Rewarded_ad_test = "ca-app-pub-3940256099942544/5224354917"
        const val Rewarded_ad_release = "ca-app-pub-3173814755166534/9742168159"

        fun openAppId(): String {
            if (BuildConfig.DEBUG) {
                return APP_OPEN_TEST
            } else {
                return APP_OPEN
            }
        }
        fun NoteId(): String {
            if (BuildConfig.DEBUG) {
                return INTERSTITIAL_TEST
            } else {

                return done_id
            }
        }

        fun openAppId(tier: PriceTier): String {
            if (BuildConfig.DEBUG) {
                return APP_OPEN_TEST
            } else {
                return when (tier) {
                    PriceTier.HIGH -> APP_OPEN_HIGH
                    PriceTier.MEDIUM -> APP_OPEN_MEDIUM
                    PriceTier.NORMAL -> APP_OPEN_NORMAL
                }
            }

        }
        fun rewardId(): String {
            if (BuildConfig.DEBUG) {
                return Rewarded_ad_test
            } else {

                return Rewarded_ad_release
            }
        }
        fun onResumeopenAppId(): String {
            if (BuildConfig.DEBUG) {
                return APP_OPEN_TEST
            } else {

                return appopenresumeid
            }
        }
    }
}