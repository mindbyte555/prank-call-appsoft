package com.example.baseapp.chatFragtoActivity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.baseapp.Application.MyApplication.Companion.bannerCollapsible
import com.example.baseapp.Application.MyApplication.Companion.bannerEnabled
import com.example.baseapp.Application.MyApplication.Companion.clickWithDebounce
import com.example.baseapp.Application.MyApplication.Companion.isEnabled
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.adsManager.RewardAdObjectManager.coin
import com.example.baseapp.alarmManagers.AlarmReceiver
import com.example.baseapp.alarmManagers.VideoAlarmReciver
import com.example.baseapp.callsActivity.CallActivity
import com.example.baseapp.chatActivity.ChatActivity
import com.example.baseapp.fragments.chat.answer.AnswerAdapter
import com.example.baseapp.fragments.chat.question.QuestionAdapter
import com.example.baseapp.secondPermission.SecondPermissionActivity
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivityChatMainBinding
import com.google.android.gms.ads.AdView
import java.util.concurrent.atomic.AtomicBoolean

class ChatMainActivity : AppCompatActivity() {
    private var isActionPerformed = false
    private lateinit var binding: ActivityChatMainBinding
    private val answers: MutableList<String> = mutableListOf()
    private val questionchat: MutableList<String> = mutableListOf()
    var message = ""
    var number = 0
    val questions = listOf(
        "What are your hobbies and interests?",
        "Have you traveled to any interesting\nplaces recently?",
        " Do you have any pets?",
        "What's your favorite outdoor activity?"
    )
    var sms=0
    var unlcoked=false
    lateinit var answerAdapter: AnswerAdapter
    var adView: AdView? = null
    var isConnected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatMainBinding.inflate(layoutInflater)
        unlcoked=PrefUtil(this).getBool("unlocked",false)

        setContentView(binding.root)
        message = intent.getStringExtra("msg").toString()
        number = intent.getIntExtra("number", 0)
        binding.profilename.text = message


        if (isEnabled && bannerEnabled) {
            binding.bannerAd.visibility = View.VISIBLE

            if (bannerCollapsible) {
                adView = AdsManager.loadCollapsible(
                    binding.bannerAd, this@ChatMainActivity,
                    object : AdsManager.AdmobBannerAdListener {
                        override fun onAdFailed() {
                            Log.e("TAG", "onAdFailed: Collapsible")
                            binding.bannerAd.setBackgroundColor(
                                ContextCompat.getColor(this@ChatMainActivity, R.color.trans)
                            )
                            binding.shimmer.setBackgroundResource(R.color.trans)
                            binding.shimmer.stopShimmer()
                            binding.shimmer.hideShimmer()
                        }

                        override fun onAdLoaded() {
                            Log.e("TAG", "onAdLoaded: Collapsible")
                            binding.bannerAd.setBackgroundColor(
                                ContextCompat.getColor(this@ChatMainActivity, R.color.trans)
                            )
                            binding.shimmer.setBackgroundResource(R.color.trans)
                            binding.shimmer.stopShimmer()
                            binding.shimmer.hideShimmer()
                        }
                    }
                )
            } else {
                AdsManager.loadAdaptiveBannerAd(
                    binding.bannerAd, this@ChatMainActivity,
                    object : AdsManager.AdmobBannerAdListener {
                        override fun onAdFailed() {
                            Log.e("TAG", "onAdFailed: Normal Banner")
                            binding.bannerAd.setBackgroundColor(
                                ContextCompat.getColor(this@ChatMainActivity, R.color.trans)
                            )
                            binding.shimmer.setBackgroundResource(R.color.trans)
                            binding.shimmer.stopShimmer()
                            binding.shimmer.hideShimmer()
                        }

                        override fun onAdLoaded() {
                            Log.e("TAG", "onAdLoaded: Normal Banner")
                            binding.bannerAd.setBackgroundColor(
                                ContextCompat.getColor(this@ChatMainActivity, R.color.trans)
                            )
                            binding.shimmer.setBackgroundResource(R.color.trans)
                            binding.shimmer.stopShimmer()
                            binding.shimmer.hideShimmer()
                        }
                    }
                ).let {
                    adView = it
                }
            }

        } else {
            // Hide ad view if neither is enabled
            binding.bannerAd.visibility = View.GONE
            binding.shimmer.stopShimmer()
            binding.shimmer.hideShimmer()
        }
        sms=PrefUtil(this).getInt("smstheme",0)

