package com.example.baseapp.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.SystemClock
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import com.example.baseapp.Application.MyApplication.Companion.noadshow
import com.example.fakecall.R
import com.example.iwidgets.Utils.AppInfoUtils

object Constants {
    fun startMarqueeEffect(
        handler: Handler, views: List<View>, moveDuration: Long = 7000, delayDuration: Long = 14000
    ) {
        val startMarquee: Runnable = object : Runnable {
            override fun run() {
                views.forEach { view ->
                    view.isSelected = true
                }

                handler.postDelayed({
                    views.forEach { view ->
                        view.isSelected = false
                    }
                }, moveDuration) // for moving
                handler.postDelayed(this, delayDuration) // for delay
            }
        }
        handler.post(startMarquee)
    }
    fun View.clickWithDebounce(debounceTime: Long = 1000L, action: () -> Unit) {
        this.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime: Long = 0

            override fun onClick(v: View) {
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
                else action()

                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }
    fun dialogbox(activity: Activity){



        val dialogView = LayoutInflater.from(activity).inflate(R.layout.internet_dialog, null)

        val dialog = AlertDialog.Builder(activity)
            .setView(dialogView)
            .setCancelable(false) // Set to false if you want to prevent dismiss on outside touch
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Optional: transparent background

        val crossImg = dialogView.findViewById<ConstraintLayout>(R.id.tryagain)
        crossImg.setOnClickListener {
            noadshow =false
            dialog.cancel()

            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            activity.startActivity(intent)




        }




        dialog.show()
    }
}