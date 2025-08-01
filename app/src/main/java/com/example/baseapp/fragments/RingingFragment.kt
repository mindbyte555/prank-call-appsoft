package com.example.baseapp.fragments

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope

import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.baseapp.Application.MyApplication.Companion.noadshow
import com.example.baseapp.Application.RingingForegroundService
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.adsManager.RewardAdObjectManager.coin
import com.example.baseapp.fragments.CustomFragment.customCallFragment.CustomCallRingingFragment.Companion.checkring
import com.example.baseapp.fragments.VideoCall.VideoRingingFragment
import com.example.baseapp.utils.PrefUtil


import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentRingingBinding
import com.example.fakecall.databinding.Theme2Binding
import com.example.fakecall.databinding.Theme3Binding
import com.example.fakecall.databinding.Theme4Binding
import com.example.fakecall.databinding.Theme5Binding
import com.example.fakecall.databinding.Theme6Binding
import com.example.swipebutton_library.OnActiveListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.checkerframework.common.subtyping.qual.Bottom


class RingingFragment : Fragment() {
    private lateinit var binding: FragmentRingingBinding
    private lateinit var binding2: Theme2Binding
    private lateinit var binding3: Theme3Binding
    private lateinit var binding4: Theme4Binding
    private lateinit var binding5: Theme5Binding
    private lateinit var binding6: Theme6Binding
    private lateinit var bottomNav: ConstraintLayout
    var cameraallow = false
    var btnhide: RelativeLayout? = null


    lateinit var prefutil: PrefUtil

    //    lateinit var ringtone: Ringtone
    var ringtone: Ringtone? = null

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private var onOrOff = false
    var themeselected = 0
    var playring = false


    private var job: Job? = null
    private val handler = Handler(Looper.getMainLooper())

