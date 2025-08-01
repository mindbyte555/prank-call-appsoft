package com.example.baseapp.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.baseapp.Application.MyApplication.Companion.unlockscreen
import com.example.baseapp.MainActivity.Companion.test
import com.example.baseapp.adsManager.ActionRewardAdClose
import com.example.baseapp.adsManager.AdsManager.Companion.showCollape
import com.example.baseapp.adsManager.RewardAdObjectManager
import com.example.baseapp.adsManager.RewardAdObjectManager.coin
import com.example.baseapp.adsManager.RewardAdObjectManager.mActivity
import com.example.baseapp.callsActivity.CallActivity
import com.example.baseapp.fragments.VideoCall.VideoCallFragment
import com.example.baseapp.fragments.sms.SmsFragment
import com.example.baseapp.inAppPurchases.InAppPurchases
import com.example.baseapp.tabadapter.TabAdapter
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentScedhuleBinding


class ScedhuleFragment : Fragment() {
    private lateinit var binding: FragmentScedhuleBinding
    var btnhide: RelativeLayout? = null

    val args: ScedhuleFragmentArgs by navArgs()
    var message = ""
    var number = 0
    var mobilenumber: Long = 0
    var uri = ""
    var stop=true
    var dialog=true
    var tempsms=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScedhuleBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRetainInstance(true)
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)
            bottomNavigation?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }

        test=false

        binding.scedhulebackbtn.setOnClickListener {

            val intent = Intent(requireContext(), CallActivity::class.java)
            if (!PrefUtil(requireContext()).getBool("audioactivity", false)) {
                intent.putExtra("video",true)
            }
            else{
                if(PrefUtil(requireContext()).getBool("sms", false)){
                    intent.putExtra("sms",true)
                    tempsms=true

                }


            }


                startActivity(intent)



        }



        message = args.audioprofile
        number = args.audioprofilevoice
        mobilenumber = args.audioprofilenumber

        setupTabs()

        Log.e("FROM", "number : number $number", )



        if (uri != "") {


        } else {
            if (number == 1) {
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
            }
            if (number == 4) {
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
            else if (number == 14) {

                Glide.with(this)
                    .load(R.drawable.ic_cruise)
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

        }


        binding.profilename.text = message
        binding.mobilenumber.text = mobilenumber.toString()

    }


    fun setupTabs() {

        val tabadapter = TabAdapter(childFragmentManager)






        tabadapter.addFragment(VideoCallFragment(), getString(R.string.video_call))
        tabadapter.addFragment(AudioCallFragment(), getString(R.string.audio_call))
        tabadapter.addFragment(SmsFragment(), getString(R.string.sms))




        binding.callschduleviewpager.offscreenPageLimit = 3
        binding.callschduleviewpager.adapter = tabadapter



        var sms=PrefUtil(requireContext()).getBool("sms", false)


        binding.scheduletabLayout.setupWithViewPager(binding.callschduleviewpager)
        binding.scheduletabLayout.getTabAt(0)
        binding.scheduletabLayout.getTabAt(1)
        binding.scheduletabLayout.getTabAt(2)



        if(sms){
            binding.callschduleviewpager.currentItem = 2
            PrefUtil(requireContext()).setBool("sms", false)


        }
        else{
            if (PrefUtil(requireContext()).getBool("audioactivity", false)) {
                binding.callschduleviewpager.currentItem = 1
            } else {
                binding.callschduleviewpager.currentItem = 0
            }
        }


    }


    override fun onResume() {
        super.onResume()
        btnhide = requireActivity().findViewById(R.id.bannerAdmain)
        if (PrefUtil(requireContext()).getBool("is_premium", false) || !showCollape) {
            btnhide?.visibility = View.GONE
        } else {

            coin = PrefUtil(requireContext()).getInt("coinValue", 0)
            dialog=PrefUtil(requireContext()).getBool("dialogreward",false)
            Log.e("FROM", "coin$coin : dialog $dialog", )

            if(stop&& coin<5 && dialog){
                if (unlockscreen && number in setOf(1, 8, 9,10, 11, 13, 15,17, 18)) {
                    unlockbox("resume")
                }
            }
            else{

            }
            btnhide?.visibility = View.VISIBLE


        }
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)
            bottomNavigation?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun unlockbox(from:String){
        Log.e("FROM", "unlockbox : from $from", )
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.unlock_layot, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()
        if(PrefUtil(requireContext()).getBool("is_premium", false )|| !stop){
            dialog.dismiss()
        }
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Keep background transparent
            val layoutParams = attributes
            layoutParams.dimAmount = 0.7f // Dim level (0.0 = no dim, 1.0 = full dim)
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            attributes = layoutParams
        }// Optional: transparent background
        val crossImg = dialogView.findViewById<ImageView>(R.id.crossreward_img)
        crossImg.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(requireContext(), CallActivity::class.java)
            if( tempsms){
                intent.putExtra("sms",true)
                tempsms=false

            }
            else{
                if (!PrefUtil(requireContext()).getBool("audioactivity", false)) {
                    intent.putExtra("video",true)
                }
            }



            startActivity(intent)

        }
        val rewardad = dialogView.findViewById<ConstraintLayout>(R.id.rewardconst)
        val upgradeconst = dialogView.findViewById<ConstraintLayout>(R.id.upgradeconst)
        rewardad.setOnClickListener {
            dialog.dismiss()
            RewardAdObjectManager.requestRewardedAd(
                context = requireContext(),
                activity = requireActivity(),
                showSuccessEvent = "schedule_counter",
                actionOnAdClosedListenersm = object : ActionRewardAdClose {
                    override fun ActionAfterAd() {
                        PrefUtil(requireContext()).setBool("unlocked",true)
                        stop=false
                        dialog.dismiss()
                        PrefUtil(requireContext()).setBool("dialogreward",false)

                    }
                }
            )
        }
        upgradeconst.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(requireContext(), InAppPurchases::class.java)
            startActivity(intent)
        }
        dialog.show()
    }




}