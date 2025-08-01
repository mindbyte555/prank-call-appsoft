package com.example.baseapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.baseapp.localization.LocalizationActivity
import com.example.baseapp.themes.ThemeActivity
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivitySettingBinding
import java.util.Locale

class SettingActivity : AppCompatActivity() {
    lateinit var settingbind: ActivitySettingBinding
    val prefutil: PrefUtil = PrefUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingbind = ActivitySettingBinding.inflate(layoutInflater)
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
                window.statusBarColor = ContextCompat.getColor(this, R.color.setting_bg)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                window.navigationBarColor = resources.getColor(R.color.setting_bg, theme)

                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = false
                }
            }

        } catch (e: Exception) {
        }
        var selectedLanguage = prefutil.getString("Language_Localization")
        changlang(selectedLanguage!!)
        setContentView(settingbind.root)

        settingbind.constRing.setOnClickListener {
            Toast.makeText(this,"Ringtones",Toast.LENGTH_SHORT).show()
        }
        settingbind.constTheme.setOnClickListener {
            val intent = Intent(this, ThemeActivity::class.java)
            startActivity(intent)
            this.finish()        }
        settingbind.constLang.setOnClickListener {
            val intent = Intent(this, LocalizationActivity::class.java)
            startActivity(intent)
            this.finish()

        }
        settingbind.constShareApp.setOnClickListener {
            Toast.makeText(this,"Share App",Toast.LENGTH_SHORT).show()
        }
        settingbind.constRateus.setOnClickListener {
            Toast.makeText(this,"Ratings",Toast.LENGTH_SHORT).show()
        }
        settingbind.constprivpolicy.setOnClickListener {
            Toast.makeText(this,"Privacy Policy",Toast.LENGTH_SHORT).show()
        }

    }
    fun changlang(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

    }
    override fun attachBaseContext(newBase: Context?) {
        var selectedLanguage = PrefUtil(newBase!!).getString("Language_Localization")

        val locale = Locale(selectedLanguage)
        val config = Configuration()
        config.setLocale(locale)
        applyOverrideConfiguration(config)
        super.attachBaseContext(newBase)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@SettingActivity, MainActivity::class.java))
        finish()
    }
}