package com.example.baseapp.themes

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.baseapp.Application.MyApplication
import com.example.baseapp.Application.MyApplication.Companion.bannerEnabled
import com.example.baseapp.Application.MyApplication.Companion.isEnabled
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivityThemeBinding
import java.util.Locale

class ThemeActivity : AppCompatActivity() {

    private lateinit var themebinding: ActivityThemeBinding
    private var checked = false
    private var themeclick = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themebinding = ActivityThemeBinding.inflate(layoutInflater)
        setContentView(themebinding.root)

        setupStatusBar()

        val imageList = listOf(
            R.drawable.ic_theme1, R.drawable.ic_theme2,
            R.drawable.ic_theme3, R.drawable.ic_theme4,
            R.drawable.ic_theme5, R.drawable.ic_theme6
        )

        // Back button
        themebinding.themebackbtn.setOnClickListener {
            onBackPressed()
        }

        // Setup Theme RecyclerView
        themebinding.themerecycle.layoutManager = GridLayoutManager(this, 3)
        themebinding.themerecycle.adapter = ThemeAdapter(this, imageList) {
            themeclick = it
            checked = true
        }

        // Done button
        themebinding.themedonenbtn.setOnClickListener {
            if (themeclick != -1) {
                PrefUtil(this).setInt("lasttheme", themeclick)
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
