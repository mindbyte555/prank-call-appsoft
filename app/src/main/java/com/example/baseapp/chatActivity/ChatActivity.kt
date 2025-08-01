package com.example.baseapp.chatActivity

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
import com.example.baseapp.MainActivity
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.adsManager.AdsManager.Companion.showNative
import com.example.baseapp.adsManager.RewardAdObjectManager.coin
import com.example.baseapp.callsActivity.CallItem
import com.example.baseapp.chatFragtoActivity.ChatMainActivity
import com.example.baseapp.utils.Constants.dialogbox
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivityChatBinding
import com.example.iwidgets.Utils.AppInfoUtils
import com.google.android.gms.ads.AdView
import java.util.concurrent.atomic.AtomicBoolean

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter
    var isConnected =false
    lateinit var items:List<ChatListItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (PrefUtil(this).getBool("is_premium", false) ||!MyApplication.isEnabled || !MyApplication.bannerRecEnabled) {
            binding.adContainer.visibility = View.GONE
        } else {
            AdsManager.loadMediumRectangleBannerAd(
                binding.rectbanner,
                this@ChatActivity,
                object : AdsManager.AdmobBannerAdListener {
                    override fun onAdLoaded() {
                        binding.rectbanner.setBackgroundColor(
                            ContextCompat.getColor(
                                this@ChatActivity, R.color.trans
                            )
                        )
                        binding.adContainer.visibility = View.VISIBLE // Make container visible after ad loads
                    }

                    override fun onAdFailed() {
                        binding.rectbanner.setBackgroundColor(
                            ContextCompat.getColor(
                                this@ChatActivity, R.color.gray1
                            )
                        )
                        binding.adContainer.visibility = View.VISIBLE // Make container visible after ad loads
                    }
                }
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
        isConnected=  AppInfoUtils(this).isInternetAvailable()



        if(!isConnected){
            dialogbox(this)

        }

        if(PrefUtil(this).getBool("is_premium", false )|| coin >=5){
            items = listOf(
                ChatListItem(
                    "Alexandra\nDaddario",

                    R.drawable.ic_chat_big
                ), ChatListItem(

                    "Lionel\nMessi",

                    R.drawable.ic_chat2_big
                ), ChatListItem(
                    "Emma\n" + "Watson",

                    R.drawable.ic_chat3_big
                ), ChatListItem(
                    "Cristiano\n" + "Ronaldo",

                    R.drawable.ic_chat4_big
                ), ChatListItem(
                    "Angelina\nJolie",

                    R.drawable.ic_chat5_big
                ), ChatListItem(
                    "Priyanka\nChopra",

                    R.drawable.ic_chat6_big
                ), ChatListItem(
                    "Tom\nHolland",

                    R.drawable.ic_chat7_big
                ), ChatListItem(
                    "Keanu\nReeves",

                    R.drawable.ic_chat8_big
                ),ChatListItem(
                    "Ana\nde Armas",

                    R.drawable.ic_chat9_big
                ),
                ChatListItem(
                    "Alina\nBoz",

                    R.drawable.ic_chat10_big
                ),  ChatListItem(
                    "Song \n" + "Joong-Ki",

                    R.drawable.ic_chat11_big
                ),
                ChatListItem(
                    "Johnny \n" +
                            "Depp",

                    R.drawable.ic_chat12_big
                ),
                ChatListItem(
                    "Scarlett \n" +
                            "Johansson",

                    R.drawable.ic_chat13_big
                ),  ChatListItem(
                    "Tom \n" +
                            "Cruise",

                    R.drawable.ic_chat14_big
                ), ChatListItem(
                    "Lee \n" +
                            "Min Ho",

                    R.drawable.ic_chat15_big
                ),
                ChatListItem(
                    "Charlize \n" +
                            "Theron",

                    R.drawable.ic_chat18_big
                ),
                ChatListItem(
                    "Donald \n" +
                            "J. Trump",

                    R.drawable.ic_chat16_big
                ),
                ChatListItem(
                    "Narendra \n" +
                            "Modi",

                    R.drawable.ic_chat17_big
                )

            )

        }
        else{
            items = listOf(
                ChatListItem(
                    "Alexandra\nDaddario",

                    R.drawable.ic_pro_alexchat
                ), ChatListItem(

                    "Lionel\nMessi",

                    R.drawable.ic_chat2_big
                ), ChatListItem(
                    "Emma\n" + "Watson",

                    R.drawable.ic_chat3_big
                ), ChatListItem(
                    "Cristiano\n" + "Ronaldo",

                    R.drawable.ic_chat4_big
                ), ChatListItem(
                    "Angelina\nJolie",

                    R.drawable.ic_chat5_big
                ), ChatListItem(
                    "Priyanka\nChopra",

                    R.drawable.ic_chat6_big
                ), ChatListItem(
                    "Tom\nHolland",

                    R.drawable.ic_chat7_big
                ), ChatListItem(
                    "Keanu\nReeves",

                    R.drawable.ic_pro_keenuchat
                )
                ,ChatListItem(
                    "Ana\nde Armas",

                    R.drawable.ic_pro_anachat
                ),
                ChatListItem(
                    "Alina\nBoz",

                    R.drawable.ic_pro_alinachat
                ),  ChatListItem(
                    "Song \n" + "Joong-Ki",

                    R.drawable.ic_pro_songchat
                ),
                ChatListItem(
                    "Johnny \n" +
                            "Depp",

                    R.drawable.ic_chat12_big
                ),
                ChatListItem(
                    "Scarlett \n" +
                            "Johansson",

                    R.drawable.ic_pro_scarrchat
                ),  ChatListItem(
                    "Tom \n" +
                            "Cruise",

                    R.drawable.ic_chat14_big
                ), ChatListItem(
                    "Lee \n" +
                            "Min Ho",

                    R.drawable.ic_pro_leechat
                ),
                ChatListItem(
                    "Charlize \n" +
                            "Theron",

                    R.drawable.ic_chat18_big
                ),
                ChatListItem(
                    "Donald \n" +
                            "J. Trump",

                    R.drawable.ic_pro_trumpchat
                ),
                ChatListItem(
                    "Narendra \n" +
                            "Modi",

                    R.drawable.ic_pro_modichat
                )

            )
        }
        adapter = ChatAdapter(items) {
            val chatProfiles = listOf(
                "Alexandra",
                "Messi",
                "Emma",
                "Cristiano Ronaldo",
                "Angelina",
                "Priyanka",
                "Holland",
                "Keanu",
                "Ana de Armas",
                "Alina Boz",
                "Song Joong-Ki",
                "Johnny Depp",
                "Scarlett Johansson",
                "Tom Cruise",
                "Lee Min Ho",
                "Charlize Theron",
                "Donald J. Trump",
                "Narendra Modi"
            )

            if (it in chatProfiles.indices) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    action = "chat1"
                    putExtra("profilechat", chatProfiles[it])
                    putExtra("chatpic", it + 1)
                    putExtra("fromChatActivity", true)
                }

                if (it in setOf(0,7, 8,9,10,12,14,16,17)) {
                    PrefUtil(this).setBool("dialogreward",true)
                }

                startActivity(intent)
                finish()
            }


        }
        val layoutManager = GridLayoutManager(this, 3)
        binding.chatrecycle.layoutManager = layoutManager
        binding.chatrecycle.adapter = adapter
        binding.fackeccallbackbtn.setOnClickListener {
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
            items[0].imageResourceId = R.drawable.ic_pro_alexchat
            items[7].imageResourceId = R.drawable.ic_pro_keenuchat


            adapter.notifyItemChanged(0)
            adapter.notifyItemChanged(7)







        }
    }

}