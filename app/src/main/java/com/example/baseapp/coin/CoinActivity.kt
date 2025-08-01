package com.example.baseapp.coin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.baseapp.Application.MyApplication.Companion.unlockscreen
import com.example.baseapp.adsManager.ActionRewardAdClose
import com.example.baseapp.adsManager.RewardAdObjectManager
import com.example.baseapp.adsManager.RewardAdObjectManager.mActivity
import com.example.baseapp.inAppPurchases.InAppPurchases
import com.example.baseapp.utils.Constants.dialogbox
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.iwidgets.Utils.AppInfoUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CoinActivity : AppCompatActivity() {

    private lateinit var buttons: List<ConstraintLayout>
    private lateinit var coinTextView: TextView
    private var coin: Int = 0
    lateinit var backbtn:ImageView
    lateinit var prem:ConstraintLayout
    lateinit var cointxt:TextView
    lateinit var watchadbtn:ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin)

        buttons = listOf(
            findViewById(R.id.day1),
            findViewById(R.id.day2),
            findViewById(R.id.day3),
            findViewById(R.id.day4),
            findViewById(R.id.day5),
            findViewById(R.id.day6),
            findViewById(R.id.day7)
        )
        backbtn=findViewById(R.id.coinbackbtn)
        prem=findViewById(R.id.prem)
        watchadbtn=findViewById(R.id.watchadbtn)
        cointxt=findViewById(R.id.cointxt)
        backbtn.setOnClickListener { finish() }
        prem.setOnClickListener {
            val intent = Intent(this, InAppPurchases::class.java)
            this.startActivity(intent)
        }
        watchadbtn.setOnClickListener {
            RewardAdObjectManager.requestRewardedAd(
                context = this,
                activity = this,
                showSuccessEvent = "coin_counter",
                actionOnAdClosedListenersm = object : ActionRewardAdClose {
                    override fun ActionAfterAd() {
                        RewardAdObjectManager.coin = PrefUtil(this@CoinActivity).getInt("coinValue", 0)
                        RewardAdObjectManager.coin = RewardAdObjectManager.coin +5
                        PrefUtil(this@CoinActivity).setInt("coinValue", RewardAdObjectManager.coin)
                        cointxt.text= RewardAdObjectManager.coin.toString()


                    }
                }
            )
        }

        coinTextView = findViewById(R.id.cointxt)

        val prefs = PrefUtil(this)
        coin = prefs.getInt("coinValue", 0)
        coinTextView.text = coin.toString()

        // Safely handle stored date (avoid parsing "null")
        val rawStoredDate = prefs.getString("ButtonDate", null)
        val storedDate = if (rawStoredDate == null || rawStoredDate == "null") null else rawStoredDate

        val lastClaimedIndex = prefs.getInt("LastClaimedIndex", -1)

        buttons.forEachIndexed { index, layout ->
            val isClaimed = prefs.getBool("Day${index + 1}Claimed", false)
            if (isClaimed) {
                markLayoutAsClaimed(index)
                setLayoutState(layout, false)
            } else {
                when {
                    index == 0 && lastClaimedIndex == -1 -> setLayoutState(layout, true)
                    index == lastClaimedIndex + 1 && storedDate?.let {
                        try {
                            DateUtils.isNextDay(it)
                        } catch (e: ParseException) {
                            false
                        }
                    } == true -> {
                        setLayoutState(layout, true)
                    }
                    else -> setLayoutState(layout, false)
                }
            }


                layout.setOnClickListener {
                    if (layout.isEnabled) {
                        val today = DateUtils.getTodayDate()
                        prefs.setString("ButtonDate", today)
                        prefs.setBool("Day${index + 1}Claimed", true)
                        prefs.setInt("LastClaimedIndex", index)

                        markLayoutAsClaimed(index)
                        setLayoutState(layout, false)

                        val coinsToAdd = when (index + 1) {
                            1, 2 -> 1
                            3, 4 -> 2
                            5, 6 -> 3
                            7 -> 5
                            else -> 0
                        }

                        coin += coinsToAdd
                        prefs.setInt("coinValue", coin)
                        coinTextView.text = coin.toString()

                        Toast.makeText(this, "Claimed Day ${index + 1} (+$coinsToAdd)", Toast.LENGTH_SHORT).show()
                    }
                }



        }
    }

    private fun setLayoutState(layout: ConstraintLayout, enabled: Boolean) {
        layout.isEnabled = enabled
        layout.alpha = if (enabled) 1f else 0.4f
    }

    private fun markLayoutAsClaimed(index: Int) {
        val layout = buttons[index]
        layout.setBackgroundResource(R.drawable.unselected_new_inapp)

        val dayTextId = resources.getIdentifier("day${index + 1}txt", "id", packageName)
        val coinTextId = resources.getIdentifier("coin${index + 1}", "id", packageName)

        findViewById<TextView>(dayTextId)?.setTextColor(Color.BLACK)
        findViewById<TextView>(coinTextId)?.setTextColor(Color.BLACK)
    }

    override fun onResume() {
        super.onResume()
        var isConnected = AppInfoUtils(this).isInternetAvailable()

        if(!isConnected){
            dialogbox(this)

        }
        RewardAdObjectManager.coin = PrefUtil(this).getInt("coinValue", 0)
        cointxt.text=RewardAdObjectManager.coin.toString()
    }
}


object DateUtils {
    fun isNextDay(storedDate: String): Boolean {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val saved = format.parse(storedDate) ?: return false
        val calendar = Calendar.getInstance()
        calendar.time = saved
        calendar.add(Calendar.DATE, 1)

        val today = format.format(Date())
        val nextDay = format.format(calendar.time)
        return today == nextDay
    }

    fun getTodayDate(): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(Date())
    }
}
