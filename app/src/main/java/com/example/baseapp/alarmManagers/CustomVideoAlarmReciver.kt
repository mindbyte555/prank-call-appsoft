package com.example.baseapp.alarmManagers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.baseapp.MainActivity
import com.example.baseapp.fragments.CustomFragment.customVideoFragment.CustomVideoRingingFragment
import com.example.baseapp.utils.PrefUtil

class CustomVideoAlarmReciver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if(CustomVideoRingingFragment.videoring==false){
            Log.d("TestTag","InsideOnReceiver of Video")
            var prefUtil = PrefUtil(context)
            var profile = prefUtil.getString("custm_profile")
            var profileInt = prefUtil.getString("custm_num")
            var profileUri = prefUtil.getString("profile_img")
            Log.d("Testtag","Uri in Reciver $profileUri")
            Log.d("Testtag","Profile in Reciver $profile")



            val intent = Intent(context, MainActivity::class.java)
            intent.action = "customvideocall"
            intent.putExtra("customprofile", profile)
            intent.putExtra("customprofileno", profileInt)
            intent.putExtra("customprofileuri", profileUri)
            prefUtil.setString("custm_profile","")
            prefUtil.setString("custm_num","")
            prefUtil.setString("profile_img","")

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
            context.startActivity(intent)
        }

        else{
            Log.d("Testtag","nooo")
        }


    }

}