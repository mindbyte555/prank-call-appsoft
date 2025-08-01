package com.example.baseapp.localization

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseapp.ActivityCustomcall
import com.example.baseapp.Application.MyApplication
import com.example.baseapp.MainActivity
import com.example.fakecall.R
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.adsManager.AdsManager.Companion.showNative
import com.example.baseapp.inAppPurchases.InAppPurchases
import com.example.baseapp.onbording.onbording

import com.example.fakecall.databinding.ActivityLocalizationBinding
import com.example.baseapp.utils.PrefUtil
import java.util.Locale

class LocalizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLocalizationBinding

    val prefutil: PrefUtil = PrefUtil(this)
    private lateinit var adapter: RecyclerViewAdapter
    var fromInapp=false
    var user=0
    var lang=1
    var splash = false



    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalizationBinding.inflate(layoutInflater)
        val onboard=prefutil.getBool("onboard")
        fromInapp = intent.getBooleanExtra("fromInapp", false)
        splash=intent.getBooleanExtra("splash", false)
        Log.d("TestTag","splash$splash")

        user=intent.getIntExtra("user",0)
        if(fromInapp||splash){
            binding.languagebackbtn.visibility=View.INVISIBLE
        }
        else
        {
            binding.languagebackbtn.visibility=View.VISIBLE

        }

        setContentView(binding.root)

        if (PrefUtil(this).getBool("is_premium", false) ||!MyApplication.isEnabled || !MyApplication.bannerRecEnabled) {
            binding.adContainer.visibility = View.GONE

        } else {
            AdsManager.loadMediumRectangleBannerAd(
                binding.rectbanner,
                this@LocalizationActivity,
                object : AdsManager.AdmobBannerAdListener {
                    override fun onAdLoaded() {
                        binding.rectbanner.setBackgroundColor(
                            ContextCompat.getColor(
                                this@LocalizationActivity, R.color.trans
                            )
                        )
                        binding.adContainer.visibility = View.VISIBLE // Make container visible after ad loads
                    }

                    override fun onAdFailed() {
                        binding.rectbanner.setBackgroundColor(
                            ContextCompat.getColor(
                                this@LocalizationActivity, R.color.gray1
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

                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = false
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
        val msg = prefutil.getInt("pos", 1)
        val items = listOf(
            Item(
                "Urdu (اردو)",

                R.drawable.pakistan
            ),
            Item(
                "English",

                R.drawable.ic_england
            ),
            Item(
                "French (français)",

                R.drawable.france
            ),
            Item(
                "Arabic (عربي)",

                R.drawable.ic_arab
            ),
            Item(
                "German (Deutsch)",

                R.drawable.ic_germany
            ),
            Item(
                "Hindi (हिंदी)",

                R.drawable.ic_india
            ),
            Item(
                "Italian (Italiana)",

                R.drawable.ic_italy
            ),
            Item(
                "Korean (한국인)",

                R.drawable.ic_korean
            ),
            Item(
                "Persian (فارسی)",

                R.drawable.ic_persian
            ),
            Item(
                "Portuguese (Português)",

                R.drawable.ic_portugal
            ),
            Item(
                "Spanish (Español)",

                R.drawable.ic_spanish
            )

            )



        binding.modebtn.setOnClickListener {

                if(splash)
                {

                    startActivity(Intent(this@LocalizationActivity, InAppPurchases::class.java))
                    intent.putExtra("user", 10)
                    intent.putExtra("fromInapp", true)
                    splash=false
                    finish()

                }
            else{
                    startActivity(Intent(this@LocalizationActivity, MainActivity::class.java))
                    finish()
            }






        }
        binding.applylang.setOnClickListener {
            Log.d("TestTag","splash$splash")

            changelange()
            if(splash)
            {
//                startActivity(Intent(this@LocalizationActivity, onbording::class.java))
//                finish()
//                AdsManager.showInterstitial(true, this, object : AdsManager.InterstitialAdListener {
//                    override fun onAdClosed() { }
//                    })   //fromInapp=true


                var intent =Intent(this@LocalizationActivity, onbording::class.java).apply {
                    putExtra("fromInapp", true)
                    putExtra("user", 10)
                }
                splash = false
                startActivity(intent)
                finish()


            }
            else{

                var intent =Intent(this@LocalizationActivity, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra("refresh", true)
                }
               startActivity(intent)

              //  onBackPressed()
            }

        }
        binding.languagebackbtn.setOnClickListener {
            onBackPressed()
        }


        adapter = RecyclerViewAdapter(items) {
            val msg = prefutil.getInt("pos", 1)

            if (it == msg) {
                Toast.makeText(this, " Language alreadyselected", Toast.LENGTH_SHORT).show()

            } else {


                prefutil.setInt("pos", it)
                //changelange()
            }
           // lang=it

        }
        binding.localizedrecycle.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.localizedrecycle.adapter = adapter
        adapter.setSelectedItemPosition(msg)


    }


    fun changelange() {
        val selectedLanguage = when (prefutil.getInt("pos", 1))
//        val selectedLanguage = when (selectedLanguage)
        {
            0 -> "pa"
            1 -> "en"
            2 -> "fr"
            3 -> "ar"
            4 -> "de"
            5 -> "hi"
            6 -> "it"
            7 -> "ko"
            8 -> "fa"
            9 -> "pt"
            10 -> "es"
            else -> "en"
        }

        val locale = Locale(selectedLanguage)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        prefutil.setString("Language_Localization", selectedLanguage)
        recreate()
  //      val configuration = resources.configuration
//        configuration.setLocale(locale)
//        resources.updateConfiguration(configuration, resources.displayMetrics)


    }
    /*
     prefUtil.setString("selectedLanguage", selectedlanguage)
            val locale = Locale(selectedlanguage)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

     */

        override fun onBackPressed() {
//                startActivity(Intent(this@LocalizationActivity, MainActivity::class.java))
            if(!splash){
                finish()

            }
        }


        override fun attachBaseContext(newBase: Context?) {
            var selectedLanguage = PrefUtil(newBase!!).getString("Language_Localization")


            val locale = Locale(selectedLanguage)
            val config = Configuration()
            config.setLocale(locale)
            applyOverrideConfiguration(config)
            super.attachBaseContext(newBase)
        }


}