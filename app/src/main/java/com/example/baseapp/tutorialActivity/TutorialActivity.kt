package com.example.baseapp.tutorialActivity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.viewpager.widget.ViewPager
import com.example.baseapp.MainActivity
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivityTutorialBinding
import java.util.Locale

class TutorialActivity : AppCompatActivity() {
    lateinit var binding:ActivityTutorialBinding
    var count: Int = 0
    val prefutil: PrefUtil = PrefUtil(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
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
        binding.skipbtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.continuenbtn.setOnClickListener {
            count++

            updateView()

            if (count < 3) {
                binding.tutviewPager.currentItem = count
            } else if (count == 3) {
                finish()
                prefutil.setBool("tutorial",true)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
        }
        val items = listOf(
            TutorialDataClass(
                "1",

                R.drawable.afghanistan
            ),
            TutorialDataClass("2",R.drawable.england),
            TutorialDataClass(
                "3",
                R.drawable.pakistan

            )



        )
        val adapter = TutorialAdapter(items, this)
        binding.tutviewPager.adapter = adapter
        binding.tutviewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }


            override fun onPageSelected(position: Int) {
                // This method is called when a new page is selected
                count = position
                updateView()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }


        })

    }
    private fun updateView() {
        when (count) {
            0 -> {
                binding.viewPg1.setBackgroundResource(R.drawable.view_selected)
                binding.viewPg2.setBackgroundResource(R.drawable.view_unselected)
                binding.viewPg3.setBackgroundResource(R.drawable.view_unselected)


                binding.continuenbtn.text = getString(R.string.continuenbutton)

            }

            1 -> {
                binding.viewPg1.setBackgroundResource(R.drawable.view_unselected)
                binding.viewPg2.setBackgroundResource(R.drawable.view_selected)
                binding.viewPg3.setBackgroundResource(R.drawable.view_unselected)


                binding.continuenbtn.text = getString(R.string.continuenbutton)

            }

            2 -> {
                binding.viewPg1.setBackgroundResource(R.drawable.view_unselected)
                binding.viewPg2.setBackgroundResource(R.drawable.view_unselected)
                binding.viewPg3.setBackgroundResource(R.drawable.view_selected)
                binding.skipbtn.visibility=View.GONE
                binding.continuenbtn.text = getString(R.string.endbutton)

            }




            else -> {}
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