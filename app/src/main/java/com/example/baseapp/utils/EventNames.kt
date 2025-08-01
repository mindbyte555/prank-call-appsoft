package com.example.baseapp.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.baseapp.SplashScreen.Companion.version
import com.google.android.material.snackbar.Snackbar

object EventNames {
     //AudioCall
     var SCHEDULE_AUDIOCALLACTIVITY ="Schedule_AudioCallActivity_$version"
     var RINGTONE_AUDIOCALL ="Ringtone_AudioCall_$version"
     var THEME_AUDIOCALL ="Theme_AudioCall_$version"
     var SCHEDULECALL_AUDIOCALLBTN ="ScheduleCall_AudioCallBtn_$version"
     //CustomCall
     var CUSTOMCALL_SCHDULEACTIVITY ="CustomCall_SchduleActivity_$version"
     var CUSTOMCALL_RINGTONE ="CustomCall_Ringtone_$version"
     var CUSTOMCALL_THEME ="CustomCall_Theme_$version"
     var CUSTOMCALL_SCEDHULEBUTTON ="CustomCall_ScedhuleButton_$version"
    var CUSTOMSMS_SCHDULEACTIVITY ="CustomSMs_SchduleActivity_$version"

    //Customvideocall
     var CUSTOMVIDEO_SCHDULEACTIVITY ="CustomVideo_SchduleActivity_$version"
     var CUSTOMVIDEO_RINGTONE ="CustomVideo_Ringtone_$version"
     var CUSTOMVIDEO_THEME ="CustomVideo_Theme_$version"
     var CUSTOMVIDEO_SCHEDULECALLBTN ="CustomVideo_ScheduleCallBtn_$version"
     //mainActivity
     var SIMULATE_CALLMAIN ="Simulate_CallMain_$version"
     var SEEALL_CHAT ="Fake_chat_dashboard_$version"
     var SEEALL_CALL ="Voice_call_dashboard_$version"
     var video_call ="Video_call_dashboard_$version"
     var Shedule ="SheduleCall_dashboard_$version"
     var sms ="SheduleSMS_dashboard_$version"
     var FAKECHAT_ALEX ="fakeChat_Alex_$version"
     var FAKECHAT_MESSI ="fakeChat_Messi_$version"
     var FAKECHAT_EMMA ="fakeChat_Emma_$version"
     var FAKECHAT_RONALDO ="fakeChat_Ronaldo_$version"
     var FAKECALLVIDEO_ALEXANDRA ="fakeCallVideo_Alexandra_$version"
     var FAKECALLVIDEO_MESSI ="fakeCallVideo_Messi_$version"
     var FAKECALLVIDEO_EMMA ="fakeCallVideo_Emma_$version"
     //VideoCall
     var SCHEDULE_VIDEOCALLACTIVITY ="Schedule_VideoCallActivity_$version"
     var RINGTONE_VIDEOCALLACTIVITY ="Ringtone_VideoCallActivity_$version"
     var THEME_VIDEOCALL ="Theme_VideoCall_$version"
     var SCHEDULECALLBTN_AUDIOCALL ="ScheduleCallbtn_AudioCall_$version"

    //SMS
    var THEME_SMS ="Theme_Sms_$version"
    var SCEDHULE_SMS ="Schedule_Sms_$version"
    var SCHEDULE_SMSACTIVITY ="Schedule_SmsActivity_$version"
    var CUSTOM_THEME_SMS ="Custom_Theme_Sms_$version"
    var RINGTONE_CUSTOM_SMS ="ringtone_custom_sms_$version"
    var RINGTONE_SMS ="ringtone_sms_$version"


    //Exit
    var EXIT_DIALOG ="exit_dialog_$version"


    fun showSnackbarWithIcon(activity: Activity, view: View, message: String, iconResId: Int?=null) {
          val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
          val snackbarView = snackbar.view
          val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
          textView.gravity= Gravity.CENTER_VERTICAL
        iconResId?.let {
            val icon: Drawable? = ContextCompat.getDrawable(activity, it)
            icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
            textView.setCompoundDrawables(icon, null, null, null)
            textView.compoundDrawablePadding = 16 // Optional padding
        }
          snackbar.show()
     }

}