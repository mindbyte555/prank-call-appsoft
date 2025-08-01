package com.example.baseapp.alarmManagers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.baseapp.MainActivity
import com.example.baseapp.fragments.RingingFragment
import com.example.baseapp.utils.PrefUtil

class SmSReciver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if(RingingFragment.checkring==false){
            Log.d("TestTag","InsideOnReceiver of audio")
            var prefUtil = PrefUtil(context)
            var profile=""
            var profileInt=-1
            if(prefUtil.getBool("custom",false)){
                 profile = prefUtil.getString("custm_profile")?:""

            }
            else{
                 profile = prefUtil.getString("profile")?:""
                 profileInt = prefUtil.getInt("profile_int",0)

            }


            val intent = Intent(context, MainActivity::class.java)
            Log.d("TestTag","InsideOnReceiver of intent")

            intent.action = "sms_shit"
            intent.putExtra("profile", profile)
            intent.putExtra("profile_int", profileInt)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
            Log.d("TestTag","InsideOnReceiver of intent ${ intent.action}")

            context.startActivity(intent)
        }

        else{
            Log.d("Testtag","nooo")
        }


    }

}