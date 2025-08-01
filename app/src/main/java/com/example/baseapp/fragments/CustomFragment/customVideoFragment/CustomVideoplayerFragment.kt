package com.example.baseapp.fragments.CustomFragment.customVideoFragment

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.OnBackPressedCallback
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.baseapp.adsManager.AdsManager
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentCustomVideoplayerBinding


class CustomVideoplayerFragment : Fragment() {
    private lateinit var binding: FragmentCustomVideoplayerBinding
    var message=""
    var number=0
    private lateinit var videoView: VideoView
    private lateinit var cameraProvider: ProcessCameraProvider
    private var lensFacing = CameraSelector.DEFAULT_FRONT_CAMERA
    private var currentSeconds = 0
    private var isPlaying: Boolean = true
    lateinit var audioManager: AudioManager
    private var isMuted = false
    private var isHold = false
    private var btnhide: RelativeLayout? = null
    private var isSpeaker = false
    private var currentVolume = 0
    val handler = Handler()
    lateinit var runnable : Runnable
    val args: CustomVideoplayerFragmentArgs by navArgs()
    private var playbackPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btnhide=requireActivity().findViewById(R.id.bannerAdmain)
        btnhide?.visibility=View.GONE
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)


            bottomNavigation?.visibility = View.GONE
        }catch (e : Exception){
            e.printStackTrace()
        }
        setRetainInstance(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomVideoplayerBinding.inflate(inflater, container, false)

        audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            })

        runnable = object : Runnable {
            override fun run() {
                if (isPlaying) {
                    currentSeconds++
                    val formattedTime = String.format("%02d:%02d", currentSeconds / 60, currentSeconds % 60)
                    binding.videotimename.text = formattedTime
                    handler.postDelayed(this, 1000)
                }
            }
        }
        videoView = binding.customvideoview
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            mediaPlayer.start()
            isPlaying = true
        }


        startCamera()


        handler.post(runnable)
        binding.endvideocallconst.setOnClickListener {
            Log.d("iaminins", "onViewCreated: Ad is called")

            findNavController().popBackStack(R.id.mainFragment, false)

            AdsManager.showInterstitial(true, requireActivity(), object :
                AdsManager.InterstitialAdListener {
                override fun onAdClosed() {
                    val homeImg = requireActivity().findViewById<ImageView>(R.id.homeImg)
//                    val addpeopleimg = requireActivity().findViewById<ImageView>(R.id.addpeopleimg)
                    val settingicon = requireActivity().findViewById<ImageView>(R.id.settingicon)

                    homeImg.setImageResource(R.drawable.ic_homeselect)
//                    addpeopleimg.setImageResource(R.drawable.ic_addppl_grey)
                    settingicon.setImageResource(R.drawable.ic_settingunsele)
//                    findNavController().popBackStack(R.id.mainFragment, false)
                }
            })
        }
        binding.flipvideocallconst.setOnClickListener {
            flipCamera()
        }
        binding.speakervideocallconst.setOnClickListener {
            toggleSpeaker()
        }
        binding.mutevideocallconst.setOnClickListener {
            muteAudio()
        }
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        message = args.customvideoprofile

        binding.customvideocallname.text = message
        initMediaPlayer()




    }

    private fun initMediaPlayer() {
        val videoResource = when (number) {
            1 -> R.raw.video_1
            2 -> R.raw.video_2
            3 -> R.raw.video_3
            4 -> R.raw.video_4
            else -> R.raw.video_1
        }

        videoResource?.let {
            val videoPath = "android.resource://${requireActivity().packageName}/$it"
            binding.customvideoview.setVideoURI(Uri.parse(videoPath))
            binding.customvideoview.requestFocus()
        }
    }


    private fun stopMediaPlayer() {
        if (isPlaying) {
            binding.customvideoview.stopPlayback()
            isPlaying = false
        }
    }



    private fun toggleSpeaker() {
        if (isSpeaker) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0)
            isSpeaker = false
        } else {
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0)
            isSpeaker = true
        }

        // Update UI based on isSpeaker
        if (isSpeaker) {
            binding.speakertext.visibility = View.VISIBLE
        } else {
            binding.speakertext.visibility = View.GONE
        }
    }
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.createSurfaceProvider())
                }

            val cameraSelector = lensFacing

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )

            } catch (exc: Exception) {
                Log.e("TestTag", "Use case binding failed", exc)
            }
            binding.blackBackground.visibility = View.INVISIBLE
            binding.viewFinder.visibility = View.VISIBLE

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun stopCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
        }, ContextCompat.getMainExecutor(requireContext()))
    }
    private fun flipCamera() {
        lensFacing = if (lensFacing == CameraSelector.DEFAULT_FRONT_CAMERA) {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        else {
            CameraSelector.DEFAULT_FRONT_CAMERA
        }
        stopCamera()
        startCamera()
    }
    private fun muteAudio() {
        if (isMuted) {
            binding.mutetext.visibility=View.VISIBLE

            isMuted = false
        } else {

            binding.mutetext.visibility=View.GONE

            isMuted = true
        }


    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
        currentSeconds = 0
        stopMediaPlayer()
        stopCamera()
    }
    override fun onResume() {
        super.onResume()
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)


            bottomNavigation?.visibility = View.GONE
        }catch (e : Exception){
            e.printStackTrace()
        }
        initMediaPlayer()
        lensFacing=CameraSelector.DEFAULT_FRONT_CAMERA
        startCamera()
        // Seek to the last known position
        binding.customvideoview.seekTo(playbackPosition)

        if (isPlaying) {
            binding.customvideoview.start()
        }

        // Restart the timer
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 1000)
    }


}