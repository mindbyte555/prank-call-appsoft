package com.example.baseapp.onbording

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.baseapp.Application.MyApplication
import com.example.baseapp.SplashScreen.Companion.appOpenShow
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.inAppPurchases.InAppPurchases
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivityOnbordingBinding

class onbording : AppCompatActivity() {
    private lateinit var dots: Array<ImageView?>
    lateinit var binding: ActivityOnbordingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnbordingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        var prefUtil = PrefUtil(this)
        prefUtil.setInt("onboardingTrue", 1)

        // Initialize AdView for Banner Ad


        // Hide shimmer if ads are not needed
        if (PrefUtil(this).getBool("is_premium", false) ||!MyApplication.isEnabled || !MyApplication.bannerRecEnabled || !appOpenShow) {

            binding.adContainer.visibility = View.GONE
        } else {
            AdsManager.loadMediumRectangleBannerAd(
                binding.rectbanner,
                this@onbording,
                object : AdsManager.AdmobBannerAdListener {
                    override fun onAdLoaded() {
                        binding.rectbanner.setBackgroundColor(
                            ContextCompat.getColor(
                                this@onbording, R.color.trans
                            )
                        )
                        binding.adContainer.visibility = View.VISIBLE // Make container visible after ad loads
                    }

                    override fun onAdFailed() {
                        binding.rectbanner.setBackgroundColor(
                            ContextCompat.getColor(
                                this@onbording, R.color.gray1
                            )
                        )
                        binding.adContainer.visibility = View.VISIBLE // Make container visible after ad loads
                    }
                }
            )
        }

        binding.viewpager.adapter = pagerAdapter(supportFragmentManager)
        initializeDots()
        updateIndicator(0)
        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                when (binding.viewpager.currentItem) {
                    0 -> {
                        binding.btnBordingSkip.visibility = View.INVISIBLE
                        binding.buttonNext.setText(R.string.next)
                        updateIndicator(0)
                    }

                    1 -> {
                        binding.btnBordingSkip.visibility = View.INVISIBLE
                        binding.buttonNext.setText(R.string.next)
                        updateIndicator(1)
                    }

                    2 -> {
                        binding.btnBordingSkip.visibility = View.GONE
                        binding.buttonNext.setText("Done")
                        updateIndicator(2)
                    }
                }
            }

            override fun onPageSelected(p0: Int) {}
            override fun onPageScrollStateChanged(p0: Int) {}
        })

        binding.btnBordingSkip.clickWithDebounce {
           // val intent = Intent(this, LocalizationActivity::class.java)
            val intent = Intent(this, InAppPurchases::class.java)
            intent.putExtra("user", 10)
            startActivity(intent)
            this.finish()
        }

        binding.buttonNext.setOnClickListener {
            when (binding.viewpager.currentItem) {
                0, 1 -> {
                    val nextFragmentIndex = binding.viewpager.currentItem + 1
                    binding.viewpager.setCurrentItem(nextFragmentIndex, true)
                }
                2 -> {
                    AdsManager.showInterstitial(true, this, object : AdsManager.InterstitialAdListener {
                        override fun onAdClosed() {
                            val intent = Intent(this@onbording, InAppPurchases::class.java)
                            intent.putExtra("user", 10)
                            intent.putExtra("fromInapp", true)
                            startActivity(intent)
                            this@onbording.finish()
                        }
                    })
                }
            }
        }
    }

    private fun initializeDots() {
        dots = arrayOfNulls(3)
        for (i in dots.indices) {
            dots[i] = ImageView(this)
            val widthHeight = resources.getDimensionPixelSize(R.dimen.dot_size)
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams(widthHeight, widthHeight))
            params.setMargins(8, 0, 8, 0)
            dots[i]?.layoutParams = params
            dots[i]?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselecteddot))
            binding.indicatorLayout.addView(dots[i])
        }
    }

    private fun updateIndicator(position: Int) {
        for (i in dots.indices) {
            if (i == position) {
                dots[i]?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selecteddot__2_))
            } else {
                dots[i]?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselecteddot))
            }
        }
    }

    fun View.clickWithDebounce(debounceTime: Long = 200L, action: () -> Unit) {
        this.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime: Long = 0

            override fun onClick(v: View) {
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
                else action()

                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }

    override fun onBackPressed() {
    }
}
