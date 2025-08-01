package com.example.baseapp.fragments.VideoCall

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
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
import com.example.baseapp.Application.MyApplication.Companion.review
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.adsManager.RewardAdObjectManager.coin
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentVideoPlayerBinding


class VideoPlayerFragment : Fragment() {

    private lateinit var binding: FragmentVideoPlayerBinding
    var message=""
    var number=0
    private lateinit var videoView: VideoView
    private lateinit var cameraProvider: ProcessCameraProvider
    private var lensFacing =CameraSelector.DEFAULT_FRONT_CAMERA
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
    val args: VideoPlayerFragmentArgs by navArgs()
    private var playbackPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
//        btnhide=requireActivity().findViewById(R.id.bannerAdmain)
//        btnhide?.visibility=View.GONE

        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)
            bottomNavigation?.visibility = View.GONE
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)
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
        videoView = binding.videoview
        var unlcoked=PrefUtil(requireContext()).getBool("unlocked",false)

        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            mediaPlayer.start()
            isPlaying = true
        }


        startCamera()


        handler.post(runnable)
        binding.endvideocallconst.setOnClickListener {
            review =true

            Log.d("iaminins", "onViewCreated: Ad is called")
            findNavController().popBackStack(R.id.mainFragment, false)
            if(!unlcoked){
                coin = PrefUtil(requireContext()).getInt("coinValue", 0)
                var profileInt = PrefUtil(requireContext()).getInt("profile_int",0)
                if (profileInt in listOf(1, 8, 9, 10,11,13,17,18)) {
                    coin= coin-5
                    PrefUtil(requireContext()).setInt("coinValue", coin)
                }
            }
            else {
                PrefUtil(requireContext()).setBool("unlocked", false)
            }

                AdsManager.showInterstitial(true, requireActivity(), object :
                AdsManager.InterstitialAdListener {
                override fun onAdClosed() {
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
        message = args.videoprofile
        number = args.videoprofilevoice

        binding.videocallname.text = message
        initMediaPlayer()




    }
    private fun initMediaPlayer() {
        val videoResource = when (number) {
            1 -> R.raw.video_1
            2 -> R.raw.video_2
            3 -> R.raw.video_3
            4 -> R.raw.video_4
            5 -> R.raw.video_5
            6 -> R.raw.video_6
            7 -> R.raw.video_7
            8 -> R.raw.video_8
            9 -> R.raw.video_9
            10 -> R.raw.video_10
            11 -> R.raw.video_11
            12 -> R.raw.video_12
            13 -> R.raw.video_13
            14 -> R.raw.video_14
            15 -> R.raw.video_15
            16 -> R.raw.video_16
            17 -> R.raw.video_17
            18 -> R.raw.video_18
            else -> R.raw.video_1
        }

        videoResource?.let {
            val videoPath = "android.resource://${requireActivity().packageName}/$it"
            binding.videoview.setVideoURI(Uri.parse(videoPath))
            binding.videoview.requestFocus()
        }
    }


    private fun stopMediaPlayer() {
        if (isPlaying) {
            binding.videoview.stopPlayback()
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
            binding.speakerimg.setImageResource(R.drawable.ic_speaker_video)
           // binding.speakertext.visibility = View.VISIBLE
        } else {
           // binding.speakertext.visibility = View.GONE
            binding.speakerimg.setImageResource(R.drawable.mkemute)
        }
    }
    //commented for zoom
//private fun startCamera() {
//    val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
//
//    cameraProviderFuture.addListener({
//        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//        // Preview
//        val preview = Preview.Builder()
//            .build()
//            .also {
//                it.setSurfaceProvider(binding.viewFinder.createSurfaceProvider())
//            }
//
//        val cameraSelector = lensFacing
//
//        try {
//            cameraProvider.unbindAll()
//            cameraProvider.bindToLifecycle(
//                this, cameraSelector, preview
//            )
//
//        } catch (exc: Exception) {
//            Log.e("TestTag", "Use case binding failed", exc)
//        }
//        binding.blackBackground.visibility = View.INVISIBLE
//        binding.viewFinder.visibility = View.VISIBLE
//
//    }, ContextCompat.getMainExecutor(requireContext()))
//}

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
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )

                // Check zoom ratio range
                val cameraInfo = camera.cameraInfo
                val zoomState = cameraInfo.zoomState.value
                val minZoomRatio = zoomState?.minZoomRatio ?: 1.0f
                val maxZoomRatio = zoomState?.maxZoomRatio ?: 1.0f

                Log.d("TestTag", "Min Zoom Ratio: $minZoomRatio, Max Zoom Ratio: $maxZoomRatio")

                // Ensure zoom ratio is within valid range
                val requestedZoomRatio = 1.0f
                if (requestedZoomRatio in minZoomRatio..maxZoomRatio) {
                    // Set zoom ratio if needed
                    camera.cameraControl.setZoomRatio(requestedZoomRatio)
                } else {
                    Log.e("TestTag", "Requested zoomRatio $requestedZoomRatio is not within valid range [$minZoomRatio , $maxZoomRatio]")
                }

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
            binding.muteimg.setImageResource(R.drawable.ic_mute_video)
          //  binding.mutetext.visibility=View.VISIBLE

            isMuted = false
        } else {

         //   binding.mutetext.visibility=View.GONE
            binding.muteimg.setImageResource(R.drawable.speakeroff)
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
        initMediaPlayer() // Initializes the video. Ensure this doesn't auto-play.
        lensFacing=CameraSelector.DEFAULT_FRONT_CAMERA
        startCamera()
        // Seek to the last known position
        binding.videoview.seekTo(playbackPosition)

        // Optionally start playing the video if it was playing before onStop()
        if (isPlaying) {
            binding.videoview.start()
        }

        // Restart the timer
        handler.removeCallbacks(runnable) // Prevent the runnable from being posted multiple times
        handler.postDelayed(runnable, 1000) // Schedule the timer update
    }


}