package com.example.baseapp.alarmManagers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.baseapp.MainActivity
import com.example.baseapp.fragments.RingingFragment
import com.example.baseapp.fragments.VideoCall.VideoRingingFragment
import com.example.baseapp.utils.PrefUtil

class VideoAlarmReciver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if(VideoRingingFragment.videoring==false){
            Log.d("TestTag","InsideOnReceiver of Video")
            var prefUtil = PrefUtil(context)
            var profile = prefUtil.getString("profile")
            var profileInt = prefUtil.getInt("profile_int",0)


            val intent = Intent(context, MainActivity::class.java)
            intent.action = "videocall"
            intent.putExtra("profile", profile)
            intent.putExtra("profile_int", profileInt)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
            context.startActivity(intent)
        }

        else{
            Log.d("Testtag","nooo")
        }


    }

}