    private val runnableCode = object : Runnable {
        override fun run() {
            val context = context // Using the nullable context
            if (context != null) {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            600, VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(600)
                }
                // Schedule the next run
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startForegroundService()
        btnhide = requireActivity().findViewById(R.id.bannerAdmain)
        btnhide?.visibility = View.GONE
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)
            bottomNavigation?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
        prefutil = PrefUtil(requireContext())


        setRetainInstance(true)

        val notification: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

        if (notification != null) {
            try {
                ringtone = RingtoneManager.getRingtone(requireContext(), notification)
                if (ringtone != null) {
                    playring = true

                }
                //Log.d("TESTTAG", "Playing Ringtone URI: ${ringtone.getTitle(requireContext())}")
                Log.d("TESTTAG", "inside catch")

            } catch (e: SecurityException) {
                Log.d("TESTTAG", "SecurityException: ${e.message}")
                playring = false
                // Handle the security exception
                // Provide a default sound as a fallback
//                val defaultSound = RingtoneManager.getRingtone(requireContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            } catch (e: Exception) {
                Log.d("TESTTAG", "Error obtaining ringtone: ${e.message}")
                playring = false
                // Handle other exceptions
                // Provide a default sound as a fallback
//                val defaultSound = RingtoneManager.getRingtone(requireContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            }
        } else {
            playring = false

            Log.d("TESTTAG", "Ringtone URI is null")
            // Provide a default sound as a fallback
        }

        cameraManager =
            (requireContext()?.getSystemService(Context.CAMERA_SERVICE) as? CameraManager)!!
        try {
            // Get the list of camera IDs
            val cameraIdList = cameraManager.cameraIdList

            // Check if the list is not empty
            if (cameraIdList.isNotEmpty()) {
                // Get the ID of the back camera
                cameraId = cameraIdList[0]
                cameraallow = true

            } else {
                // Handle the case where no cameras are available
                Log.e("CameraFragment", "No cameras available on this device.")
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

//
//        try {
//            // Get the ID of the back camera
//            cameraId = cameraManager.cameraIdList[0]
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }
        checkModeAndAct()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRingingBinding.inflate(inflater, container, false)

        binding2 = Theme2Binding.inflate(inflater, container, false)
        binding3 = Theme3Binding.inflate(inflater, container, false)
        binding4 = Theme4Binding.inflate(inflater, container, false)
        binding5 = Theme5Binding.inflate(inflater, container, false)
        binding6 = Theme6Binding.inflate(inflater, container, false)


        themeselected = prefutil.getInt("lasttheme", 0)
        when (themeselected) {
            0 -> {
                binding = FragmentRingingBinding.inflate(inflater, container, false)
                return binding.root
            }

            1 -> {
                binding2 = Theme2Binding.inflate(inflater, container, false)
                return binding2.root

            }

            2 -> {
                binding3 = Theme3Binding.inflate(inflater, container, false)
                return binding3.root

            }

            3 -> {
                binding4 = Theme4Binding.inflate(inflater, container, false)
                return binding4.root

            }

            4 -> {
                binding5 = Theme5Binding.inflate(inflater, container, false)
                return binding5.root

            }

            5 -> {
                binding6 = Theme6Binding.inflate(inflater, container, false)
                return binding6.root

            }

            else -> {
                binding = FragmentRingingBinding.inflate(inflater, container, false)
                return binding.root
            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            })

        val message = arguments?.getString("profilename")
        val number = arguments?.getInt("profiileno")

        var unlcoked=PrefUtil(requireContext()).getBool("unlocked",false)
        if (themeselected == 0) {
            binding.profilename.text = message
            if(number==-1){
                var img=PrefUtil(requireContext()).getString("profile_img").toString()
                if(img!=""){
                    Glide.with(this)
                        .load(img.toUri())
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding.imagecall)
                }
                else{
                    Glide.with(this)
                        .load(R.drawable.ic_alex)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding.imagecall)
                }
            }
            else if (number == 1) {
                Glide.with(this)
                    .load(R.drawable.ic_alex)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)

            } else if (number == 2) {
                Glide.with(this)
                    .load(R.drawable.ic_messi)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)

            } else if (number == 3) {
                Glide.with(this)
                    .load(R.drawable.ic_emma)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)

            } else if (number == 4) {
                Glide.with(this)
                    .load(R.drawable.ic_ron)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)

            } else if (number == 5) {
                Glide.with(this)
                    .load(R.drawable.ic_ang)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)

            } else if (number == 6) {
                Glide.with(this)
                    .load(R.drawable.ic_pry)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)

            } else if (number == 7) {
                Glide.with(this)
                    .load(R.drawable.ic_tom)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)

            } else if (number == 8) {
                Glide.with(this)
                    .load(R.drawable.ic_keenu)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)

            }
            else if (number == 9) {

                Glide.with(this)
                    .load(R.drawable.ic_ana)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)
            }
            else if (number == 10) {

                Glide.with(this)
                    .load(R.drawable.ic_alina)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)
            }
            else if (number == 11) {

                Glide.with(this)
                    .load(R.drawable.ic_song)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)
            }
            else if (number == 12) {

                Glide.with(this)
                    .load(R.drawable.ic_depp)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)
            }
            else if (number == 13) {

                Glide.with(this)
                    .load(R.drawable.ic_scarr)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)
            }

            else if (number == 15) {

                Glide.with(this)
                    .load(R.drawable.ic_lee)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)
            }
            else if (number == 16) {

                Glide.with(this)
                    .load(R.drawable.ic_charlie)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)
            }
            else if (number == 17) {

                Glide.with(this)
                    .load(R.drawable.ic_trump)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)
            }
            else if (number == 18) {

                Glide.with(this)
                    .load(R.drawable.ic_modi)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)
            }



            binding.endcallconst.setOnClickListener {
                Log.d("iaminins", "onViewCreated: Ad is called")
                checkring = false
                stopRingtone()
                findNavController().popBackStack(R.id.mainFragment, false)
                if(!unlcoked){
                    coin = PrefUtil(requireContext()).getInt("coinValue", 0)
                    var profileInt = PrefUtil(requireContext()).getInt("profile_int",0)
                    if (profileInt in listOf(1, 8, 9, 10,11,13,17,18)) {
                        coin= coin-5
                        PrefUtil(requireContext()).setInt("coinValue", coin)
                    }
                }
                else{
                    PrefUtil(requireContext()).setBool("unlocked",false)

                }

                AdsManager.showInterstitial(true, requireActivity(), object :
                    AdsManager.InterstitialAdListener {
                    override fun onAdClosed() {



                    }
                })
                stopForegroundService()
            }


            binding.recievecallconst.setOnTouchListener { view, event ->
                val initialY = view.y
                val halfScreen = resources.displayMetrics.heightPixels / 2


                when (event.action) {
                    MotionEvent.ACTION_DOWN -> true
                    MotionEvent.ACTION_MOVE -> {
                        val newY = event.rawY - view.height / 2
                        // Ensure view only moves upwards and not beyond half screen
                        if (newY < initialY && newY >= halfScreen) {
                            view.y = newY
                        } else if (newY < halfScreen) {
                            view.y = halfScreen.toFloat()
                        }
                        Log.d("TouchMove", "New Y: $newY, View Y: ${view.y}")
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        if (view.y <= halfScreen) {
                            Log.d(
                                "TouchUp",
                                "Inside Action: View Y: ${view.y}, Half Screen: $halfScreen"
                            )

                            // Perform your action here

                            checkring = false
                            stopRingtone()
                            stopVibration()
                            stopSOSFlash()


                            val action =
                                RingingFragmentDirections.actionRingingFragmentToAudioPlayerFragment(
                                    message!!,
                                    number!!
                                )
                            findNavController().navigate(action)
                        }
                        true

                    }


                    else -> false
                }

            }


        }
        else if (themeselected == 1) {
            binding2.profilename.text = message
            if(number==-1){
                var img=PrefUtil(requireContext()).getString("profile_img").toString()
                if(img!=""){
                    Glide.with(this)
                        .load(img.toUri())
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding2.imagecall)
                }
                else{
                    Glide.with(this)
                        .load(R.drawable.ic_alex)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding2.imagecall)
                }
            }
            else if (number == 1) {
                Glide.with(this)
                    .load(R.drawable.ic_alex)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)

            } else if (number == 2) {
                Glide.with(this)
                    .load(R.drawable.ic_messi)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)

            } else if (number == 3) {
                Glide.with(this)
                    .load(R.drawable.ic_emma)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)

            } else if (number == 4) {
                Glide.with(this)
                    .load(R.drawable.ic_ron)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)

            } else if (number == 5) {
                Glide.with(this)
                    .load(R.drawable.ic_ang)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)

            } else if (number == 6) {
                Glide.with(this)
                    .load(R.drawable.ic_pry)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)

            } else if (number == 7) {
                Glide.with(this)
                    .load(R.drawable.ic_tom)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)

            } else if (number == 8) {
                Glide.with(this)
                    .load(R.drawable.ic_keenu)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)

            }
            else if (number == 9) {

                Glide.with(this)
                    .load(R.drawable.ic_ana)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)
            }
            else if (number == 10) {

                Glide.with(this)
                    .load(R.drawable.ic_alina)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)
            }
            else if (number == 11) {

                Glide.with(this)
                    .load(R.drawable.ic_song)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)
            }
            else if (number == 12) {

                Glide.with(this)
                    .load(R.drawable.ic_depp)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)
            }
            else if (number == 13) {

                Glide.with(this)
                    .load(R.drawable.ic_scarr)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)
            }

            else if (number == 15) {

                Glide.with(this)
                    .load(R.drawable.ic_lee)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)
            }
            else if (number == 16) {

                Glide.with(this)
                    .load(R.drawable.ic_charlie)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)
            }
            else if (number == 17) {

                Glide.with(this)
                    .load(R.drawable.ic_trump)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)
            }
            else if (number == 18) {

                Glide.with(this)
                    .load(R.drawable.ic_modi)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)
            }


            binding2.endcallconst.setOnClickListener {
                Log.d("iaminins", "onViewCreated: Ad is called")
                if(!unlcoked){
                    coin = PrefUtil(requireContext()).getInt("coinValue", 0)
                    var profileInt = PrefUtil(requireContext()).getInt("profile_int",0)
                    if (profileInt in listOf(1, 8, 9, 10,11,13,17,18)) {
                        coin= coin-5
                        PrefUtil(requireContext()).setInt("coinValue", coin)
                    }
                }
                else{
                    PrefUtil(requireContext()).setBool("unlocked",false)

                }
                findNavController().popBackStack(R.id.mainFragment, false)
                checkring = false
                stopRingtone()
                AdsManager.showInterstitial(true, requireActivity(), object :
                    AdsManager.InterstitialAdListener {
                    override fun onAdClosed() {



                    }
                })
                stopForegroundService()
            }
            binding2.recievecallconst.setOnClickListener {
                checkring = false
                stopRingtone()
                val action = RingingFragmentDirections.actionRingingFragmentToAudioPlayerFragment(
                    message!!,
                    number!!
                )
                findNavController().navigate(action)

            }

        }
        else if (themeselected == 2) {
            binding3.profilename.text = message
            if(number==-1){
                var img=PrefUtil(requireContext()).getString("profile_img").toString()
                if(img!=""){
                    Glide.with(this)
                        .load(img.toUri())
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding3.imagecall)
                }
                else{
                    Glide.with(this)
                        .load(R.drawable.ic_alex)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding3.imagecall)
                }
            }
            else if (number == 1) {
                Glide.with(this)
                    .load(R.drawable.ic_alex)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)

            } else if (number == 2) {
                Glide.with(this)
                    .load(R.drawable.ic_messi)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)

            } else if (number == 3) {
                Glide.with(this)
                    .load(R.drawable.ic_emma)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)

            } else if (number == 4) {
                Glide.with(this)
                    .load(R.drawable.ic_ron)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)

            } else if (number == 5) {
                Glide.with(this)
                    .load(R.drawable.ic_ang)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)

            } else if (number == 6) {
                Glide.with(this)
                    .load(R.drawable.ic_pry)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)

            } else if (number == 7) {
                Glide.with(this)
                    .load(R.drawable.ic_tom)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)

            } else if (number == 8) {
                Glide.with(this)
                    .load(R.drawable.ic_keenu)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)

            }
            else if (number == 9) {

                Glide.with(this)
                    .load(R.drawable.ic_ana)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)
            }
            else if (number == 10) {

                Glide.with(this)
                    .load(R.drawable.ic_alina)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)
            }
            else if (number == 11) {

                Glide.with(this)
                    .load(R.drawable.ic_song)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)
            }
            else if (number == 12) {

                Glide.with(this)
                    .load(R.drawable.ic_depp)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)
            }
            else if (number == 13) {

                Glide.with(this)
                    .load(R.drawable.ic_scarr)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)
            }

            else if (number == 15) {

                Glide.with(this)
                    .load(R.drawable.ic_lee)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)
            }
            else if (number == 16) {

                Glide.with(this)
                    .load(R.drawable.ic_charlie)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)
            }
            else if (number == 17) {

                Glide.with(this)
                    .load(R.drawable.ic_trump)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)
            }
            else if (number == 18) {

                Glide.with(this)
                    .load(R.drawable.ic_modi)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)
            }



            binding3.endcallconst.setOnClickListener {
                Log.d("iaminins", "onViewCreated: Ad is called")
                if(!unlcoked){
                    coin = PrefUtil(requireContext()).getInt("coinValue", 0)
                    var profileInt = PrefUtil(requireContext()).getInt("profile_int",0)
                    if (profileInt in listOf(1, 8, 9, 10,11,13,17,18)) {
                        coin= coin-5
                        PrefUtil(requireContext()).setInt("coinValue", coin)
                    }
                }
                else{
                    PrefUtil(requireContext()).setBool("unlocked",false)

                }
                findNavController().popBackStack(R.id.mainFragment, false)

                AdsManager.showInterstitial(true, requireActivity(), object :
                    AdsManager.InterstitialAdListener {
                    override fun onAdClosed() {
                        checkring = false
                        stopRingtone()

                    }
                })
                stopForegroundService()
            }
            binding3.recievecallconst.setOnClickListener {
                checkring = false
                stopRingtone()
                val action = RingingFragmentDirections.actionRingingFragmentToAudioPlayerFragment(
                    message!!,
                    number!!
                )
                findNavController().navigate(action)

            }

        }
        else if (themeselected == 3) {

            binding4.profilename.text = message
            if(number==-1){
                var img=PrefUtil(requireContext()).getString("profile_img").toString()
                if(img!=""){
                    Glide.with(this)
                        .load(img.toUri())
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding4.imagecall)
                }
                else{
                    Glide.with(this)
                        .load(R.drawable.ic_alex)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding4.imagecall)
                }
            }
            else if (number == 1) {
                Glide.with(this)
                    .load(R.drawable.ic_alex)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)

            } else if (number == 2) {
                Glide.with(this)
                    .load(R.drawable.ic_messi)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)

            } else if (number == 3) {
                Glide.with(this)
                    .load(R.drawable.ic_emma)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)

            } else if (number == 4) {
                Glide.with(this)
                    .load(R.drawable.ic_ron)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)

            } else if (number == 5) {
                Glide.with(this)
                    .load(R.drawable.ic_ang)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)

            } else if (number == 6) {
                Glide.with(this)
                    .load(R.drawable.ic_pry)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)

            } else if (number == 7) {
                Glide.with(this)
                    .load(R.drawable.ic_tom)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)

            } else if (number == 8) {
                Glide.with(this)
                    .load(R.drawable.ic_keenu)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)

            }
            else if (number == 9) {

                Glide.with(this)
                    .load(R.drawable.ic_ana)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)
            }
            else if (number == 10) {

                Glide.with(this)
                    .load(R.drawable.ic_alina)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)
            }
            else if (number == 11) {

                Glide.with(this)
                    .load(R.drawable.ic_song)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)
            }
            else if (number == 12) {

                Glide.with(this)
                    .load(R.drawable.ic_depp)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)
            }
            else if (number == 13) {

                Glide.with(this)
                    .load(R.drawable.ic_scarr)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)
            }

            else if (number == 15) {

                Glide.with(this)
                    .load(R.drawable.ic_lee)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)
            }
            else if (number == 16) {

                Glide.with(this)
                    .load(R.drawable.ic_charlie)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)
            }
            else if (number == 17) {

                Glide.with(this)
                    .load(R.drawable.ic_trump)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)
            }
            else if (number == 18) {

                Glide.with(this)
                    .load(R.drawable.ic_modi)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)
            }


            binding4.endcallconst.setOnClickListener {
                Log.d("iaminins", "onViewCreated: Ad is called")

                stopForegroundService()

                checkring = false
                stopRingtone()
                if(!unlcoked){
                    coin = PrefUtil(requireContext()).getInt("coinValue", 0)
                    var profileInt = PrefUtil(requireContext()).getInt("profile_int",0)
                    if (profileInt in listOf(1, 8, 9, 10,11,13,17,18)) {
                        coin= coin-5
                        PrefUtil(requireContext()).setInt("coinValue", coin)
                    }
                }
                else{
                    PrefUtil(requireContext()).setBool("unlocked",false)

                }
//                findNavController().popBackStack(R.id.mainFragment, false)
                findNavController().popBackStack(R.id.mainFragment, false)

                AdsManager.showInterstitial(true, requireActivity(), object :
                    AdsManager.InterstitialAdListener {
                    override fun onAdClosed() {
//                        checkring = false
//                        stopRingtone()
//                        findNavController().popBackStack(R.id.mainFragment, false)

                    }
                })
            }
            binding4.recievecallconst.setOnClickListener {


                checkring = false
                stopRingtone()
                val action =
                    RingingFragmentDirections.actionRingingFragmentToAudioPlayerFragment(
                        message!!,
                        number!!
                    )
                findNavController().navigate(action)

            }
        }
        else if (themeselected == 4) {
            binding5.profilename.text = message
            if(number==-1){
                var img=PrefUtil(requireContext()).getString("profile_img").toString()
                if(img!=""){
                    Glide.with(this)
                        .load(img.toUri())
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding5.imagecall)
                }
                else{
                    Glide.with(this)
                        .load(R.drawable.ic_alex)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding5.imagecall)
                }
            }
            else if (number == 1) {
                Glide.with(this)
                    .load(R.drawable.ic_alex)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)

            }
            else if (number == 2) {
                Glide.with(this)
                    .load(R.drawable.ic_messi)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)

            } else if (number == 3) {
                Glide.with(this)
                    .load(R.drawable.ic_emma)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)

            } else if (number == 4) {
                Glide.with(this)
                    .load(R.drawable.ic_ron)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)

            } else if (number == 5) {
                Glide.with(this)
                    .load(R.drawable.ic_ang)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)

            } else if (number == 6) {
                Glide.with(this)
                    .load(R.drawable.ic_pry)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)

            } else if (number == 7) {
                Glide.with(this)
                    .load(R.drawable.ic_tom)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)

            } else if (number == 8) {
                Glide.with(this)
                    .load(R.drawable.ic_keenu)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)

            }
            else if (number == 9) {

                Glide.with(this)
                    .load(R.drawable.ic_ana)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)
            }
            else if (number == 10) {

                Glide.with(this)
                    .load(R.drawable.ic_alina)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)
            }
            else if (number == 11) {

                Glide.with(this)
                    .load(R.drawable.ic_song)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)
            }
            else if (number == 12) {

                Glide.with(this)
                    .load(R.drawable.ic_depp)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)
            }
            else if (number == 13) {

                Glide.with(this)
                    .load(R.drawable.ic_scarr)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)
            }

            else if (number == 15) {

                Glide.with(this)
                    .load(R.drawable.ic_lee)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)
            }
            else if (number == 16) {

                Glide.with(this)
                    .load(R.drawable.ic_charlie)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)
            }
            else if (number == 17) {

                Glide.with(this)
                    .load(R.drawable.ic_trump)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)
            }
            else if (number == 18) {

                Glide.with(this)
                    .load(R.drawable.ic_modi)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)
            }



            binding5.endcallconst.setOnTouchListener { view, event ->
                when (event.action) {

                    MotionEvent.ACTION_DOWN -> true
                    MotionEvent.ACTION_MOVE -> {
                        val newX = event.rawX - view.width / 2
                        val halfScreen = (resources.displayMetrics.widthPixels / 2) - 150

                        // Ensure the view stays within the boundaries
                        if (newX <= 0) {
                            view.x = 0f
                        } else if (newX >= halfScreen) {
                            view.x = halfScreen.toFloat()
                        } else {
                            view.x = newX
                        }

                        Log.d(
                            "TouchMove",
                            "New X: $newX, View X: ${view.x}, Half Screen: $halfScreen"
                        )
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        val halfScreen = (resources.displayMetrics.widthPixels / 2) - 150

                        if (view.x >= halfScreen) {
                            Log.d(
                                "TouchUp",
                                "Inside Action: View X: ${view.x}, Half Screen: $halfScreen"
                            )

                            checkring = false
                            stopRingtone()
                            stopForegroundService()
                            if(!unlcoked){
                                coin = PrefUtil(requireContext()).getInt("coinValue", 0)
                                var profileInt = PrefUtil(requireContext()).getInt("profile_int",0)
                                if (profileInt in listOf(1, 8, 9, 10,11,13,17,18)) {
                                    coin= coin-5
                                    PrefUtil(requireContext()).setInt("coinValue", coin)
                                }
                            }
                            else{
                                PrefUtil(requireContext()).setBool("unlocked",false)

                            }
                            findNavController().popBackStack(R.id.mainFragment, false)

                            Log.d("mainringing", "onViewCreated: ads generated")

                            AdsManager.showInterstitial(
                                activity = requireActivity(),
                                listener = object : AdsManager.InterstitialAdListener {
                                    override fun onAdClosed() {
                                    }
                                }
                            )
                        }

                        true
                    }

                    else -> false
                }
            }


            binding5.recievecallconst.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> true
                    MotionEvent.ACTION_MOVE -> {
                        val newX = event.rawX - view.width / 2
                        val halfScreen = (resources.displayMetrics.widthPixels / 2)

                        // Ensure the view stays within the boundaries
                        if (newX <= halfScreen) {
                            view.x = halfScreen.toFloat()
                        } else if (newX >= resources.displayMetrics.widthPixels - view.width) {
                            view.x = (resources.displayMetrics.widthPixels - view.width).toFloat()
                        } else {
                            view.x = newX
                        }

                        Log.d(
                            "TouchMove",
                            "New X: $newX, View X: ${view.x}, Half Screen: $halfScreen"
                        )
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        val halfScreen = (resources.displayMetrics.widthPixels / 2)

                        if (view.x <= halfScreen) {
                            Log.d(
                                "TouchUp",
                                "Inside Action: View X: ${view.x}, Half Screen: $halfScreen"
                            )
                            checkring = false
                            stopRingtone()

                            val action =
                                RingingFragmentDirections.actionRingingFragmentToAudioPlayerFragment(
                                    message!!,
                                    number!!
                                )
                            findNavController().navigate(action)
                        }
                        true
                    }

                    else -> false
                }
            }


        }
        else if (themeselected == 5) {
            binding6.profilename.text = message
            if(number==-1){
                var img=PrefUtil(requireContext()).getString("profile_img").toString()
                if(img!=""){
                    Glide.with(this)
                        .load(img.toUri())
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding6.imagecall)
                }
                else{
                    Glide.with(this)
                        .load(R.drawable.ic_alex)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding6.imagecall)
                }
            }
            else if (number == 1) {
                Glide.with(this)
                    .load(R.drawable.ic_alex)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)

            } else if (number == 2) {
                Glide.with(this)
                    .load(R.drawable.ic_messi)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)

            } else if (number == 3) {
                Glide.with(this)
                    .load(R.drawable.ic_emma)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)

            } else if (number == 4) {
                Glide.with(this)
                    .load(R.drawable.ic_ron)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)

            } else if (number == 5) {
                Glide.with(this)
                    .load(R.drawable.ic_ang)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)

            } else if (number == 6) {
                Glide.with(this)
                    .load(R.drawable.ic_pry)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)

            } else if (number == 7) {
                Glide.with(this)
                    .load(R.drawable.ic_tom)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)

            } else if (number == 8) {
                Glide.with(this)
                    .load(R.drawable.ic_keenu)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)

            }
            else if (number == 9) {

                Glide.with(this)
                    .load(R.drawable.ic_ana)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)
            }
            else if (number == 10) {

                Glide.with(this)
                    .load(R.drawable.ic_alina)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)
            }
            else if (number == 11) {

                Glide.with(this)
                    .load(R.drawable.ic_song)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)
            }
            else if (number == 12) {

                Glide.with(this)
                    .load(R.drawable.ic_depp)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)
            }
            else if (number == 13) {

                Glide.with(this)
                    .load(R.drawable.ic_scarr)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)
            }

            else if (number == 15) {

                Glide.with(this)
                    .load(R.drawable.ic_lee)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)
            }
            else if (number == 16) {

                Glide.with(this)
                    .load(R.drawable.ic_charlie)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)
            }
            else if (number == 17) {

                Glide.with(this)
                    .load(R.drawable.ic_trump)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)
            }
            else if (number == 18) {

                Glide.with(this)
                    .load(R.drawable.ic_modi)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)
            }


            binding6.recievecallconst.setOnActiveListener(OnActiveListener {
                checkring = false
                stopRingtone()
                stopForegroundService()
                val action = RingingFragmentDirections.actionRingingFragmentToAudioPlayerFragment(
                    message!!,
                    number!!
                )
                findNavController().navigate(action)

            })


        }


    }

    private fun startForegroundService() {
        val message = arguments?.getString("profilename") ?: "Unknown"
        val number = arguments?.getInt("profiileno") ?: 0

        val serviceIntent = Intent(requireContext(), RingingForegroundService::class.java).apply {
            putExtra("profile_name", message)
            putExtra("profile_number", number)
            putExtra("theme_selected", themeselected)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(serviceIntent)
        } else {
            requireContext().startService(serviceIntent)
        }
    }

    private fun stopForegroundService() {

        context?.let {
            val serviceIntent = Intent(it, RingingForegroundService::class.java)
            it.stopService(serviceIntent)
        }

    }


    // Modify your checkModeAndAct function to use the service


    // Modify your stopRingtone function


    private fun vibratePhone() {
        handler.post(runnableCode)

    }

    private fun playDefaultRingtone() {
        if (playring) {
            ringtone!!.play()

        } else {

        }
    }

    private fun checkModeAndAct() {

        val audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        checkring = true
        //  stopForegroundService()

        when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_SILENT, AudioManager.RINGER_MODE_VIBRATE -> {
                vibratePhone()
                if (cameraallow) {
                    setSOSFlash()
                }
            }

            AudioManager.RINGER_MODE_NORMAL -> {
                playDefaultRingtone()
                vibratePhone()
                if (cameraallow) {
                    setSOSFlash()
                }
            }
        }
    }


    private fun stopRingtone() {
        checkring = false

        if (ringtone != null) {
            if (ringtone!!.isPlaying) { // Now it's safer to use !! inside this block
                if (playring) {
                    ringtone!!.stop()
                }
            }
        }
        stopVibration()
        stopSOSFlash()
    }

    private fun stopVibration() {

        handler.removeCallbacks(runnableCode)
        stopForegroundService()

    }

    private fun hasFlash(cameraId: String): Boolean {
        return try {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) ?: false
        } catch (e: CameraAccessException) {
            Log.e("FlashController", "Failed to access camera characteristics: ${e.message}")
            false
        } catch (e: IllegalArgumentException) {
            Log.e("FlashController", "Camera $cameraId does not exist: ${e.message}")
            false
        }
    }

    //    private fun turnFlash(isCheck: Boolean) {
