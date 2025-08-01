package com.example.baseapp.fragments.CustomFragment

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.baseapp.Application.MyApplication.Companion.noadshow
import com.example.baseapp.alarmManagers.SmSReciver
import com.example.baseapp.fragments.sms.ThemeSmsActivity
import com.example.baseapp.ringtones.RingtoneActivity
import com.example.baseapp.schedule.ScheduleActivity
import com.example.baseapp.secondPermission.SecondPermissionActivity
import com.example.baseapp.utils.EventNames.CUSTOMCALL_SCHDULEACTIVITY
import com.example.baseapp.utils.EventNames.CUSTOMSMS_SCHDULEACTIVITY
import com.example.baseapp.utils.EventNames.CUSTOM_THEME_SMS
import com.example.baseapp.utils.EventNames.RINGTONE_AUDIOCALL
import com.example.baseapp.utils.EventNames.RINGTONE_CUSTOM_SMS
import com.example.baseapp.utils.EventNames.SCEDHULE_SMS
import com.example.baseapp.utils.EventNames.THEME_SMS
import com.example.baseapp.utils.EventNames.showSnackbarWithIcon
import com.example.baseapp.utils.FirebaseCustomEvents
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentCustomCallBinding
import com.example.fakecall.databinding.FragmentCustomSmsfragmentBinding


class CustomSmsfragment : Fragment() {
    private lateinit var binding: FragmentCustomSmsfragmentBinding
    private var leftRightAnimation: ObjectAnimator? = null

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
        binding = FragmentCustomSmsfragmentBinding.inflate(inflater, container, false)
        leftRightAnimation=ObjectAnimator.ofPropertyValuesHolder(
            binding.scheduleconst,
            PropertyValuesHolder.ofFloat("scaleX", 0.95f, 1.02f),
            PropertyValuesHolder.ofFloat("scaleY", 0.95f, 1.05f)
        ).apply {
            duration = 500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            interpolator = DecelerateInterpolator() // Smooth transition
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.scheduleconst.setOnClickListener {

            videouri= prefutil.getString("profile_img").toString()
            videoname=prefutil.getString("custm_profile").toString()
            videonum=prefutil.getString("custm_num").toString()
            if(videouri!=""&& videoname!=""&& videonum!="" ){
                FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                    CUSTOMSMS_SCHDULEACTIVITY,
                    "true"
                )
                val intent = Intent(it.context, ScheduleActivity::class.java)
                intent.putExtra("Key", "customsms")
                intent.putExtra("custom_profile",videoname)
                intent.putExtra("custom_mobile",videonum)
                intent.putExtra("custom_uri",videouri)

                it.context.startActivity(intent)
            }
            else{

                Toast.makeText(requireActivity(),"Please first select image ,add name and number",
                    Toast.LENGTH_LONG).show()

                Log.d("Testtag","No uri senior")
            }
        }
        binding.themesconst.setOnClickListener {
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                CUSTOM_THEME_SMS,
                "true"
            )
            val intent = Intent(context, ThemeSmsActivity::class.java)
            context?.startActivity(intent)
        }
        binding.ringtoneconst.setOnClickListener {
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                RINGTONE_CUSTOM_SMS,
                "true"
            )
            Log.d("Testag","Inside ringtone")
//            val intent = Intent(context, RingtoneActivity::class.java)
//                context?.startActivity(intent)
            if (Settings.System.canWrite(context)) {
                val intent = Intent(context, RingtoneActivity::class.java)
                context?.startActivity(intent)
            } else {
                Log.d("Testag","Inside ringtone else")

                val intent = Intent(it.context, SecondPermissionActivity::class.java)
                intent.putExtra("key", "audiocall")
                it.context.startActivity(intent)
            }
        }
        binding.schedulenbtn.setOnClickListener {
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(SCEDHULE_SMS, "true")

            noadshow =false

            count= prefutil.getInt("sms_count", 0)
            Log.d("Testtag",count.toString())

            prefutil.setBool("custom",true)

            val alarmIntent = Intent(requireContext(), SmSReciver::class.java)

            val pendingIntent =
                PendingIntent.getBroadcast(requireContext(), 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)

            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            when(count){

                1->{

                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent)
                    prefutil?.setInt("sms_count",0)

                    Toast.makeText(requireContext(),"SMS Scheduled for 10 sec", Toast.LENGTH_LONG).show()

                    count=0
                }
                2->{

                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, pendingIntent)
                    prefutil?.setInt("sms_count",0)
                    Toast.makeText(requireContext(),"SMS Scheduled for 30 sec", Toast.LENGTH_LONG).show()

                    count=0
                }
                3->{

                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent)
                    prefutil?.setInt("sms_count",0)
                    Toast.makeText(requireContext(),"SMS Scheduled for 1 minute", Toast.LENGTH_LONG).show()

                    count=0
                }
                4->{

                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, pendingIntent)
                    prefutil?.setInt("sms_count",0)
                    Toast.makeText(requireContext(),"SMS Scheduled for 5 minute", Toast.LENGTH_LONG).show()

                    count=0
                }
                else ->
                {
                    binding.scheduleconst.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(requireContext(), R.color.ripple_color))
                    leftRightAnimation?.start()
                    showSnackbarWithIcon(requireActivity(), requireView(), "Please select time in Schedule",R.drawable.snackschedule)
                }

            }
        }

    }


}