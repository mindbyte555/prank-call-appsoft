package com.example.baseapp.Application

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.media.Ringtone
import android.os.Build
import android.os.IBinder
import android.os.Vibrator
import android.provider.Settings.EXTRA_CONVERSATION_ID
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.baseapp.Application.NotificationConstants.KEY_TEXT_REPLY
import com.example.baseapp.Application.NotificationConstants.REPLY_ACTION
import com.example.baseapp.MainActivity
import com.example.baseapp.alarmManagers.CustomcallReciver
import com.example.baseapp.alarmManagers.ReplyReceiver
import com.example.fakecall.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class MessagingForegroundService : Service() {
    private val notificationId = 101
    private lateinit var notificationManager: NotificationManager
    private lateinit var vibrator: Vibrator
    private var ringtone: Ringtone? = null
    private var flashJob: Job? = null
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private var isRunning = false
    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private val channelId = "sms_channel_01" // Changed to avoid conflicts

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val profileName = intent?.getStringExtra("profile_name") ?: "unknown"
        val profilenum = intent?.getIntExtra("profile_num",0)
        startForeground(notificationId, createNotification(profileName,profilenum!!))
        isRunning = true
        return START_STICKY
    }



    private fun createNotification(profileName: String,profileNum:Int): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "SMS Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Incoming message notifications"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }


        val replyIntent = Intent(this, ReplyReceiver::class.java).apply {
            action = REPLY_ACTION
            putExtra(EXTRA_CONVERSATION_ID, 1) // Use a real conversation ID here
            putExtra("profile_name", profileName)
            putExtra("profile_num", profileNum)

        }
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create remote input
        val replyLabel = "Type your reply here"
        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(replyLabel)
            build()
        }

        // Create reply action


        val replyPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val replyAction = NotificationCompat.Action.Builder(
            R.drawable.ic_call_icon, // Make sure you have this icon
            "Reply",
            replyPendingIntent
        ).addRemoteInput(remoteInput)
            .build()

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Message from $profileName")
            .setContentText("Tap to open conversation")
            .setSmallIcon(R.drawable.ic_call_icon)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(replyAction)
            .build()
    }
    private fun stopAll() {
        isRunning = false
        ringtone?.stop()
        vibrator.cancel()
        flashJob?.cancel()
        try {
            cameraManager.setTorchMode(cameraId, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        serviceScope.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAll()
    }

    override fun onBind(p0: Intent?): IBinder? = null
}
object NotificationConstants {
    const val KEY_TEXT_REPLY = "key_text_reply"
    const val REPLY_ACTION = "com.your.package.REPLY_ACTION"
    const val BROADCAST_ACTION = "com.your.package.REPLY_BROADCAST"
    const val EXTRA_REPLY_TEXT = "extra_reply_text"
    const val EXTRA_PROFILE_NAME = "profile_name"
    const val EXTRA_PROFILE_NUM = "profile_num"
    const val NOTIFICATION_ID = 101
}
//class ChatHeadService : Service() {
//
//    private var windowManager: WindowManager? = null
//    private var chatHead: View? = null
//    private var messageCount = 0 // Track message count
//
//    override fun onBind(intent: Intent?): IBinder? = null
//
//    override fun onCreate() {
//        super.onCreate()
//        createRoundedChatHead()
//        // Start with 1 message for demo (you can update this dynamically)
//        updateMessageCount(1)
//    }
//
//    private fun createRoundedChatHead() {
//        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
//
//        // Create a container view
//        chatHead = View.inflate(this, R.layout.chat_head_layout, null)
//
//        // Set click listener
//        chatHead?.setOnClickListener {
//            // Open chat when clicked
//            // Reset count when opened
//            updateMessageCount(4)
//        }
//
//        val params = WindowManager.LayoutParams(
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
//            } else {
//                WindowManager.LayoutParams.TYPE_PHONE
//            },
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//            PixelFormat.TRANSLUCENT
//        ).apply {
//            gravity = Gravity.END or Gravity.TOP // Position at bottom-right
//            x = 100 // Margin from right edge
//            y = 200 // Margin from bottom edge
//        }
//
//        windowManager?.addView(chatHead, params)
//    }
//
//    private fun updateMessageCount(count: Int) {
//        messageCount = count
//        chatHead?.findViewById<TextView>(R.id.badgeCount)?.let { badge ->
//            if (count > 0) {
//                badge.text = if (count > 9) "9+" else count.toString()
//                badge.visibility = View.VISIBLE
//            } else {
//                badge.visibility = View.GONE
//            }
//        }
//    }
//
//
//
//    private fun dpToPx(dp: Int): Int {
//        return (dp * resources.displayMetrics.density).toInt()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        chatHead?.let { windowManager?.removeView(it) }
//    }
//
//    companion object {
//        fun start(context: android.content.Context) {
//            ContextCompat.startForegroundService(
//                context,
//                Intent(context, ChatHeadService::class.java)
//            )
//        }
//
//        fun stop(context: android.content.Context) {
//            context.stopService(Intent(context, ChatHeadService::class.java))
//        }
//    }
//}
//class ChatHeadService : Service() {
//
//    private var windowManager: WindowManager? = null
//    private var chatHead: ImageView? = null
//
//    override fun onBind(intent: Intent?): IBinder? = null
//
//    override fun onCreate() {
//        super.onCreate()
//        createRoundedChatHead()
//    }
//
//    private fun createRoundedChatHead() {
//        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
//
//        chatHead = ImageView(this).apply {
//            setImageResource(R.drawable.ic_alex) // Use a rounded drawable
//            // Make it circular (if not already in the drawable)
//            clipToOutline = true
//            setOnClickListener {
//                // Open chat when clicked
//            }
//        }
//
//        val params = WindowManager.LayoutParams(
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
//            } else {
//                WindowManager.LayoutParams.TYPE_PHONE
//            },
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//            PixelFormat.TRANSLUCENT
//        ).apply {
//            gravity = Gravity.LEFT or Gravity.TOP // Position at bottom-right
//            x = 100 // Margin from right edge
//            y = 200 // Margin from bottom edge
//        }
//        windowManager?.addView(chatHead, params)
//
////        if (checkOverlayPermission()) {
////            windowManager?.addView(chatHead, params)
////        }
//    }
//
//
//
//
//
//    override fun onDestroy() {
//        super.onDestroy()
//        chatHead?.let { windowManager?.removeView(it) }
//    }
//
//    companion object {
//        fun start(context: android.content.Context) {
//            ContextCompat.startForegroundService(
//                context,
//                Intent(context, ChatHeadService::class.java)
//            )
//        }
//
//        fun stop(context: android.content.Context) {
//            context.stopService(Intent(context, ChatHeadService::class.java))
//        }
//    }
//}