        if(sms==0){
            binding.callimg.visibility=View.VISIBLE
            binding.videoimg.visibility=View.VISIBLE
            binding.mainbg.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.callimg.setImageResource(R.drawable.ic_call_icon)
            binding.videoimg.setImageResource(R.drawable.ic_video_icon)
            binding.line.setImageResource(R.drawable.line_background)
            binding.backbtnchat.setImageResource(R.drawable.ic_back_black)
            binding.profilename.setTextColor(ContextCompat.getColor(this, R.color.chat_text))
            binding.onlinename.setTextColor(ContextCompat.getColor(this, R.color.chat_text))
        }
        else if(sms==1){
            binding.callimg.visibility=View.VISIBLE
            binding.videoimg.visibility=View.VISIBLE
            binding.mainbg.setBackgroundResource(R.drawable.ic_whatbg)
            binding.constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.whastapp_bg))
            binding.callimg.setImageResource(R.drawable.ic_callwht)
            binding.videoimg.setImageResource(R.drawable.ic_vdowht)
            binding.line.setImageResource(R.drawable.ic_whtline)
            binding.backbtnchat.setImageResource(R.drawable.white_back)
            binding.profilename.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.onlinename.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
        else if(sms==2)
        {
            binding.callimg.visibility=View.VISIBLE
            binding.videoimg.visibility=View.VISIBLE
            binding.mainbg.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.constraintLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.white))
            binding.callimg.setImageResource(R.drawable.ic_callblue)
            binding.videoimg.setImageResource(R.drawable.ic_videoblue)
            binding.line.setImageResource(R.drawable.line_background)
            binding.backbtnchat.setImageResource(R.drawable.back_blue)
            binding.profilename.setTextColor(ContextCompat.getColor(this, R.color.scedhuleing_text))
            binding.onlinename.setTextColor(ContextCompat.getColor(this, R.color.scedhuleing_text))
        }
        else if(sms==3){
            binding.mainbg.setBackgroundResource(R.drawable.tele_bg)
            binding.constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.telegram_const))
            binding.callimg.visibility=View.GONE
            binding.videoimg.visibility=View.GONE
            binding.line.setImageResource(R.drawable.ic_whtline)
            binding.backbtnchat.setImageResource(R.drawable.white_back)
            binding.profilename.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.onlinename.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
        else if(sms==4){
            binding.callimg.visibility=View.VISIBLE
            binding.videoimg.visibility=View.VISIBLE
            binding.mainbg.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

            binding.callimg.setImageResource(R.drawable.ic_callblack)
            binding.videoimg.setImageResource(R.drawable.ic_vdoblack)

            binding.line.setImageResource(R.drawable.line_background)
            binding.backbtnchat.setImageResource(R.drawable.ic_back_black)
            binding.imagechat.borderColor = Color.parseColor("#B609A0")

            binding.profilename.setTextColor(ContextCompat.getColor(this, R.color.scedhuleing_text))
            binding.onlinename.setTextColor(ContextCompat.getColor(this, R.color.insta_sec))
        }


        Handler(Looper.getMainLooper()).postDelayed(
            {

                binding.chatrecycler.visibility = View.VISIBLE
                binding.questionlistrecycler.visibility = View.VISIBLE
                binding.waitprogress.visibility = View.GONE
                binding.waittext.visibility = View.GONE


            }, 3000
        )
        if (number in setOf(1, 8, 9, 11, 13, 17, 18)) {
            if(!unlcoked){
                coin = PrefUtil(this).getInt("coinValue", 0)
                coin = coin - 5
                PrefUtil(this).setInt("coinValue", coin)

            }
            else{
                PrefUtil(this).setBool("unlocked",false)

            }
        }
        val imageMap = mapOf(
            1 to R.drawable.ic_alex,
            2 to R.drawable.ic_messi,
            3 to R.drawable.ic_emma,
            4 to R.drawable.ic_ron,
            5 to R.drawable.ic_ang,
            6 to R.drawable.ic_pry,
            7 to R.drawable.ic_tom,
            8 to R.drawable.ic_keenu,
            9 to R.drawable.ic_ana,
            10 to R.drawable.ic_alina,
            11 to R.drawable.ic_song,
            12 to R.drawable.ic_depp,
            13 to R.drawable.ic_scarr,
            14 to R.drawable.ic_cruise,
            15 to R.drawable.ic_lee,
            16 to R.drawable.ic_charlie,
            17 to R.drawable.ic_trump,
            18 to R.drawable.ic_modi
        )
        Log.d("Images","number$number")

        if (number == -1) {
            val img = PrefUtil(this).getString("profile_img").toString()
            val uri = img.toUri()

            Glide.with(this)
                .load(if (img.isNotBlank()) uri else R.drawable.ic_alex)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagechat)

        } else {

            val drawableRes = imageMap[number] ?: R.drawable.ic_alex
            val drawableName = resources.getResourceEntryName(drawableRes)
            Log.d("Images", "Drawable name for number $number: $drawableName")
            Glide.with(this)
                .load(drawableRes)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagechat)
        }



        binding.backbtnchat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)


        }

        if (answers.isEmpty()) {
            var check=PrefUtil(this).getString("reply")?:""
            if(check!="" && check!="null"){
                answers.add(check)

            }
            else{
                answers.add("Hi")

            }
        }
        answerAdapter = AnswerAdapter(answers, questionchat)
        binding.questionlistrecycler.layoutManager = LinearLayoutManager(this)
        binding.chatrecycler.layoutManager = LinearLayoutManager(this)
        binding.questionlistrecycler.layoutManager = LinearLayoutManager(this)
        val adapter = QuestionAdapter(questions) { question, it ->


            questionchat.add(question)
            if (it == 0) {
                answers.add("I enjoy reading books, hiking in nature, and\ntrying out new recipes in the kitchen.")
            } else if (it == 1) {
                answers.add("Yes, I recently visited Japan and\nwas fascinated by the rich culture\n,delicious food, and stunning landscapes")
            } else if (it == 2) {
                answers.add("Yes, I have a dog named Max.")
            } else if (it == 3) {
                answers.add("Hiking in the mountains.")
            }


            answerAdapter.notifyDataSetChanged()
        }

        binding.chatrecycler.adapter = answerAdapter
        binding.questionlistrecycler.adapter = adapter
        binding.callimg.clickWithDebounce {
            if (!isActionPerformed) {
                isActionPerformed = true
                if (number in -1..17) {
                    PrefUtil(this).setString("profile", message)
                    PrefUtil(this).setInt("profile_int", number)
                    sendBroadcast(Intent(this, AlarmReceiver::class.java))
                    finish()
                }
                Handler().postDelayed({
                    isActionPerformed = false
                }, 2000)
            } else {
                return@clickWithDebounce
            }
        }
        binding.videoimg.clickWithDebounce {
            if (!isActionPerformed) {
                isActionPerformed = true
                if (!checkCameraPermission()) {
                    PrefUtil(this).setString("profile", message)
                    PrefUtil(this).setInt("profile_int", number)
                    val intent = Intent(this, SecondPermissionActivity::class.java)
                    intent.putExtra("key", "chat")
                    startActivity(intent)
                    finish()

                } else {
                    if (number in -1..17) {
                        PrefUtil(this).setString("profile", message)
                        PrefUtil(this).setInt("profile_int", number)
                        sendBroadcast(Intent(this, VideoAlarmReciver::class.java))
                        finish()
                    }
                }
                Handler().postDelayed({
                    isActionPerformed = false
                }, 2000)
            } else {
                return@clickWithDebounce
            }
        }
    }




    override fun onResume() {
        super.onResume()
        Log.d("TestTag", "OnResume of chat")

        if (PrefUtil(this@ChatMainActivity).getBool("is_premium", false)) {
            binding.bannerAd.visibility = View.GONE
        } else {
//            adView?.let { it.resume() }

        }
    }






    private fun checkCameraPermission(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        )
        return cameraPermission == PackageManager.PERMISSION_GRANTED
    }
}