package com.example.baseapp.fragments.sms

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.baseapp.Application.MyApplication
import com.example.baseapp.Application.MyApplication.Companion.bannerEnabled
import com.example.baseapp.Application.MyApplication.Companion.isEnabled
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.themes.ThemeAdapter
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivityThemeBinding
import com.example.fakecall.databinding.ActivityThemeSmsBinding
import java.util.Locale

class ThemeSmsActivity : AppCompatActivity() {
    private lateinit var themebinding: ActivityThemeSmsBinding
    private var checked = false
    private var themeclick = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themebinding = ActivityThemeSmsBinding.inflate(layoutInflater)
        setContentView(themebinding.root)

        setupStatusBar()

        val smsDataList = listOf(
            SmsData(R.drawable.ic_default, "Default"),
            SmsData(R.drawable.ic_whastapp, "WhatsApp"),
            SmsData(R.drawable.ic_messanger, "Messenger"),
            SmsData(R.drawable.ic_telegram, "Telegram"),
            SmsData(R.drawable.ic_instagram, "Instagram"),
        )

        // Back button
        themebinding.themebackbtn.setOnClickListener {
            onBackPressed()
        }

        // Setup Theme RecyclerView

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.circle_width)

        themebinding.themerecycle.layoutManager = GridLayoutManager(this, 2)
        themebinding.themerecycle.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, true))
        themebinding.themerecycle.adapter = ThemeSmsAdapter(this, smsDataList) {
            themeclick = it
            checked = true
        }
        // Done button
        themebinding.themedonenbtn.setOnClickListener {
            if (themeclick != -1) {
                PrefUtil(this).setInt("smstheme", themeclick)
            }

            if (checked) {
                AdsManager.showInterstitial(true, this, object : AdsManager.InterstitialAdListener {
                    override fun onAdClosed() {
                        finish()
                    }
                })
                checked = false
            } else {
                finish()
            }
        }

        // Load Adaptive Banner Ad
        if (PrefUtil(this).getBool("is_premium", false)||!isEnabled || !bannerEnabled) {


            themebinding.layoutNative.visibility = View.GONE
        }
        else {
            if (isEnabled) {
                themebinding.layoutNative.visibility = View.VISIBLE
                if (!MyApplication.bannerCollapsible) {
                    AdsManager.loadAdaptiveBannerAd(
                        themebinding.layoutNative, this,
                        object : AdsManager.AdmobBannerAdListener {
                            override fun onAdLoaded() {
                                Log.d("ThemeActivity", "Adaptive Banner Ad Loaded")
                                themebinding.layoutNative.visibility = View.VISIBLE
                            }

                            override fun onAdFailed() {
                                Log.d("ThemeActivity", "Adaptive Banner Ad Failed to Load")
                                themebinding.layoutNative.visibility = View.GONE
                            }
                        }
                    )
                } else {
                    AdsManager.loadCollapsible(
                        themebinding.layoutNative, this,
                        object : AdsManager.AdmobBannerAdListener {
                            override fun onAdLoaded() {
                                Log.d("ThemeActivity", "Standard Banner Ad Loaded")
                                themebinding.layoutNative.visibility = View.VISIBLE
                            }

                            override fun onAdFailed() {
                                Log.d("ThemeActivity", "Standard Banner Ad Failed to Load")
                                themebinding.layoutNative.visibility = View.GONE
                            }
                        }
                    )
                }
            } else {
                // Both flags are false, so hide the ad container
                themebinding.layoutNative.visibility = View.GONE
            }
        }
    }

    private fun setupStatusBar() {
        try {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            window.navigationBarColor = resources.getColor(R.color.white, theme)

            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = true
            }
        } catch (e: Exception) {
            Log.e("ThemeActivity", "Status bar setup failed", e)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val selectedLanguage = PrefUtil(newBase!!).getString("Language_Localization") ?: "en"
        val locale = Locale(selectedLanguage)
        val config = Configuration()
        config.setLocale(locale)
        applyOverrideConfiguration(config)
        super.attachBaseContext(newBase)
    }
}