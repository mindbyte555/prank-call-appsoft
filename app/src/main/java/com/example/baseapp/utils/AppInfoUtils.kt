package com.example.iwidgets.Utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import com.example.fakecall.R


class AppInfoUtils(var activity: Activity) {

    fun rateApp() {
        val appPackageName = activity.packageName

        try {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (activityNotFoundException: ActivityNotFoundException) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }
    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }


    fun openPlayStore() {
        val appPackageName = activity.packageName
//        try {
//            activity.startActivity(
//                Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse("market://details?id=$appPackageName")
//                )
//            )
//        } catch (activityNotFoundException: ActivityNotFoundException) {
//            activity.startActivity(
//                Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
//                )
//            )
//        }
    }
    fun openGooglePrivacy() {
        try {
            val url =
                "https://payments.google.com/payments/apis-secure/u/0/get_legal_document?ldl=en_GB&ldo=0&ldt=buyertos"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            activity.startActivity(browserIntent)
        } catch (e: java.lang.Exception) {
            // Catch Exception here
        }
    }
    fun openSubscriptionPage() {
        try {
            val url = "http://play.google.com/store/account/subscriptions"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            activity.startActivity(browserIntent)
        } catch (e: Exception) {
            // Catch Exception here
        }
    }

    fun shareApp() {
        try {
            val appPackageName = activity.packageName
            val myUrl = "https://play.google.com/store/apps/details?id=$appPackageName"
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, myUrl)
            sendIntent.type = "text/plain"
            activity.startActivity(
                Intent.createChooser(
                    sendIntent,
                    activity.getString(R.string.share)
                )
            )
        } catch (e: Exception) {
            // Catch Exception here
        }
    }

    fun openPrivacy() {
        try {
            val url = "https://sites.google.com/view/fake-call-app-privacy/home"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            activity.startActivity(browserIntent)
        } catch (e: Exception) {
            // Catch Exception here
        }
    }

    fun checkUpdate() {
        val appPackageName = activity.packageName
        try {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (activityNotFoundException: ActivityNotFoundException) {
            try {
                activity.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            } catch (e: Exception) {
            }
        }
    }

}