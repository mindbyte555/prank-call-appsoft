package com.example.baseapp.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseCustomEvents(val context: Context) {

    var FirebBaseEvent: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun createFirebaseEvents(key: String, value: String) {
        val params = Bundle()
        params.putString(key, value)
        FirebBaseEvent.logEvent(key, params)

        Log.e("TAG", "createFirebaseEvents: $key and $value")
    }

    companion object {
        private var customInsteance: FirebaseCustomEvents? = null
        private var mContext: Context? = null
        val Firebaseinstance: FirebaseCustomEvents
            get() {
                if (customInsteance == null) {
                    customInsteance = FirebaseCustomEvents(mContext!!)
                }
                return customInsteance!!
            }
    }

    init {
        if (mContext == null) {
            mContext = context
        }
    }
}