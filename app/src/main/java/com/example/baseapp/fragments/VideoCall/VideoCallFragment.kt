package com.example.baseapp.fragments.VideoCall

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable

import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.TextView

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.baseapp.Application.MyApplication.Companion.noadshow
import com.example.baseapp.MainActivity
import com.example.baseapp.SplashScreen.Companion.version
import com.example.baseapp.alarmManagers.VideoAlarmReciver
import com.example.baseapp.ringtones.RingtoneActivity
import com.example.baseapp.schedule.ScheduleActivity
import com.example.baseapp.secondPermission.SecondPermissionActivity
import com.example.baseapp.themes.ThemeActivity
import com.example.baseapp.utils.EventNames.RINGTONE_VIDEOCALLACTIVITY
import com.example.baseapp.utils.EventNames.SCHEDULECALLBTN_AUDIOCALL
import com.example.baseapp.utils.EventNames.SCHEDULE_VIDEOCALLACTIVITY
import com.example.baseapp.utils.EventNames.THEME_VIDEOCALL
import com.example.baseapp.utils.EventNames.showSnackbarWithIcon
import com.example.baseapp.utils.FirebaseCustomEvents
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentVideoCallBinding
import com.google.android.material.snackbar.Snackbar


class  VideoCallFragment : Fragment() {
//    private lateinit var leftRightAnimation: ObjectAnimator
private var leftRightAnimation: ObjectAnimator? = null

    private lateinit var binding: FragmentVideoCallBinding
    lateinit var prefutil: PrefUtil
    var videocount=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefutil= PrefUtil(requireContext())

    }

    override fun onDestroy() {
        leftRightAnimation?.cancel()
        super.onDestroy()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoCallBinding.inflate(inflater, container, false)
        leftRightAnimation =
            ObjectAnimator.ofPropertyValuesHolder(
                binding.schedulevideoconst,
                PropertyValuesHolder.ofFloat("scaleX", 0.95f, 1.02f),
                PropertyValuesHolder.ofFloat("scaleY", 0.95f, 1.05f)
            ).apply {
                duration = 500
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
                interpolator = DecelerateInterpolator() // Smooth transition
            }
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.schedulevideoconst.setOnClickListener {
            binding.schedulevideoconst.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
            leftRightAnimation?.cancel()
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                SCHEDULE_VIDEOCALLACTIVITY,
                "true"
            )
            if(!checkCameraPermission()){
                val intent = Intent(it.context, SecondPermissionActivity::class.java)
                intent.putExtra("key", "cameravideo")
                it.context.startActivity(intent)
            }
            else{
                val intent = Intent(it.context, ScheduleActivity::class.java)
                it.context.startActivity(intent)
            }

        }
        binding.videoringtoneconst.setOnClickListener {
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                RINGTONE_VIDEOCALLACTIVITY,
                "true"
            )
            if (Settings.System.canWrite(requireContext())) {
                val intent = Intent(context, RingtoneActivity::class.java)
                context?.startActivity(intent)
            } else {
                val intent = Intent(it.context, SecondPermissionActivity::class.java)
                intent.putExtra("key", "audiocall")
                it.context.startActivity(intent)
//                checkAndRequestWriteSettingsPermission()
            }

        }
        binding.videothemesconst.setOnClickListener {
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                THEME_VIDEOCALL,
                "true"
            )
            val intent = Intent(context, ThemeActivity::class.java)
            context?.startActivity(intent)
        }
        binding.schedulevideobtn.setOnClickListener {
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                SCHEDULECALLBTN_AUDIOCALL,
                "true"
            )
            noadshow =false

            videocount= prefutil.getInt("videotime", 0)
            Log.d("Testtag",videocount.toString())
            val alarmIntent = Intent(requireContext(), VideoAlarmReciver::class.java)

            val pendingIntent =
                PendingIntent.getBroadcast(requireContext(), 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)

            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            when(videocount){
                1->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent)
                    prefutil?.setInt("videotime",0)

                    Toast.makeText(requireContext(),"Video Call Scheduled for 10 sec",Toast.LENGTH_LONG).show()

                    videocount=0
                }
                2->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, pendingIntent)
                    prefutil?.setInt("videotime",0)
                    Toast.makeText(requireContext(),"Video Call Scheduled for 30 sec",Toast.LENGTH_LONG).show()
                    videocount=0
                }
                3->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent)
                    prefutil?.setInt("videotime",0)
                    Toast.makeText(requireContext(),"Video Call Scheduled for 1 minute",Toast.LENGTH_LONG).show()
                    videocount=0
                }
                4->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, pendingIntent)
                    prefutil?.setInt("videotime",0)
                    Toast.makeText(requireContext(),"Video Call Scheduled for 5 minute",Toast.LENGTH_LONG).show()

                    videocount=0
                }
                else -> {
                    leftRightAnimation?.start()
                    binding.schedulevideoconst.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.ripple_color))
                   // Snackbar.make(requireView(), "Please select time in Schedule", Snackbar.LENGTH_SHORT).show()
                       showSnackbarWithIcon(requireActivity(),requireView(),"Please select time in Schedule",R.drawable.snackschedule)
                }

            }
        }


    }
    override fun onResume() {
        binding.schedulevideoconst.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        leftRightAnimation?.cancel()
        super.onResume()
    }


private val requestWriteSettingsPermission = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
) { isGranted ->
    if (isGranted) {
        // Permission granted, start the RingtoneActivity
        val intent = Intent(context, RingtoneActivity::class.java)
        startActivity(intent)
    } else {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:" + requireContext().packageName)
        startActivity(intent)
    }
}
    private fun checkAndRequestWriteSettingsPermission() {
        if (Settings.System.canWrite(requireContext())) {
            // Permission already granted, start the RingtoneActivity
            val intent = Intent(context, RingtoneActivity::class.java)
            startActivity(intent)
        } else {
            // Request the WRITE_SETTINGS permission
            requestWriteSettingsPermission.launch(android.Manifest.permission.WRITE_SETTINGS)
        }
    }
    private fun checkCameraPermission(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        )
        return cameraPermission == PackageManager.PERMISSION_GRANTED
    }
}