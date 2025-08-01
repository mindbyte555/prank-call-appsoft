package com.example.baseapp.fragments.CustomFragment.customCallFragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import com.example.baseapp.alarmManagers.CustomcallReciver
import com.example.baseapp.ringtones.RingtoneActivity
import com.example.baseapp.schedule.ScheduleActivity
import com.example.baseapp.secondPermission.SecondPermissionActivity
import com.example.baseapp.themes.ThemeActivity
import com.example.baseapp.utils.EventNames.CUSTOMCALL_RINGTONE
import com.example.baseapp.utils.EventNames.CUSTOMCALL_SCEDHULEBUTTON
import com.example.baseapp.utils.EventNames.CUSTOMCALL_SCHDULEACTIVITY
import com.example.baseapp.utils.EventNames.CUSTOMCALL_THEME
import com.example.baseapp.utils.FirebaseCustomEvents
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.databinding.FragmentCustomCallBinding

class CustomCallFragment : Fragment() {
    private lateinit var binding: FragmentCustomCallBinding
    lateinit var prefutil: PrefUtil
    var count=0
    var videoname=""
    var videonum=""
    var videouri=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefutil= PrefUtil(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomCallBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Testtag", count.toString())

        binding.customscheduleconst.setOnClickListener {

            videouri= prefutil.getString("profile_img").toString()
            videoname=prefutil.getString("custm_profile").toString()
            videonum=prefutil.getString("custm_num").toString()
            if(videouri!=""&& videoname!=""&& videonum!="" ){
                FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                    CUSTOMCALL_SCHDULEACTIVITY,
                    "true"
                )
                val intent = Intent(it.context, ScheduleActivity::class.java)
                intent.putExtra("Key", "customcall")
                intent.putExtra("custom_profile",videoname)
                intent.putExtra("custom_mobile",videonum)
                intent.putExtra("custom_uri",videouri)

                it.context.startActivity(intent)
            }
            else{

                Toast.makeText(requireActivity(),"Please first select image ,add name and number",Toast.LENGTH_LONG).show()

                Log.d("Testtag","No uri senior")
            }
        }
        binding.customringtoneconst.setOnClickListener {
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                CUSTOMCALL_RINGTONE,
                "true"
            )

            if (Settings.System.canWrite(context)) {
                val intent = Intent(context, RingtoneActivity::class.java)
                context?.startActivity(intent)
            } else {
                val intent = Intent(it.context, SecondPermissionActivity::class.java)
                intent.putExtra("key", "audiocall")
                it.context.startActivity(intent)
//                checkAndRequestWriteSettingsPermission()
            }

        }
        binding.customthemesconst.setOnClickListener {

            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                CUSTOMCALL_THEME,
                "true"
            )
            val intent = Intent(context, ThemeActivity::class.java)
            context?.startActivity(intent)
        }
        binding.customschedulenbtn.setOnClickListener {
            if(count!=0){
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                CUSTOMCALL_SCEDHULEBUTTON,
                "true"
            )}
            count= prefutil.getInt("customaudiotime", 0)
            Log.d("Testtag",count.toString())


            val alarmIntent = Intent(requireContext(), CustomcallReciver::class.java)

            val pendingIntent =
                PendingIntent.getBroadcast(requireContext(), 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)

            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            when(count){
                1->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent)
//                    prefutil?.setInt("customaudiotime",0)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent)
                    prefutil?.setInt("customaudiotime",0)

                    Toast.makeText(requireContext(),"Call Scheduled for 10 sec", Toast.LENGTH_LONG).show()

                    count=0
                }
                2->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, pendingIntent)
                    prefutil?.setInt("customaudiotime",0)
                    Toast.makeText(requireContext(),"Call Scheduled for 30 sec", Toast.LENGTH_LONG).show()

                    count=0
                }
                3->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent)
                    prefutil?.setInt("customaudiotime",0)
                    Toast.makeText(requireContext(),"Call Scheduled for 1 minute", Toast.LENGTH_LONG).show()

                    count=0
                }
                4->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, pendingIntent)
                    prefutil?.setInt("customaudiotime",0)
                    Toast.makeText(requireContext(),"Call Scheduled for 5 minute", Toast.LENGTH_LONG).show()

                    count=0
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


}