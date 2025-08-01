package com.example.baseapp.fragments

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
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
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.baseapp.Application.MyApplication.Companion.review
import com.example.baseapp.Application.RingingForegroundService
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.adsManager.RewardAdObjectManager.coin
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentAudioPlayerBinding


class AudioPlayerFragment : Fragment() {
    private lateinit var binding: FragmentAudioPlayerBinding
    var message=""
    var number=0
    private var currentSeconds = 0
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = true
    lateinit var audioManager: AudioManager
    private var isMuted = false
    private var isHold = false
    var btnhide: RelativeLayout? = null
    private var isSpeaker = false
    private var currentVolume = 0
    val handler = Handler()
    lateinit var runnable : Runnable
    val args: AudioPlayerFragmentArgs by navArgs()
    private var playbackPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setRetainInstance(true)
        btnhide = requireActivity().findViewById(R.id.bannerAdmain)
        btnhide?.visibility=View.VISIBLE
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
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)


        audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        mediaPlayer = MediaPlayer()

        runnable = object : Runnable {
            override fun run() {
                if (isPlaying) {
                    currentSeconds++
                    val formattedTime = String.format("%02d:%02d", currentSeconds / 60, currentSeconds % 60)
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
        Log.d("Test",number.toString())
        var unlcoked=PrefUtil(requireContext()).getBool("unlocked",false)

        binding.audiotext.text=message
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
            review =true
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

//            stopAndReleaseMediaPlayer()


                }
            })



        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
        message = args.profilenamerimg1
        number = args.profiilenorimg1
        binding.audiotext.text=message
        if(number==-1){
            var img= PrefUtil(requireContext()).getString("profile_img").toString()
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

        }
        else if (number == 2) {
            Glide.with(this)
                .load(R.drawable.ic_messi)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagecall)

        }
        else if (number == 3) {
            Glide.with(this)
                .load(R.drawable.ic_emma)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagecall)

        }
        else if (number == 4) {
            Glide.with(this)
                .load(R.drawable.ic_ron)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagecall)

        }
        else if (number == 5) {
            Glide.with(this)
                .load(R.drawable.ic_ang)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagecall)

        }
        else if (number == 6) {
            Glide.with(this)
                .load(R.drawable.ic_pry)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagecall)

        }
        else if (number == 7) {
            Glide.with(this)
                .load(R.drawable.ic_tom)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagecall)

        }
        else if (number == 8) {
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
        initMediaPlayer()
    }
    private fun initMediaPlayer() {
        val audioResource = when (number) {
            1 -> R.raw.audio_1
            2 -> R.raw.audio_2
            3->R.raw.audio_3
            4->R.raw.audio_4
            5 -> R.raw.audio_5
            6 -> R.raw.audio_6
            7->R.raw.audio_7
            8->R.raw.audio_8
            9 -> R.raw.audio_9
            10 -> R.raw.audio_10
            11->R.raw.audio_11
            12->R.raw.audio_12
            13 -> R.raw.audio_13
            14 -> R.raw.audio_14
            15->R.raw.audio_15
            16->R.raw.audio_16
            17->R.raw.audio_17
            18->R.raw.audio_18

            else -> R.raw.audio_1
        }

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
            binding.mutebgaudio.visibility=View.VISIBLE

            isMuted = false
        } else {

            binding.mutebgaudio.visibility=View.GONE

            isMuted = true
        }
        if(isMuted){
            binding.mutebgaudio.visibility=View.VISIBLE

        }
        else{
            binding.mutebgaudio.visibility=View.GONE

        }


    }

    private fun holdAudio() {
        isHold = !isHold

        if(isHold){
            binding.holdbgaudio.visibility=View.VISIBLE
            binding.addcallaudioconst.visibility=View.INVISIBLE
            binding.recordingaudioconst.visibility=View.INVISIBLE        }
        else{
            binding.holdbgaudio.visibility=View.GONE
            binding.addcallaudioconst.visibility=View.VISIBLE
            binding.recordingaudioconst.visibility=View.VISIBLE
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

        if(isSpeaker){
            binding.speakerbgaudio.visibility=View.VISIBLE
        }
        else{
            binding.speakerbgaudio.visibility=View.GONE

        }

    }


    override fun onStop() {
        super.onStop()
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)


            bottomNavigation?.visibility = View.GONE
        }catch (e : Exception){
            e.printStackTrace()
        }
        handler.removeCallbacks(runnable)
//        currentSeconds = 0
        stopAndReleaseMediaPlayer()

    }


override fun onResume() {
    super.onResume()
    try {
        val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)
        bottomNavigation?.visibility = View.GONE

    }catch (e : Exception){
        e.printStackTrace()
    }
    initMediaPlayer() // Make sure this doesn't auto-play the media

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