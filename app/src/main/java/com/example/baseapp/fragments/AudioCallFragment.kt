package com.example.baseapp.fragments

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.baseapp.Application.MyApplication.Companion.noadshow
import com.example.baseapp.adsManager.RewardAdObjectManager.coin
import com.example.baseapp.alarmManagers.AlarmReceiver
import com.example.baseapp.ringtones.RingtoneActivity
import com.example.baseapp.schedule.ScheduleActivity
import com.example.baseapp.secondPermission.SecondPermissionActivity
import com.example.baseapp.themes.ThemeActivity
import com.example.baseapp.utils.EventNames.RINGTONE_AUDIOCALL
import com.example.baseapp.utils.EventNames.SCHEDULECALL_AUDIOCALLBTN
import com.example.baseapp.utils.EventNames.SCHEDULE_AUDIOCALLACTIVITY
import com.example.baseapp.utils.EventNames.THEME_AUDIOCALL
import com.example.baseapp.utils.EventNames.showSnackbarWithIcon
import com.example.baseapp.utils.FirebaseCustomEvents
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentAudioCallBinding


class AudioCallFragment : Fragment() {
//    private lateinit var leftRightAnimation: ObjectAnimator
private var leftRightAnimation: ObjectAnimator? = null
    private lateinit var binding: FragmentAudioCallBinding
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


        binding = FragmentAudioCallBinding.inflate(inflater, container, false)
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
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Testtag", count.toString())

        binding.scheduleconst.setOnClickListener {
            binding.scheduleconst.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
            leftRightAnimation?.cancel()
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                SCHEDULE_AUDIOCALLACTIVITY,
                "true"
            )
            val intent = Intent(it.context, ScheduleActivity::class.java)
            intent.putExtra("Key", "audio")
            it.context.startActivity(intent)



        }
        binding.ringtoneconst.setOnClickListener {
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                RINGTONE_AUDIOCALL,
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
        binding.themesconst.setOnClickListener {
            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                THEME_AUDIOCALL,
                "true"
            )
            val intent = Intent(context, ThemeActivity::class.java)
            context?.startActivity(intent)
        }
        binding.schedulenbtn.setOnClickListener {


            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                SCHEDULECALL_AUDIOCALLBTN,
                "true"
            )
            noadshow =false

            count= prefutil.getInt("audiotime", 0)
            Log.d("Testtag",count.toString())


            val alarmIntent = Intent(requireContext(), AlarmReceiver::class.java)

            val pendingIntent =
                PendingIntent.getBroadcast(requireContext(), 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)

            val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            when(count){
                1->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent)
                    prefutil?.setInt("audiotime",0)

                    Toast.makeText(requireContext(),"Call Scheduled for 10 sec",Toast.LENGTH_LONG).show()

                    count=0
                }
                2->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, pendingIntent)
                    prefutil?.setInt("audiotime",0)
                    Toast.makeText(requireContext(),"Call Scheduled for 30 sec",Toast.LENGTH_LONG).show()

                    count=0
                }
                3->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent)
                    prefutil?.setInt("audiotime",0)
                    Toast.makeText(requireContext(),"Call Scheduled for 1 minute",Toast.LENGTH_LONG).show()

                    count=0
                }
                4->{
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, pendingIntent)
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, pendingIntent)
                    prefutil?.setInt("audiotime",0)
                    Toast.makeText(requireContext(),"Call Scheduled for 5 minute",Toast.LENGTH_LONG).show()

                    count=0
                }
                else ->
                  {
                      binding.scheduleconst.backgroundTintList = ColorStateList.valueOf(
                          ContextCompat.getColor(requireContext(), R.color.ripple_color))
                      leftRightAnimation?.start()
//                      Snackbar.make(requireView(), "Please select time in Schedule", Snackbar.LENGTH_SHORT).show()
                     showSnackbarWithIcon(requireActivity(), requireView(), "Please select time in Schedule",R.drawable.snackschedule)
//                    Toast.makeText(requireContext(), "Please select time in Schedule", Toast.LENGTH_SHORT).show()
                }

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
//            val intent = Intent(context, RingtoneActivity::class.java)
//            startActivity(intent)
        } else {
            // Request the WRITE_SETTINGS permission
            requestWriteSettingsPermission.launch(android.Manifest.permission.WRITE_SETTINGS)
        }
    }

    override fun onResume() {
        binding.scheduleconst.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        leftRightAnimation?.cancel()
        super.onResume()

//        checkAndRequestWriteSettingsPermission()

    }

    override fun onDestroy() {
        leftRightAnimation?.cancel()
        super.onDestroy()
    }

}