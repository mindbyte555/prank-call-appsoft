package com.example.baseapp.fragments.CustomFragment.customCallFragment

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
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.baseapp.Application.RingingForegroundService
import com.example.baseapp.fragments.VideoCall.VideoRingingFragment
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentCustomCallRingingBinding
import com.example.fakecall.databinding.Theme2Binding
import com.example.fakecall.databinding.Theme3Binding
import com.example.fakecall.databinding.Theme4Binding
import com.example.fakecall.databinding.Theme5Binding
import com.example.fakecall.databinding.Theme6Binding
import com.example.swipebutton_library.OnActiveListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CustomCallRingingFragment : Fragment() {
    private lateinit var binding: FragmentCustomCallRingingBinding
    private lateinit var binding2: Theme2Binding
    private lateinit var binding3: Theme3Binding
    private lateinit var binding4: Theme4Binding
    private lateinit var binding5: Theme5Binding
    private lateinit var binding6: Theme6Binding
    lateinit var ringtone: Ringtone
    private var btnhide: RelativeLayout? = null
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private var onOrOff = false
    var themeselected = 0
    lateinit var prefutil: PrefUtil
    var cameraallow = false

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
        setRetainInstance(true)
        prefutil = PrefUtil(requireContext())

        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(context, notification)
        cameraManager = (context?.getSystemService(Context.CAMERA_SERVICE) as? CameraManager)!!

//        try {
//            // Get the ID of the back camera
//            cameraId = cameraManager.cameraIdList[0]
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }
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
        checkModeAndAct()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomCallRingingBinding.inflate(inflater, container, false)
        binding2 = Theme2Binding.inflate(inflater, container, false)
        binding3 = Theme3Binding.inflate(inflater, container, false)
        binding4 = Theme4Binding.inflate(inflater, container, false)
        binding5 = Theme5Binding.inflate(inflater, container, false)
        binding6 = Theme6Binding.inflate(inflater, container, false)

        themeselected = prefutil.getInt("lasttheme", 0)
        when (themeselected) {
            0 -> {
                binding = FragmentCustomCallRingingBinding.inflate(inflater, container, false)
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
                binding = FragmentCustomCallRingingBinding.inflate(inflater, container, false)
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
        val uri = arguments?.getString("custom_uri")
        var urito = uri?.toUri()


        Log.d("Testtag", uri.toString())




        if (themeselected == 0) {
            binding.customprofilename.text = message
            try {
                if (uri == "alexandra") {
                    Glide.with(this)
                        .load(R.drawable.ic_alex)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding.customimagecall)
                } else {
                    Glide.with(this)
                        .load(urito)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding.customimagecall)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding.customendcallconst.setOnClickListener {


                // This will be called after ad is dismissed or skipped
                val homeImg = requireActivity().findViewById<ImageView>(R.id.homeImg)
//                val addpeopleimg = requireActivity().findViewById<ImageView>(R.id.addpeopleimg)
                val settingicon = requireActivity().findViewById<ImageView>(R.id.settingicon)

                homeImg.setImageResource(R.drawable.ic_homeselect)
//                addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
                settingicon.setImageResource(R.drawable.ic_settingunsele)

                checkring = false
                stopRingtone()
             stopForegroundService()
                findNavController().popBackStack(R.id.mainFragment, false)
            }









            binding.customrecievecallconst.setOnTouchListener { view, event ->
                Log.d("receiving", "onViewCreated: call received")
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
                            checkring = false
                            stopRingtone()

                            if (uri == "alexandra") {
                                val action =
                                    CustomCallRingingFragmentDirections.actionCustomCallRingingFragmentToCustomCallPlayerFragment(
                                        message!!,
                                        "alexandra"
                                    )
                                findNavController().navigate(action)

                            } else {
                                val action =
                                    CustomCallRingingFragmentDirections.actionCustomCallRingingFragmentToCustomCallPlayerFragment(
                                        message!!,
                                        uri!!
                                    )
                                findNavController().navigate(action)

                            }

//                            val action = CustomCallRingingFragmentDirections.actionCustomCallRingingFragmentToCustomCallPlayerFragment(message!!, uri!!)
//                            findNavController().navigate(action)
                        }
                        true
                    }

                    else -> false
                }
            }

        } else if (themeselected == 1) {
            binding2.profilename.text = message
            try {

                if (uri == "alexandra") {
                    Glide.with(this)
                        .load(R.drawable.ic_alex)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding2.imagecall)
                } else {
                    Glide.with(this)
                        .load(urito)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding2.imagecall)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


            binding2.endcallconst.setOnClickListener {

                // This will be called after ad is dismissed or skipped
                val homeImg = requireActivity().findViewById<ImageView>(R.id.homeImg)
//                val addpeopleimg = requireActivity().findViewById<ImageView>(R.id.addpeopleimg)
                val settingicon = requireActivity().findViewById<ImageView>(R.id.settingicon)

                homeImg.setImageResource(R.drawable.ic_homeselect)
//                addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
                settingicon.setImageResource(R.drawable.ic_settingunsele)

                checkring = false
                stopRingtone()
                stopForegroundService()
                findNavController().popBackStack(R.id.mainFragment, false)



            }
            binding2.recievecallconst.setOnClickListener {
                checkring = false
                stopRingtone()

                if (uri == "alexandra") {
                    val action =
                        CustomCallRingingFragmentDirections.actionCustomCallRingingFragmentToCustomCallPlayerFragment(
                            message!!,
                            "alexandra"
                        )
                    findNavController().navigate(action)

                } else {
                    val action =
                        CustomCallRingingFragmentDirections.actionCustomCallRingingFragmentToCustomCallPlayerFragment(
                            message!!,
                            uri!!
                        )
                    findNavController().navigate(action)
                }

            }

        } else if (themeselected == 2) {
            binding3.profilename.text = message
            try {
                if (uri == "alexandra") {
                    Glide.with(this)
                        .load(R.drawable.ic_alex)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding3.imagecall)
                } else {
                    Glide.with(this)
                        .load(urito)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding3.imagecall)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


            binding3.endcallconst.setOnClickListener {

                // This will be called after ad is dismissed or skipped
                val homeImg = requireActivity().findViewById<ImageView>(R.id.homeImg)
//                val addpeopleimg = requireActivity().findViewById<ImageView>(R.id.addpeopleimg)
                val settingicon = requireActivity().findViewById<ImageView>(R.id.settingicon)

                homeImg.setImageResource(R.drawable.ic_homeselect)
//                addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
                settingicon.setImageResource(R.drawable.ic_settingunsele)

                checkring = false
                stopRingtone()
               stopForegroundService()
                findNavController().popBackStack(R.id.mainFragment, false)

            }
            binding3.recievecallconst.setOnClickListener {
                checkring = false
                stopRingtone()

                if (uri == "alexandra") {
                    val action =
                        CustomCallRingingFragmentDirections.actionCustomCallRingingFragmentToCustomCallPlayerFragment(
                            message!!,
                            "alexandra"
                        )
                    findNavController().navigate(action)

                } else {
                    val action =
                        CustomCallRingingFragmentDirections.actionCustomCallRingingFragmentToCustomCallPlayerFragment(
                            message!!,
                            uri!!
                        )
                    findNavController().navigate(action)
                }

            }

        } else if (themeselected == 3) {
            binding4.profilename.text = message
            try {
                if (uri == "alexandra") {
                    Glide.with(this)
                        .load(R.drawable.ic_alex)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding4.imagecall)
                } else {
                    Glide.with(this)
                        .load(urito)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding4.imagecall)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


            binding4.endcallconst.setOnClickListener {

                // This will be called after ad is dismissed or skipped
                val homeImg = requireActivity().findViewById<ImageView>(R.id.homeImg)
//                val addpeopleimg = requireActivity().findViewById<ImageView>(R.id.addpeopleimg)
                val settingicon = requireActivity().findViewById<ImageView>(R.id.settingicon)

                homeImg.setImageResource(R.drawable.ic_homeselect)
//                addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
                settingicon.setImageResource(R.drawable.ic_settingunsele)

                checkring = false
                stopRingtone()
                stopForegroundService()
                findNavController().popBackStack(R.id.mainFragment, false)


            }
            binding4.recievecallconst.setOnClickListener {
                checkring = false
                stopRingtone()

                if (uri == "alexandra") {
                    val action =
                        CustomCallRingingFragmentDirections.actionCustomCallRingingFragmentToCustomCallPlayerFragment(
                            message!!,
                            "alexandra"
                        )
                    findNavController().navigate(action)

                } else {
                    val action =
                        CustomCallRingingFragmentDirections.actionCustomCallRingingFragmentToCustomCallPlayerFragment(
                            message!!,
                            uri!!
                        )
                    findNavController().navigate(action)
                }

            }

        } else if (themeselected == 4) {
            binding5.profilename.text = message
            try {
                if (uri == "alexandra") {
                    Glide.with(this)
                        .load(R.drawable.ic_alex)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding5.imagecall)
                } else {
                    Glide.with(this)
                        .load(urito)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding5.imagecall)
                }

            } catch (e: Exception) {
                e.printStackTrace()
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
                            val homeImg = requireActivity().findViewById<ImageView>(R.id.homeImg)
//                            val addpeopleimg =
////                                requireActivity().findViewById<ImageView>(R.id.addpeopleimg)
                            val settingicon =
                                requireActivity().findViewById<ImageView>(R.id.settingicon)

                            homeImg.setImageResource(R.drawable.ic_homeselect)
//                            addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
                            settingicon.setImageResource(R.drawable.ic_settingunsele)

                            checkring = false
                            stopRingtone()
                            stopForegroundService()


                            // Navigate after ad is closed
                            findNavController().popBackStack(R.id.mainFragment, false)


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

                            if (uri == "alexandra") {
                                val action =
                                    CustomCallRingingFragmentDirections.actionCustomCallRingingFragmentToCustomCallPlayerFragment(
                                        message!!,
                                        "alexandra"
                                    )
                                findNavController().navigate(action)

                            } else {
                                val action =
                                    CustomCallRingingFragmentDirections.actionCustomCallRingingFragmentToCustomCallPlayerFragment(
                                        message!!,
                                        uri!!
                                    )
                                findNavController().navigate(action)
                            }

                        }
                        true
                    }

                    else -> false
                }
            }


        } else if (themeselected == 5) {
            binding6.profilename.text = message
            try {
                if (uri == "alexandra") {
                    Glide.with(this)
                        .load(R.drawable.ic_alex)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding6.imagecall)
                } else {
                    Glide.with(this)
                        .load(urito)
                        .apply(RequestOptions().override(200, 200))
                        .centerCrop()
                        .into(binding6.imagecall)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


            binding6.recievecallconst.setOnActiveListener(OnActiveListener {
                checkring = false
                stopRingtone()
                if (uri == "alexandra") {
                    val action =
                        CustomCallRingingFragmentDirections.actionCustomCallRingingFragmentToCustomCallPlayerFragment(
                            message!!,
                            "alexandra"
                        )
                    findNavController().navigate(action)

                } else {
                    val action =
                        CustomCallRingingFragmentDirections.actionCustomCallRingingFragmentToCustomCallPlayerFragment(
                            message!!,
                            uri!!
                        )
                    findNavController().navigate(action)
                }
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
        ringtone.play()

    }

    private fun checkModeAndAct() {
        val audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        checkring = true

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


        if (ringtone.isPlaying) {
            ringtone.stop()
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
        Log.d("TestTag", "Paused")

    }

    override fun onStop() {
        super.onStop()
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

