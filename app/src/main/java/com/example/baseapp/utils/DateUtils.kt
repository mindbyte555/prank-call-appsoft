////package com.example.baseapp.utils
////
//////object DateUtils {
//////    fun isNextDay(storedDate: String): Boolean {
//////        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//////        val saved = format.parse(storedDate) ?: return false
//////        val calendar = Calendar.getInstance()
//////        calendar.time = saved
//////        calendar.add(Calendar.DATE, 1)
//////
//////        val today = format.format(Date())
//////        val nextDay = format.format(calendar.time)
//////        return today == nextDay
//////    }
//////
//////    fun getTodayDate(): String {
//////        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//////        return format.format(Date())
//////    }
//////}
////class CoinActivity : AppCompatActivity() {
////
////    private lateinit var buttons: List<ConstraintLayout>
////
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_coin)
////
////        buttons = listOf(
////            findViewById(R.id.day1),
////            findViewById(R.id.day2),
////            findViewById(R.id.day3),
////            findViewById(R.id.day4),
////            findViewById(R.id.day5),
////            findViewById(R.id.day6),
////            findViewById(R.id.day7)
////        )
////
////        val storedDate = PrefUtil(this).getString("Button1Date", null)
////
////        buttons.forEachIndexed { index, layout ->
////            when (index) {
////                0 -> setLayoutState(layout, true) // day1 always enabled
////                1 -> {
////                    val isNextDay = storedDate?.let {
////                        try {
////                            DateUtils.isNextDay(it)
////                        } catch (e: ParseException) {
////                            false
////                        }
////                    } ?: false
////                    setLayoutState(layout, isNextDay)
////                }
////                else -> setLayoutState(layout, false)
////            }
////        }
////
////
////        buttons[0].setOnClickListener {
////            val today = DateUtils.getTodayDate()
////            PrefUtil(this).setString("Button1Date", today)
////            Toast.makeText(this, "Date saved: $today", Toast.LENGTH_SHORT).show()
////
////            // Mark UI changes
////            markLayoutAsClaimed(0)
////
////            // Disable others
////            for (i in 1 until buttons.size) {
////                setLayoutState(buttons[i], false)
////            }
////        }
////
////    }
////
////    private fun setLayoutState(layout: ConstraintLayout, enabled: Boolean) {
////        layout.isEnabled = enabled
////        layout.alpha = if (enabled) 1f else 0.4f
////    }
////
////    private fun markLayoutAsClaimed(index: Int) {
////        val layout = buttons[index]
////        layout.setBackgroundResource(R.drawable.unselected_new_inapp)
////
////        val dayTextId = resources.getIdentifier("day${index + 1}txt", "id", packageName)
////        val coinTextId = resources.getIdentifier("coin${index + 1}", "id", packageName)
////
////        findViewById<TextView>(dayTextId)?.setTextColor(Color.BLACK)
////        findViewById<TextView>(coinTextId)?.setTextColor(Color.BLACK)
////    }
////
////
////
////
////
////}
////
////object DateUtils {
////    fun isNextDay(storedDate: String): Boolean {
////        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
////        val saved = format.parse(storedDate) ?: return false
////        val calendar = Calendar.getInstance()
////        calendar.time = saved
////        calendar.add(Calendar.DATE, 1)
////
////        val today = format.format(Date())
////        val nextDay = format.format(calendar.time)
////        return today == nextDay
////    }
////
////    fun getTodayDate(): String {
////        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
////        return format.format(Date())
////    }
////}
//
//override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    setContentView(R.layout.activity_coin)
//
//    buttons = listOf(
//        findViewById(R.id.day1),
//        findViewById(R.id.day2),
//        findViewById(R.id.day3),
//        findViewById(R.id.day4),
//        findViewById(R.id.day5),
//        findViewById(R.id.day6),
//        findViewById(R.id.day7)
//    )
//
//    val prefs = PrefUtil(this)
//    coin = prefs.getInt("coinValue", 0)
//    coinTextView.text = coin.toString()
//    val storedDate = prefs.getString("ButtonDate", null)
//    val lastClaimedIndex = prefs.getInt("LastClaimedIndex", -1)
//
//    buttons.forEachIndexed { index, layout ->
//        val isClaimed = prefs.getBool("Day${index + 1}Claimed", false)
//        if (isClaimed) {
//            markLayoutAsClaimed(index)
//            setLayoutState(layout, false)
//        } else {
//            when {
//                index == 0 && lastClaimedIndex == -1 -> setLayoutState(layout, true)
//                index == lastClaimedIndex + 1 && storedDate?.let { DateUtils.isNextDay(it) } == true -> {
//                    setLayoutState(layout, true)
//                }
//                else -> setLayoutState(layout, false)
//            }
//        }
//
//        // Attach click listener to all buttons
//        layout.setOnClickListener {
//            if (layout.isEnabled) {
//                val today = DateUtils.getTodayDate()
//                prefs.setString("ButtonDate", today)
//                prefs.setBool("Day${index + 1}Claimed", true)
//                prefs.setInt("LastClaimedIndex", index)
//
//                markLayoutAsClaimed(index)
//                setLayoutState(layout, false)
//
//                // 🎯 Coin logic
//                val coinsToAdd = when (index + 1) {
//                    1, 2 -> 1
//                    3, 4 -> 2
//                    5, 6 -> 3
//                    7 -> 5
//                    else -> 0
//                }
//
//                coin += coinsToAdd
//                prefs.setInt("coinValue", coin)
//                coinTextView.text = coin.toString()
//
//                Toast.makeText(this, "Claimed Day ${index + 1} (+$coinsToAdd)", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    }
//}