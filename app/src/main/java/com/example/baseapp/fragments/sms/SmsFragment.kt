package com.example.baseapp.fragments.sms

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
import com.example.baseapp.adsManager.RewardAdObjectManager.coin
import com.example.baseapp.alarmManagers.AlarmReceiver
import com.example.baseapp.alarmManagers.SmSReciver
import com.example.baseapp.ringtones.RingtoneActivity
import com.example.baseapp.schedule.ScheduleActivity
import com.example.baseapp.secondPermission.SecondPermissionActivity
import com.example.baseapp.utils.EventNames.RINGTONE_CUSTOM_SMS
import com.example.baseapp.utils.EventNames.RINGTONE_SMS
import com.example.baseapp.utils.EventNames.SCEDHULE_SMS
import com.example.baseapp.utils.EventNames.SCHEDULECALL_AUDIOCALLBTN
import com.example.baseapp.utils.EventNames.SCHEDULE_SMSACTIVITY
import com.example.baseapp.utils.EventNames.THEME_SMS
import com.example.baseapp.utils.EventNames.showSnackbarWithIcon
import com.example.baseapp.utils.FirebaseCustomEvents
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentSmsBinding


class SmsFragment : Fragment() {

    private var leftRightAnimation: ObjectAnimator? = null
    private lateinit var binding: FragmentSmsBinding
    lateinit var prefutil: PrefUtil
    var count=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefutil= PrefUtil(requireContext())


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_sms, container, false)

        binding = FragmentSmsBinding.inflate(inflater, container, false)
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
        binding.themesconst.setOnClickListener {
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                THEME_SMS,
                "true"
            )
            val intent = Intent(context, ThemeSmsActivity::class.java)
            context?.startActivity(intent)
        }
        var unlcoked=PrefUtil(requireContext()).getBool("unlocked",false)

        binding.scheduleconst.setOnClickListener {
            binding.scheduleconst.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
            leftRightAnimation?.cancel()
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                SCHEDULE_SMSACTIVITY,
                "true"
            )
            val intent = Intent(it.context, ScheduleActivity::class.java)
            intent.putExtra("Key", "sms")
            it.context.startActivity(intent)

        }
        binding.ringtoneconst.setOnClickListener {
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                RINGTONE_SMS,
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
            var profileInt = PrefUtil(requireContext()).getInt("profile_int",0)

            count= prefutil.getInt("sms_count", 0)
            Log.d("Testtag",count.toString())
            Log.d("Testtag",count.toString())


//            if(!unlcoked){
//                coin = PrefUtil(requireContext()).getInt("coinValue", 0)
//                if (profileInt in listOf(1, 8)) {
//                    coin= coin-5
//                    PrefUtil(requireContext()).setInt("coinValue", coin)
//                }
//            }
//            else{
//                PrefUtil(requireContext()).setBool("unlocked",false)
//
//            }

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

//        binding.schedulenbtn.setOnClickListener {
//            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(SCEDHULE_SMS, "true")
//
////            val serviceIntent = Intent(requireContext(), MessagingForegroundService::class.java).apply {
////                putExtra("profile_name", "John Doe") // Example profile name
////            }
////
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                requireContext().startForegroundService(serviceIntent)
////            } else {
////                requireContext().startService(serviceIntent)
////            }
//        }
    }




    override fun onResume() {
        binding.scheduleconst.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        leftRightAnimation?.cancel()
        super.onResume()


    }

    override fun onDestroy() {
        leftRightAnimation?.cancel()
        super.onDestroy()
    }

}