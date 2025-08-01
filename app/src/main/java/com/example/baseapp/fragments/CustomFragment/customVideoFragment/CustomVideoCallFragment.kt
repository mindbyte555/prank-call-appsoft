package com.example.baseapp.fragments.CustomFragment.customVideoFragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.baseapp.SplashScreen.Companion.version
import com.example.baseapp.alarmManagers.CustomVideoAlarmReciver
import com.example.baseapp.ringtones.RingtoneActivity
import com.example.baseapp.schedule.ScheduleActivity
import com.example.baseapp.secondPermission.SecondPermissionActivity
import com.example.baseapp.themes.ThemeActivity
import com.example.baseapp.utils.EventNames.CUSTOMVIDEO_RINGTONE
import com.example.baseapp.utils.EventNames.CUSTOMVIDEO_SCHDULEACTIVITY
import com.example.baseapp.utils.EventNames.CUSTOMVIDEO_SCHEDULECALLBTN
import com.example.baseapp.utils.EventNames.CUSTOMVIDEO_THEME
import com.example.baseapp.utils.FirebaseCustomEvents
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.databinding.FragmentCustomVideoCallBinding


class CustomVideoCallFragment : Fragment() {
    private lateinit var binding: FragmentCustomVideoCallBinding
    lateinit var prefutil: PrefUtil
    var videocount=0
    var videouri=""
    var videoname=""
    var videonum=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefutil= PrefUtil(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomVideoCallBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.customschedulevideoconst.setOnClickListener {


            videouri= prefutil.getString("profile_img").toString()
            videoname=prefutil.getString("custm_profile").toString()
            videonum=prefutil.getString("custm_num").toString()

            Log.d("Testtag","$videoname :name in custom")
            Log.d("Testtag","$videonum :no in custom")
            Log.d("Testtag","$videouri :uri in custom")


            if(videouri!=""&& videoname!=""&& videonum!="" ){
                FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                    CUSTOMVIDEO_SCHDULEACTIVITY,
                    "true"
                )
                if(!checkCameraPermission()){
                    val intent = Intent(it.context, SecondPermissionActivity::class.java)
                    intent.putExtra("key", "customvideo")
//                    intent.putExtra("custom_uri",videouri)
//                    intent.putExtra("custom_profile",videoname)
//                    intent.putExtra("custom_mobile",videonum)

                    it.context.startActivity(intent)
                }
                else{
                    val intent = Intent(it.context, ScheduleActivity::class.java)
                    intent.putExtra("Key", "customvideo")
//                    intent.putExtra("custom_uri",videouri)
//                    intent.putExtra("custom_profile",videoname)
//                    intent.putExtra("custom_mobile",videonum)

                    it.context.startActivity(intent)
                }

            }
            else{
                Toast.makeText(requireActivity(),"Please first select image ,add name and number",Toast.LENGTH_LONG).show()

                Log.d("Testtag","No uri senior")
            }


        }
        binding.customvideoringtoneconst.setOnClickListener {
//            val intent = Intent(context, RingtoneActivity::class.java)
//            context?.startActivity(intent)
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                CUSTOMVIDEO_RINGTONE,
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
        binding.customvideothemesconst.setOnClickListener {
            val intent = Intent(context, ThemeActivity::class.java)
            context?.startActivity(intent)
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                CUSTOMVIDEO_THEME,
                "true"
            )
        }
        binding.customschedulevideobtn.setOnClickListener {
//            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
//                "CustomVideo_ScheduleCallBtn_$version",
//                "true"
//            )
            videocount= prefutil.getInt("customvideotime", 0)
            Log.d("Testtag",videocount.toString())
            val alarmIntent = Intent(requireContext(), CustomVideoAlarmReciver::class.java)

            val pendingIntent =
                PendingIntent.getBroadcast(requireContext(), 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)

            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if(videocount!=0){
                FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                    CUSTOMVIDEO_SCHEDULECALLBTN,
                    "true"
                )
            }
            when(videocount){
                1->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent)
                    prefutil?.setInt("customvideotime",0)

                    Toast.makeText(requireContext(),"Video Call Scheduled for 10 sec", Toast.LENGTH_LONG).show()

                    videocount=0
                }
                2->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, pendingIntent)
                    prefutil?.setInt("customvideotime",0)
                    Toast.makeText(requireContext(),"Video Call Scheduled for 30 sec", Toast.LENGTH_LONG).show()
                    videocount=0
                }
                3->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent)
                    prefutil?.setInt("customvideotime",0)
                    Toast.makeText(requireContext(),"Video Call Scheduled for 1 minute", Toast.LENGTH_LONG).show()
                    videocount=0
                }
                4->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, pendingIntent)
                    prefutil?.setInt("customvideotime",0)
                    Toast.makeText(requireContext(),"Video Call Scheduled for 5 minute", Toast.LENGTH_LONG).show()

                    videocount=0
                }
                else -> Toast.makeText(requireContext(), "Please select time in Schedule", Toast.LENGTH_SHORT).show()

            }
        }


    }
    private val requestWriteSettingsPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            val intent = Intent(requireContext(), RingtoneActivity::class.java)
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