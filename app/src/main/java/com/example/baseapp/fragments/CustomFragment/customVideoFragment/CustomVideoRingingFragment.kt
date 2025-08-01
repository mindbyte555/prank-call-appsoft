package com.example.baseapp.fragments.CustomFragment.customVideoFragment

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
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.baseapp.Application.MyApplication.Companion.interstitialEnabled
import com.example.baseapp.Application.RingingForegroundService
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.adsManager.AdsManager.Companion.showInterstitial
import com.example.baseapp.fragments.VideoCall.VideoRingingFragment
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentCustomVideoRingingBinding
import com.example.fakecall.databinding.Videothem4Binding
import com.example.fakecall.databinding.Videotheme2Binding
import com.example.fakecall.databinding.Videotheme3Binding
import com.example.fakecall.databinding.Videotheme5Binding
import com.example.fakecall.databinding.Videotheme6Binding
import com.example.swipebutton_library.OnActiveListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CustomVideoRingingFragment : Fragment() {
    private lateinit var binding: FragmentCustomVideoRingingBinding
    private lateinit var binding2: Videotheme2Binding
    private lateinit var binding3: Videotheme3Binding
    var btnhide:RelativeLayout?=null
    private lateinit var binding4: Videothem4Binding
    private lateinit var binding5: Videotheme5Binding
    private lateinit var binding6: Videotheme6Binding
    lateinit var prefutil: PrefUtil
    var themeselected=0
    var ringtone: Ringtone? = null

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private var onOrOff = false
    var cameraallow=false
    var playring=false

    private var job: Job? = null
    private val handler = Handler(Looper.getMainLooper())
    private val runnableCode = object : Runnable {
        override fun run() {
            val context = context
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
                handler.postDelayed(this, 1000)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startForegroundService()
        setRetainInstance(true)
        btnhide=requireActivity().findViewById(R.id.bannerAdmain)
        btnhide?.visibility=View.GONE
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)


            bottomNavigation?.visibility = View.GONE
        }catch (e : Exception){
            e.printStackTrace()
        }
        prefutil= PrefUtil(requireContext())

//        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
//        ringtone = RingtoneManager.getRingtone(context, notification)
        val notification: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

        if (notification != null) {
            try {
                ringtone = RingtoneManager.getRingtone(requireContext(), notification)
                if(ringtone!=null)
                {
                    playring=true

                }//                Log.d("TESTTAG", "Playing Ringtone URI: ${ringtone.getTitle(requireContext())}")
                Log.d("TESTTAG", "inside catch")

            } catch (e: SecurityException) {
                Log.d("TESTTAG", "SecurityException: ${e.message}")
                playring=false
                // Handle the security exception
                // Provide a default sound as a fallback
//                val defaultSound = RingtoneManager.getRingtone(requireContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            } catch (e: Exception) {
                Log.d("TESTTAG", "Error obtaining ringtone: ${e.message}")
                playring=false
                // Handle other exceptions
                // Provide a default sound as a fallback
//                val defaultSound = RingtoneManager.getRingtone(requireContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            }
        } else {
            playring=false

            Log.d("TESTTAG", "Ringtone URI is null")
            // Provide a default sound as a fallback
        }


//        }
        cameraManager = (requireContext()?.getSystemService(Context.CAMERA_SERVICE) as? CameraManager)!!
        try {
            // Get the list of camera IDs
            val cameraIdList = cameraManager.cameraIdList

            // Check if the list is not empty
            if (cameraIdList.isNotEmpty()) {
                // Get the ID of the back camera
                cameraId = cameraIdList[0]
                cameraallow=true

            } else {
                // Handle the case where no cameras are available
                Log.e("CameraFragment", "No cameras available on this device.")
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        checkModeAndAct()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCustomVideoRingingBinding.inflate(inflater, container, false)

        binding2= Videotheme2Binding.inflate(inflater, container, false)
        binding3= Videotheme3Binding.inflate(inflater, container, false)
        binding4= Videothem4Binding.inflate(inflater, container, false)
        binding5= Videotheme5Binding.inflate(inflater, container, false)
        binding6= Videotheme6Binding.inflate(inflater, container, false)


        themeselected=prefutil.getInt("lasttheme",0)
        when(themeselected){
            0->{
                binding= FragmentCustomVideoRingingBinding.inflate(inflater, container, false)
                return binding.root
            }
            1->{
                binding2= Videotheme2Binding.inflate(inflater, container, false)
                return binding2.root

            }
            2->{
                binding3= Videotheme3Binding.inflate(inflater, container, false)
                return binding3.root

            }
            3->{
                binding4= Videothem4Binding.inflate(inflater, container, false)
                return binding4.root

            }
            4->{
                binding5= Videotheme5Binding.inflate(inflater, container, false)
                return binding5.root

            }
            5->{
                binding6= Videotheme6Binding.inflate(inflater, container, false)
                return binding6.root

            }
            else->{
                binding= FragmentCustomVideoRingingBinding.inflate(inflater, container, false)
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

        val message = arguments?.getString("custom_profile")
        val uri=arguments?.getString("custom_uri")
        var urito=uri?.toUri()

        Log.d("Testtag",uri.toString())

        binding.customvideoprofilename.text=message




        if(themeselected==0){
            binding.customvideoprofilename.text = message

            try {
                Glide.with(this)
                    .load(urito)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.customvideoimagecall)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            binding.customvideoendcallconst.setOnClickListener {
                findNavController().popBackStack(R.id.mainFragment, false)
                stopRingtone()

                Log.d("iaminins", "onViewCreated: Ad  is called")
                AdsManager.showInterstitial(true, requireActivity(), object :
                    AdsManager.InterstitialAdListener {
                    override fun onAdClosed() {
                        val homeImg = requireActivity().findViewById<ImageView>(R.id.homeImg)
//                        val addpeopleimg =
//                            requireActivity().findViewById<ImageView>(R.id.addpeopleimg)
                        val settingicon =
                            requireActivity().findViewById<ImageView>(R.id.settingicon)

                        homeImg.setImageResource(R.drawable.ic_homeselect)
//                        addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
                        settingicon.setImageResource(R.drawable.ic_settingunsele)
                        videoring = false
//                        stopRingtone()
//                        stopForegroundService()

                        findNavController().popBackStack(R.id.mainFragment, false)
                    }

                    })
                stopForegroundService()

            }

//            binding.customvideorecievecallconst.setOnTouchListener { view, event ->
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN -> true
//                    MotionEvent.ACTION_MOVE -> {
//                        val newY = event.rawY - view.height / 2
//                        val halfScreen = resources.displayMetrics.heightPixels / 2
//                        if (newY <= halfScreen) {
//                            view.y = halfScreen.toFloat()
//                        } else {
//                            view.y = newY
//                        }
//                        Log.d("TouchMove", "New Y: $newY, View Y: ${view.y}, Half Screen: $halfScreen")
//                        true
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        val halfScreen = resources.displayMetrics.heightPixels / 2
//                        if (view.y <= halfScreen) {
//                            Log.d("TouchUp", "Inside Action: View Y: ${view.y}, Half Screen: $halfScreen")
//                            videoring =false
//                            stopRingtone()
//                            val action = CustomVideoRingingFragmentDirections.actionCustomVideoRingingFragmentToCustomVideoplayerFragment(message!!)
//                            findNavController().navigate(action)
//                        }
//                        true
//                    }
//                    else -> false
//                }
//            }

            binding.customvideorecievecallconst.setOnTouchListener { view, event ->
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
                            Log.d("TouchUp", "Inside Action: View Y: ${view.y}, Half Screen: $halfScreen")
                            videoring = false
                            stopRingtone()
                            val action = CustomVideoRingingFragmentDirections.actionCustomVideoRingingFragmentToCustomVideoplayerFragment(message!!)
                            findNavController().navigate(action)
                        }
                        true
                    }
                    else -> false
                }
            }


        }
        else if(themeselected==1){
            binding2.profilename.text = message
            try {
                Glide.with(this)
                    .load(urito)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding2.imagecall)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding2.endcallconst.setOnClickListener {
                Log.d("iaminins", "onViewCreated: Ad is called")
                stopRingtone()
                findNavController().popBackStack(R.id.mainFragment, false)

                AdsManager.showInterstitial(true, requireActivity(), object :
                    AdsManager.InterstitialAdListener {
                    override fun onAdClosed() {
                        val homeImg = requireActivity().findViewById<ImageView>(R.id.homeImg)
//                        val addpeopleimg =
//                            requireActivity().findViewById<ImageView>(R.id.addpeopleimg)
                        val settingicon =
                            requireActivity().findViewById<ImageView>(R.id.settingicon)

                        homeImg.setImageResource(R.drawable.ic_homeselect)
//                        addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
                        settingicon.setImageResource(R.drawable.ic_settingunsele)
                        videoring = false
//                        stopRingtone()

//                        findNavController().popBackStack(R.id.mainFragment, false)

                    }
                })
                stopForegroundService()
            }
            binding2.recievecallconst.setOnClickListener {
                videoring =false
                stopRingtone()
            val action = CustomVideoRingingFragmentDirections.actionCustomVideoRingingFragmentToCustomVideoplayerFragment(message!!)
            findNavController().navigate(action)


        }

    }
        else if(themeselected==2){
            binding3.profilename.text = message

            try {
                Glide.with(this)
                    .load(urito)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding3.imagecall)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding3.endcallconst.setOnClickListener {
                Log.d("iaminins", "onViewCreated: Ad is called")

                findNavController().popBackStack(R.id.mainFragment, false)
                stopRingtone()

                AdsManager.showInterstitial(true, requireActivity(), object :
                    AdsManager.InterstitialAdListener {
                    override fun onAdClosed() {
                        val homeImg = requireActivity().findViewById<ImageView>(R.id.homeImg)
//                        val addpeopleimg =
//                            requireActivity().findViewById<ImageView>(R.id.addpeopleimg)
                        val settingicon =
                            requireActivity().findViewById<ImageView>(R.id.settingicon)

                        homeImg.setImageResource(R.drawable.ic_homeselect)
//                        addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
                        settingicon.setImageResource(R.drawable.ic_settingunsele)
                        videoring = false

//                        findNavController().popBackStack(R.id.mainFragment, false)

                    }
                })
                stopForegroundService()
            }
            binding3.recievecallconst.setOnClickListener {
                videoring =false
                stopRingtone()
                val action = CustomVideoRingingFragmentDirections.actionCustomVideoRingingFragmentToCustomVideoplayerFragment(message!!)
                findNavController().navigate(action)
            }

        }
        else if(themeselected==3){
            binding4.profilename.text = message
            try {
                Glide.with(this)
                    .load(urito)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding4.imagecall)
            } catch (e: Exception) {
                e.printStackTrace()
            }



            binding4.endcallconst.setOnClickListener {
                Log.d("iaminins", "onViewCreated: Ad is called")

                findNavController().popBackStack(R.id.mainFragment, false)
                stopRingtone()

                AdsManager.showInterstitial(true, requireActivity(), object :
                    AdsManager.InterstitialAdListener {
                    override fun onAdClosed() {
                        val homeImg = requireActivity().findViewById<ImageView>(R.id.homeImg)
//                        val addpeopleimg =
//                            requireActivity().findViewById<ImageView>(R.id.addpeopleimg)
                        val settingicon =
                            requireActivity().findViewById<ImageView>(R.id.settingicon)

                        homeImg.setImageResource(R.drawable.ic_homeselect)
//                        addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
                        settingicon.setImageResource(R.drawable.ic_settingunsele)
                        videoring = false

//                        findNavController().popBackStack(R.id.mainFragment, false)

                    }
                })
                stopForegroundService()
            }
            binding4.recievecallconst.setOnClickListener {
                videoring =false
                stopRingtone()
                val action = CustomVideoRingingFragmentDirections.actionCustomVideoRingingFragmentToCustomVideoplayerFragment(message!!)
                findNavController().navigate(action)
            }

        }
        else if(themeselected==4){
            binding5.profilename.text = message
            try {
                Glide.with(this)
                    .load(urito)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding5.imagecall)
            } catch (e: Exception) {
                e.printStackTrace()
            }



            binding5.endcallconst.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> true
                    MotionEvent.ACTION_MOVE -> {
                        val newX = event.rawX - view.width / 2
                        val halfScreen = (resources.displayMetrics.widthPixels / 2)-150

                        // Ensure the view stays within the boundaries
                        if (newX <= 0) {
                            view.x = 0f
                        } else if (newX >= halfScreen) {
                            view.x = halfScreen.toFloat()
                        } else {
                            view.x = newX
                        }

                        Log.d("TouchMove", "New X: $newX, View X: ${view.x}, Half Screen: $halfScreen")
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        val halfScreen = (resources.displayMetrics.widthPixels / 2) - 150

                        if (view.x >= halfScreen) {
                            Log.d("TouchUp", "Inside Action: View X: ${view.x}, Half Screen: $halfScreen")

                            // Update UI immediately
                            val homeImg = requireActivity().findViewById<ImageView>(R.id.homeImg)
//                            val addpeopleimg = requireActivity().findViewById<ImageView>(R.id.addpeopleimg)
                            val settingicon = requireActivity().findViewById<ImageView>(R.id.settingicon)

                            homeImg.setImageResource(R.drawable.ic_homeselect)
//                            addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
                            settingicon.setImageResource(R.drawable.ic_settingunsele)

                            videoring = false
                            stopRingtone()
                            findNavController().popBackStack(R.id.mainFragment, false)

                            stopForegroundService()

                            // Show interstitial ad
                            AdsManager.showInterstitial(
                                activity = requireActivity(),
                                listener = object : AdsManager.InterstitialAdListener {
                                    override fun onAdClosed() {
                                        // Navigate only after ad is closed
//                                        findNavController().popBackStack(R.id.mainFragment, false)
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

                        Log.d("TouchMove", "New X: $newX, View X: ${view.x}, Half Screen: $halfScreen")
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        val halfScreen = (resources.displayMetrics.widthPixels / 2)

                        if (view.x <= halfScreen) {
                            Log.d("TouchUp", "Inside Action: View X: ${view.x}, Half Screen: $halfScreen")
                            videoring =false
                            stopRingtone()
                            val action = CustomVideoRingingFragmentDirections.actionCustomVideoRingingFragmentToCustomVideoplayerFragment(message!!)
                            findNavController().navigate(action)
                        }
                        true
                    }
                    else -> false
                }
            }



        }
        else if(themeselected==5){
            binding6.profilename.text = message
            try {
                Glide.with(this)
                    .load(urito)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding6.imagecall)
            } catch (e: Exception) {
                e.printStackTrace()
            }


            binding6.recievecallconst.setOnActiveListener(OnActiveListener {
                videoring =false
                stopRingtone()
                val action = CustomVideoRingingFragmentDirections.actionCustomVideoRingingFragmentToCustomVideoplayerFragment(message!!)
                findNavController().navigate(action)
                stopForegroundService()

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
    private fun vibratePhone() {
        handler.post(runnableCode)

    }

    private fun playDefaultRingtone() {
            if (playring)
            {
                ringtone!!.play()

            }
            else
            {

            }
        }

    private fun checkModeAndAct() {
        val audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        videoring = true

        when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_SILENT, AudioManager.RINGER_MODE_VIBRATE -> {
                vibratePhone()
                setSOSFlash()
            }

            AudioManager.RINGER_MODE_NORMAL -> {
                playDefaultRingtone()
                vibratePhone()
                setSOSFlash()

            }
        }
    }


    private fun stopRingtone() {
        videoring = false


        if (ringtone!!.isPlaying) {
            if (playring)
            {
                ringtone!!.stop()

            }
        }
        stopVibration()
        stopSOSFlash()

    }

    private fun stopVibration() {

        handler.removeCallbacks(runnableCode)

    }

//    private fun setSOSFlash() {
//        isRunning = true
//        onOrOff = false
//        var flashOn = true
//        turnFlash(flashOn)
//
//        job = lifecycleScope.launch {
//            while (isRunning) {
//                flashOn = !flashOn
//                turnFlash(flashOn)
//                delay(300)
//                if (flashOn && onOrOff) {
//                    isRunning = false
//                    turnFlash(false)
//                }
//            }
//        }
//    }
//
//
//    private fun turnFlash(isCheck: Boolean) {
//        try {
//            cameraManager.setTorchMode(cameraId, isCheck)
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun stopSOSFlash() {
//        isRunning = false
//        onOrOff = false
//        if (job!!.isActive) {
//            job!!.cancel()
//        }
//        turnFlash(false)
//    }
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
private fun turnFlash(isCheck: Boolean) {
    try {
        if (cameraallow) {
            cameraId?.let { id ->
                val characteristics = cameraManager.getCameraCharacteristics(id)
                val hasFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) ?: false
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
        Log.d("TestTag", "Paused")

    }

    override fun onStop() {
        super.onStop()
        Log.d("TestTag", "Stoppeed")

    }
    companion object {
        var videoring = false
        var isRunning = false
    }

    override fun onResume() {
        super.onResume()
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)

            bottomNavigation?.visibility = View.GONE
        }catch (e : Exception){
            e.printStackTrace()
        }
    }



}