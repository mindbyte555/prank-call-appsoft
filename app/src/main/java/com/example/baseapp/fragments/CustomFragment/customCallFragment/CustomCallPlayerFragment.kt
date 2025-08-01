package com.example.baseapp.fragments.CustomFragment.customCallFragment

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.baseapp.adsManager.AdsManager
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentCustomCallPlayerBinding
import com.google.android.gms.ads.interstitial.InterstitialAd


class CustomCallPlayerFragment : Fragment() {
    private lateinit var binding: FragmentCustomCallPlayerBinding
    var message = ""
    var number = ""
    var btnhide: RelativeLayout? = null
    private var currentSeconds = 0
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = true
    lateinit var audioManager: AudioManager
    private var isMuted = false
    private var isHold = false

    private var isSpeaker = false
    private var currentVolume = 0
    val handler = Handler()
    lateinit var runnable: Runnable
    val args: CustomCallPlayerFragmentArgs by navArgs()
    private var playbackPosition = 0
    private var interstitialAd: InterstitialAd? = null
    private var isAdLoading = false
    private var shouldEndCallAfterAd = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btnhide = requireActivity().findViewById(R.id.bannerAdmain)
        btnhide?.visibility = View.GONE
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)


            bottomNavigation?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setRetainInstance(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomCallPlayerBinding.inflate(inflater, container, false)
        audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        mediaPlayer = MediaPlayer()

        runnable = object : Runnable {
            override fun run() {
                if (isPlaying) {
                    currentSeconds++
                    val formattedTime =
                        String.format("%02d:%02d", currentSeconds / 60, currentSeconds % 60)
                    binding.Timertext.text = formattedTime
                    handler.postDelayed(this, 1000) // Schedule the next increment
                }
            }
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                stopAndReleaseMediaPlayer()
                if (activity?.supportFragmentManager?.backStackEntryCount ?: 0 > 0) {
                    activity?.supportFragmentManager?.popBackStack()
                } else {
                    activity?.finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        handler.post(runnable)
        Log.d("Test", number.toString())
        binding.audiotext.text = message
        binding.holdaudioconst.setOnClickListener {
            holdAudio()
        }

        binding.muteaudioconst.setOnClickListener {
            muteAudio()
        }

        binding.speakeraudioconst.setOnClickListener {
            toggleSpeaker()
        }

        binding.endaudiocallconst.setOnClickListener {
            Log.d("iaminins", "onViewCreated: Ad is called")

            findNavController().popBackStack(R.id.mainFragment, false)

            AdsManager.showInterstitial(true, requireActivity(), object :
                AdsManager.InterstitialAdListener {
                override fun onAdClosed() {
//            stopAndReleaseMediaPlayer()
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
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            })
        message = args.customcallprofile
        number = args.customcalluri
        var urinum = number.toUri()
        binding.audiotext.text = message

        try {

            if (number == "alexandra") {
                Glide.with(this)
                    .load(R.drawable.ic_alex)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)
            } else {
                Glide.with(this)
                    .load(urinum)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        initMediaPlayer()
    }

    private fun initMediaPlayer() {
        val audioResource = R.raw.audio_1

        audioResource?.let {
            mediaPlayer?.reset()
            mediaPlayer = MediaPlayer.create(context, it).apply {
                isLooping = true
                setOnPreparedListener {
                    start()
                }
            }
        }
    }

    fun stopAndReleaseMediaPlayer() {
        mediaPlayer?.let {
            try {
                if (it.isPlaying) {
                    playbackPosition = it.currentPosition

                    it.stop()
                }
                it.release() // Release resources
            } catch (e: Exception) {
                Log.e("MediaPlayerError", "Error stopping or releasing media player", e)
            } finally {
                mediaPlayer = null
            }
        }
    }

    private fun togglePlayPause() {
        if (isPlaying) {
            pauseMediaPlayer()
        } else {
            resumeMediaPlayer()
        }
    }

    private fun pauseMediaPlayer() {
        isPlaying = false
    }

    private fun resumeMediaPlayer() {
        isPlaying = true
    }


    private fun muteAudio() {
        if (isMuted) {
            binding.mutebgaudio.visibility = View.VISIBLE

            isMuted = false
        } else {

            binding.mutebgaudio.visibility = View.GONE

            isMuted = true
        }
        if (isMuted) {
            binding.mutebgaudio.visibility = View.VISIBLE

        } else {
            binding.mutebgaudio.visibility = View.GONE

        }


    }

    private fun holdAudio() {
        isHold = !isHold

        if (isHold) {
            binding.holdbgaudio.visibility = View.VISIBLE
            binding.addcallaudioconst.visibility = View.INVISIBLE
            binding.recordingaudioconst.visibility = View.INVISIBLE
        } else {
            binding.holdbgaudio.visibility = View.GONE
            binding.addcallaudioconst.visibility = View.VISIBLE
            binding.recordingaudioconst.visibility = View.VISIBLE
        }


    }

    private fun toggleSpeaker() {
        if (isSpeaker) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0)
            isSpeaker = false
        } else {
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0
            )
            isSpeaker = true
        }

        if (isSpeaker) {
            binding.speakerbgaudio.visibility = View.VISIBLE
        } else {
            binding.speakerbgaudio.visibility = View.GONE

        }

    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
//        currentSeconds = 0
        stopAndReleaseMediaPlayer()

    }


    override fun onResume() {
        super.onResume()
        initMediaPlayer() // Make sure this doesn't auto-play the media
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)


            bottomNavigation?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaPlayer?.let {
            it.seekTo(playbackPosition)
            if (isPlaying) {
                it.start()

            }
            handler.removeCallbacks(runnable) // Remove any existing callbacks to avoid duplicates
            handler.postDelayed(runnable, 1000)

        }
    }


}