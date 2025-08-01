package com.example.baseapp.alarmManagers

import android.app.NotificationManager
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.baseapp.Application.MessagingForegroundService
import com.example.baseapp.Application.NotificationConstants
import com.example.baseapp.MainActivity

class ReplyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TestTag", "Inside onReceive")

        if (intent.action == NotificationConstants.REPLY_ACTION) {
            val remoteInput = RemoteInput.getResultsFromIntent(intent)
            val replyText = remoteInput?.getCharSequence(NotificationConstants.KEY_TEXT_REPLY)
            val profileName = intent.getStringExtra(NotificationConstants.EXTRA_PROFILE_NAME)
            val profileNum = intent.getIntExtra(NotificationConstants.EXTRA_PROFILE_NUM,0)
            Log.d("TestTag", "replyText${replyText}")

            if (!replyText.isNullOrEmpty()) {
                Log.d("TestTag", "Inside if")
                Log.d("TestTag", "profileName${profileName}")
                Log.d("TestTag", "profileNum${profileNum}")

                val serviceIntent = Intent(context, MessagingForegroundService::class.java)
                Log.d("TestTag", "ACTION_STOP")

                context.stopService(serviceIntent)
                // Start MainActivity (if not already running)
                val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
                    action = "sms_chat"
                    putExtra(NotificationConstants.EXTRA_REPLY_TEXT, replyText.toString())
                    putExtra("profile", profileName)
                    putExtra("profile_int", profileNum)
                    putExtra("replyText", replyText)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(mainActivityIntent)

                // Stop the service (if running)


                // Cancel the notification
                val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(NotificationConstants.NOTIFICATION_ID)
            }
            else{
                Log.d("TestTag", "Inside else")
                val serviceIntent = Intent(context, MessagingForegroundService::class.java)
                context.stopService(serviceIntent)
                val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
                    action = "sms_chat"
                    putExtra(NotificationConstants.EXTRA_REPLY_TEXT, replyText.toString())
                    putExtra("profile", profileName)
                    putExtra("profile_int", profileNum)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(mainActivityIntent)

                // Cancel the notification
                val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(NotificationConstants.NOTIFICATION_ID)

            }


        }

    }
    }

class NotificationDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "ACTION_NOTIFICATION_DELETED") {
            // Stop service
            val serviceIntent = Intent(context, MessagingForegroundService::class.java)
            context.stopService(serviceIntent)

            // Cancel notification (optional, since it's already dismissed)
            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(NotificationConstants.NOTIFICATION_ID)
        }
    }
}
//class ReplyReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == NotificationConstants.REPLY_ACTION) {
//            val remoteInput = RemoteInput.getResultsFromIntent(intent)
//            val replyText = remoteInput?.getCharSequence(NotificationConstants.KEY_TEXT_REPLY)
//            val profileName = intent.getStringExtra(NotificationConstants.EXTRA_PROFILE_NAME)
//            Log.d("TestTag","replyText${ replyText}")
//
//            if (!replyText.isNullOrEmpty()) {
//                // Send broadcast to MainActivity
//                val broadcastIntent = Intent(NotificationConstants.BROADCAST_ACTION).apply {
//                    putExtra(NotificationConstants.EXTRA_REPLY_TEXT, replyText.toString())
//                    putExtra(NotificationConstants.EXTRA_PROFILE_NAME, profileName)
//                }
//
//                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent)
//
//                // Cancel the notification
//                val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//                notificationManager.cancel(NotificationConstants.NOTIFICATION_ID)
//            }
//        }
//    }
//}