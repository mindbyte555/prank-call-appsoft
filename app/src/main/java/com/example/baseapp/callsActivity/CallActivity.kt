package com.example.baseapp.callsActivity

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.baseapp.Application.MyApplication
import com.example.baseapp.Application.MyApplication.Companion.nativeEnabled
import com.example.baseapp.MainActivity
import com.example.baseapp.MainActivity.Companion.test
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.adsManager.AdsManager.Companion.showNative
import com.example.baseapp.adsManager.RewardAdObjectManager.coin
import com.example.baseapp.adsManager.RewardAdObjectManager.mActivity
import com.example.baseapp.utils.Constants.dialogbox
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivityCallBinding
import com.example.iwidgets.Utils.AppInfoUtils

class CallActivity : AppCompatActivity() {
    lateinit var binding: ActivityCallBinding
    private lateinit var adapter: CallAdapter
    lateinit var items:List<CallItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var audio=intent.getBooleanExtra("audio",false)
        var video=intent.getBooleanExtra("video",false)
        var sms=intent.getBooleanExtra("sms",false)
        test =true

        if(sms)
        {
            PrefUtil(this).setBool("sms",true)
            binding.textView8.text = getString(R.string.sms)

        }
        else
        {
            if(audio)
            {
                PrefUtil(this).setBool("audioactivity",true)
                binding.textView8.text = getString(R.string.audio_fake_call)

            }
            else if(video)
            {
                PrefUtil(this).setBool("audioactivity",false)
                binding.textView8.text = getString(R.string.video_fake_call)
            }
        }

        if (PrefUtil(this).getBool("is_premium", false) || !MyApplication.isEnabled || !nativeEnabled || !showNative) {
            binding.musicDetailsShimmer.visibility=View.GONE

            binding.layoutNative.setBackgroundColor(
                ContextCompat.getColor(
                    this@CallActivity, R.color.white
                )
            )
            binding.musicDetailsShimmer.hideShimmer()
            binding.musicDetailsShimmer.stopShimmer()
        }
        else{
            AdsManager.loadNative(binding.layoutNative!!,
                this@CallActivity,
                object : AdsManager.AdmobBannerAdListener {
                    override fun onAdFailed() {
                        binding.layoutNative.setBackgroundColor(
                            ContextCompat.getColor(
                                this@CallActivity, R.color.white
                            )
                        )
                        binding.musicDetailsShimmer.hideShimmer()
                        binding.musicDetailsShimmer.stopShimmer()
                    }

                    override fun onAdLoaded() {
                        binding.layoutNative.setBackgroundColor(
                            ContextCompat.getColor(
                                this@CallActivity, R.color.white
                            )
                        )
                        binding.musicDetailsShimmer.hideShimmer()
                        binding.musicDetailsShimmer.stopShimmer()
                    }
                },AdsManager.NativeAdType.MEDIUM

            )
        }