//        try {
//            cameraId?.let { id ->
//                if (hasFlash(id)) {
//                    cameraManager.setTorchMode(id, isCheck)
//                } else {
//                    Log.e("FlashController", "Camera $id does not have a flash unit")
//                }
//            }
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }
//    }
    //ForeGround Service Logic

    private fun turnFlash(isCheck: Boolean) {
        try {
            if (cameraallow) {
                cameraId?.let { id ->
                    val characteristics = cameraManager.getCameraCharacteristics(id)
                    val hasFlash =
                        characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) ?: false
                    if (hasFlash) {
                        Log.d("FlashController", "Setting torch mode for camera $id")
                        cameraManager.setTorchMode(id, isCheck)
                    } else {
                        Log.e("FlashController", "Camera $id does not have a flash unit")
                    }
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            Log.e("FlashController", "Error setting torch mode: ${e.message}")
        }
    }

    fun setSOSFlash() {
        VideoRingingFragment.isRunning = true
        onOrOff = false
        var flashOn = true
        turnFlash(flashOn)

        job = lifecycleScope.launch {
            while (VideoRingingFragment.isRunning) {
                flashOn = !flashOn
                turnFlash(flashOn)
                delay(300)
                if (flashOn && onOrOff) {
                    VideoRingingFragment.isRunning = false
                    turnFlash(false)
                }
            }
        }
    }

    fun stopSOSFlash() {
        VideoRingingFragment.isRunning = false
        onOrOff = false
        job?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
        turnFlash(false)
    }


    override fun onDestroy() {
        super.onDestroy()
        stopRingtone()
        stopForegroundService()
        isRunning = false
        Log.d("TestTag", "Destroyed")
    }


    override fun onPause() {
        super.onPause()
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)
            bottomNavigation?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d("TestTag", "Paused")

    }

    override fun onStop() {
        super.onStop()

        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)
            bottomNavigation?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d("TestTag", "Stoppeed")

    }

    override fun onResume() {
        super.onResume()
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)

            bottomNavigation?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    companion object {
        var checkring = false
        var isRunning = false


    }

}