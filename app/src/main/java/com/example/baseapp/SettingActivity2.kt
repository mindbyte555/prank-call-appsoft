package com.example.baseapp
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.baseapp.MainActivity
import com.example.baseapp.fragments.MainFragment
import com.example.baseapp.inAppPurchases.InAppPurchases
import com.example.baseapp.localization.LocalizationActivity
import com.example.baseapp.ringtones.RingtoneActivity
import com.example.baseapp.secondPermission.SecondPermissionActivity
import com.example.baseapp.themes.ThemeActivity
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivitySetting2Binding
import com.example.iwidgets.Utils.AppInfoUtils
import java.util.Locale

class SettingActivity2 : AppCompatActivity() {
    val prefutil: PrefUtil = PrefUtil(this)
    private lateinit var binding: ActivitySetting2Binding
    private lateinit var prefUtil: PrefUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetting2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        prefUtil = PrefUtil(this)

        // Hide views from MainActivity

        setupClickListeners()
    }
    fun changlang(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
    private fun setupClickListeners() {
        binding.tvBack.setOnClickListener {

//            val bundle = Bundle()
//            bundle.putBoolean("fromTheme", true)
//
//            val mainFragment = MainFragment()
//            mainFragment.arguments = bundle
//
//            this.supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, mainFragment)
//                .commit()
            onBackPressed()


        }
        binding.ringtone.setOnClickListener {
            if (Settings.System.canWrite(this)) {
                val intent = Intent(this, RingtoneActivity::class.java)
                this?.startActivity(intent)
            } else {
                Log.d("Testag","Inside ringtone else")

                val intent = Intent(it.context, SecondPermissionActivity::class.java)
                intent.putExtra("key", "audiocall")
                it.context.startActivity(intent)
            }
//            val intent = Intent(this, RingtoneActivity::class.java)
//            startActivity(intent)
        }

        binding.calltheme.setOnClickListener {
            val intent = Intent(this, ThemeActivity::class.java)
            startActivity(intent)
        }

        binding.premiumiconmain.setOnClickListener {
            val intent = Intent(this, InAppPurchases::class.java)
            intent.putExtra("skip_btn", true)
            startActivity(intent)
        }

        binding.applang.setOnClickListener {
            val intent = Intent(this, LocalizationActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.rateus.setOnClickListener {
            AppInfoUtils(this).rateApp()
        }

        binding.share.setOnClickListener {
            AppInfoUtils(this).shareApp()
        }

        binding.consent.setOnClickListener {
            Toast.makeText(this, "Consent Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.priv.setOnClickListener {
            AppInfoUtils(this).openPrivacy()
        }
    }

    override fun onResume() {
        super.onResume()



    }
}