        try {
            val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
                val window = this.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.white)
                window.navigationBarColor = resources.getColor(R.color.white, theme)

                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                    isAppearanceLightNavigationBars = true
                }
            } else {
                val window = this.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.white)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                window.navigationBarColor = resources.getColor(R.color.white, theme)

                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = true
                }
            }

        } catch (e: Exception) {
        }


        var isConnected = AppInfoUtils(this).isInternetAvailable()

        if(!isConnected){
            dialogbox(this)

        }
        coin = PrefUtil(this).getInt("coinValue", 0)


        if(PrefUtil(this).getBool("is_premium", false )||coin>=5){
            items = listOf(
                CallItem(
                    "Alexandra", "+19283764525" ,

                    R.drawable.ic_alex
                ),
                CallItem(
                    "Lionel Messi","+19827346518" ,

                    R.drawable.ic_messi
                ),
                CallItem(
                    "Emma Watson","+12365478521" ,

                    R.drawable.ic_emma
                ),
                CallItem(
                    "Ronaldo","+96321458214" ,

                    R.drawable.ic_ron
                ),
                CallItem(
                    "Angeline Jolie", "+25412182129" ,

                    R.drawable.ic_ang
                ),
                CallItem(
                    "Priyanka Chopra","+98542135201" ,

                    R.drawable.ic_pry
                ),
                CallItem(
                    "Tom Holland","+12365421852" ,

                    R.drawable.ic_tom
                ),
                CallItem(
                    "Keanu Reeves","+12561515654" ,

                    R.drawable.ic_keenu
                ),

                CallItem(
                    "Ana de Armas","+165653465" ,

                    R.drawable.ic_ana
                ),
                CallItem(
                    "Alina Boz","+12521654164" ,

                    R.drawable.ic_alina
                ),
                CallItem(
                    "Song Joong-Ki","+12563846431" ,

                    R.drawable.ic_song
                ),
                CallItem(
                    "Johnny Depp","+12563846431" ,

                    R.drawable.ic_depp
                ),
                CallItem(
                    "Scarlett Johansson","+12563846431" ,

                    R.drawable.ic_scarr
                ),
                CallItem(
                    "Tom Cruise","+1286654685" ,

                    R.drawable.ic_cruise
                ),
                CallItem(
                    "Lee Min Ho","+1286654685" ,

                    R.drawable.ic_lee
                ),  CallItem(
                    "Charlize Theron","+121561435323" ,

                    R.drawable.ic_charlie
                ),
                CallItem(
                    "Donald J. Trump","+646546489" ,

                    R.drawable.ic_trump
                ),
                CallItem(
                    "Narendra Modi","+91265454635" ,

                    R.drawable.ic_modi
                )



            )

        }
        else{
            items = listOf(
                CallItem(
                    "Alexandra", "+19283764525" ,

                    R.drawable.ic_alex_pro
                ),
                CallItem(
                    "Lionel Messi","+19827346518" ,

                    R.drawable.ic_messi
                ),
                CallItem(
                    "Emma Watson","+12365478521" ,

                    R.drawable.ic_emma
                ),
                CallItem(
                    "Ronaldo","+96321458214" ,

                    R.drawable.ic_ron
                ),
                CallItem(
                    "Angeline Jolie", "+25412182129" ,

                    R.drawable.ic_ang
                ),
                CallItem(
                    "Priyanka Chopra","+98542135201" ,

                    R.drawable.ic_pry
                ),
                CallItem(
                    "Tom Holland","+12365421852" ,

                    R.drawable.ic_tom
                ),
                CallItem(
                    "Keanu Reeves","+12561515654" ,

                    R.drawable.ic_keenu_pro
                ),

                CallItem(
                    "Ana de Armas","+165653465" ,

                    R.drawable.ic_ana_pro
                ),
                CallItem(
                    "Alina Boz","+12521654164" ,

                    R.drawable.ic_alina_pro
                ),
                CallItem(
                    "Song Joong-Ki","+12563846431" ,

                    R.drawable.ic_songs_pro
                ),
                CallItem(
                    "Johnny Depp","+12563846431" ,

                    R.drawable.ic_depp
                ),
                CallItem(
                    "Scarlett Johansson","+12563846431" ,

                    R.drawable.ic_scar_pro
                ),
                CallItem(
                    "Tom Cruise","+1286654685" ,

                    R.drawable.ic_cruise
                ),
                CallItem(
                    "Lee Min Ho","+1286654685" ,

                    R.drawable.ic_lee_pro
                ),  CallItem(
                    "Charlize Theron","+121561435323" ,

                    R.drawable.ic_charlie
                ),
                CallItem(
                    "Donald J. Trump","+646546489" ,

                    R.drawable.ic_trump_pro
                ),
                CallItem(
                    "Narendra Modi","+91265454635" ,

                    R.drawable.ic_modi_pro
                )



            )

        }

        adapter = CallAdapter(items) {
            PrefUtil(this).setBool("dialogreward",true)

            if (it == 0) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Alexandra")
                intent.putExtra("profilepic", 1)
                intent.putExtra("profilenumber", +12561515654)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            } else if (it == 1) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Messi")
                intent.putExtra("profilepic", 2)
                intent.putExtra("profilenumber", +15515619654)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()

            } else if (it == 2) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Emma")
                intent.putExtra("profilepic", 3)
                intent.putExtra("profilenumber", +18563123654)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            } else if (it == 3) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Ronaldo")
                intent.putExtra("profilepic", 4)
                intent.putExtra("profilenumber", +16563636254)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            }
            else if (it == 4) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Angelina")
                intent.putExtra("profilepic", 5)
                intent.putExtra("profilenumber", +85213689654)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()

            } else if (it == 5) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Priyanka")
                intent.putExtra("profilepic", 6)
                intent.putExtra("profilenumber", +65213685354)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            } else if (it == 6) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Holland")
                intent.putExtra("profilepic", 7)
                intent.putExtra("profilenumber", +365214889654)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            }
            else if (it == 7) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Keanu")
                intent.putExtra("profilepic", 8)
                intent.putExtra("profilenumber", +366521489654)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            }
            else if (it == 8) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Ana de Armas")
                intent.putExtra("profilepic", 9)
                intent.putExtra("profilenumber", +165653465)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            }
            else if (it == 9) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Alina Boz")
                intent.putExtra("profilepic", 10)
                intent.putExtra("profilenumber", +12521654164)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            }
            else if (it == 10) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Song Joong-Ki")
                intent.putExtra("profilepic", 11)
                intent.putExtra("profilenumber", +12563846431)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            }
            else if (it == 11) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Johnny Depp")
                intent.putExtra("profilepic", 12)
                intent.putExtra("profilenumber", +12563846431)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            }
            else if (it == 12) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Scarlett Johansson")
                intent.putExtra("profilepic", 13)
                intent.putExtra("profilenumber", +1289653864)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            }
            else if (it == 13) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Tom Cruise")
                intent.putExtra("profilepic", 14)
                intent.putExtra("profilenumber", +1286654685)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            }
            else if (it == 14) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Lee Min Ho")
                intent.putExtra("profilepic", 15)
                intent.putExtra("profilenumber", +12866543248)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            }
            else if (it == 15) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Charlize Theron")
                intent.putExtra("profilepic", 16)
                intent.putExtra("profilenumber", +121561435323)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            }
            else if (it == 16) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Donald J. Trump")
                intent.putExtra("profilepic", 17)
                intent.putExtra("profilenumber", +91265454635)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            }
            else if (it == 17) {
                val intent = Intent(this, MainActivity::class.java)
                intent.action = "Call1"
                intent.putExtra("profilecall", "Narendra Modi")
                intent.putExtra("profilepic", 18)
                intent.putExtra("profilenumber", +91265454635)
                intent.putExtra("fromCallActivity", true)
                startActivity(intent)
                finish()
            }


        }
        val layoutManager = GridLayoutManager(this, 2)
        binding.callrecycle.layoutManager = layoutManager
        binding.callrecycle.adapter = adapter
        binding.fackechatbackbtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.action = "exit"
            startActivity(intent)
            finish()
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.action = "exit"
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        val coin = PrefUtil(this).getInt("coin", 0) // or however you store coins

        if (coin > 5) {
            // Let's say you want to update item at index 0 and 4
            items[0].imageResourceId = R.drawable.ic_alex_pro
            items[7].imageResourceId = R.drawable.ic_keenu_pro
            items[8].imageResourceId = R.drawable.ic_ana_pro
            items[9].imageResourceId = R.drawable.ic_alina_pro
            items[10].imageResourceId = R.drawable.ic_songs_pro
            items[12].imageResourceId = R.drawable.ic_scar_pro
            items[14].imageResourceId = R.drawable.ic_lee_pro
            items[16].imageResourceId = R.drawable.ic_trump_pro
            items[17].imageResourceId = R.drawable.ic_modi_pro

            adapter.notifyItemChanged(0)
            adapter.notifyItemChanged(7)
            adapter.notifyItemChanged(8)
            adapter.notifyItemChanged(9)
            adapter.notifyItemChanged(10)
            adapter.notifyItemChanged(12)
            adapter.notifyItemChanged(14)
            adapter.notifyItemChanged(16)
            adapter.notifyItemChanged(17)







    }
    }

}