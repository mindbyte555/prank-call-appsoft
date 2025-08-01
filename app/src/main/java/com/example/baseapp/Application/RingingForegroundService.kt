package com.example.baseapp.Application

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Person
import androidx.core.app.RemoteInput // Changed this import to androidx.core.app.RemoteInput
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.hardware.camera2.CameraManager
import android.media.Ringtone
import android.os.Build
import android.os.IBinder
import android.os.Vibrator
import android.widget.RemoteViews
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.baseapp.MainActivity
import com.example.fakecall.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class RingingForegroundService : Service() {
    private val notificationId = 101
    private lateinit var notificationManager: NotificationManager
    private lateinit var vibrator: Vibrator
    private var ringtone: Ringtone? = null
    private var flashJob: Job? = null
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private var isRunning = false
    private val serviceScope = CoroutineScope(Dispatchers.IO)


    override fun onCreate() {
        super.onCreate()

        // Initialize components
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

        // Start foreground service immediately
        startForeground(notificationId, createNotification(profileName))

        isRunning = true



        return START_STICKY
    }

    private fun createNotification(profileName: String): Notification {
        val channelId = "ringing_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Ringing Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val openIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationLayout = RemoteViews(this.packageName, R.layout.custom_call_notification)


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val caller = Person.Builder()
//                .setName(profileName)
//                .setImportant(true) // Mark as important for CallStyle
//                .build()
//
//            return Notification.Builder(this, channelId)
//                .setContentTitle("Incoming Call")
//                .setContentText("Call from $profileName")
//                .setSmallIcon(R.drawable.ic_call_icon) // Replace with your actual icon
//                .setOngoing(true) // Makes the notification non-dismissible (Android 14+) and ongoing
//                .setCategory(Notification.CATEGORY_CALL) // Categorize as a call
//                .setVisibility(Notification.VISIBILITY_PUBLIC)
//                .setStyle(
//                    Notification.CallStyle.forIncomingCall(caller, pendingIntent, pendingIntent)
//                )
//                .addPerson(caller) // Add the caller as a person
//                .build()
//        }
//        else{
//            return NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.drawable.ic_call_icon)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setCategory(NotificationCompat.CATEGORY_CALL)
//                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
//                .setCustomContentView(notificationLayout) // <--- attach custom layout
//                .build()
//        }

        val replyLabel = "Reply" // You might get this from resources: context.resources.getString(R.string.reply_label)

        // 2. Create an instance of RemoteInput.Builder
        val remoteInput: RemoteInput  = RemoteInput.Builder(replyLabel).run {
            setLabel(replyLabel)
            build()
        }



        // 4. Create the NotificationCompat.Action for the reply
        val replyAction = NotificationCompat.Action.Builder(
            R.drawable.ic_call_icon, // You might use a different icon for reply, or reuse the call icon
            replyLabel,
            pendingIntent
        )
            .addRemoteInput(remoteInput)
            .build()
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Call")
            .setContentText("$profileName")
            .setSmallIcon(R.drawable.ic_call_icon)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

//        return NotificationCompat.Builder(this, channelId)
////            .setSmallIcon(R.drawable.ic_call_icon) // required
////            .setContentTitle(profileName) // fallback for collapsed state
////            .setContentText("New messages") // fallback text
////            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_alex))
////            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
////            .setStyle(
////                NotificationCompat.InboxStyle()
////                    .addLine("$profileName:I Love you") // add first message
////                    .setBigContentTitle("$profileName - New messages")
////                    .setSummaryText("New messages")
////            )
////            .setContentIntent(pendingIntent)
////            .setPriority(NotificationCompat.PRIORITY_HIGH)
////            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
////            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
////            .setAutoCancel(true)
////            .build()
//
//            .setContentTitle("Incoming Call")
//            .setContentText("Call from $profileName")
//            .setSmallIcon(R.drawable.ic_call_icon)
//            .setContentIntent(pendingIntent)
//            .setOngoing(true)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//            .addAction(replyAction) // Add the reply action here
//            .build()


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
        stopForeground(true)
        stopSelf()
        serviceScope.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAll()

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


}
