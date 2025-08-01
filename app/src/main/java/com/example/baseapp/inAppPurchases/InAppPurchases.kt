package com.example.baseapp.inAppPurchases

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.SkuDetailsParams
import com.example.baseapp.MainActivity
import com.example.baseapp.subscription.SubscriptionPurchaseInterface
import com.example.baseapp.utils.PrefUtil
import com.example.baseapp.utils.SharedPreferencesClass
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivityInAppPurchasesBinding
import com.example.iwidgets.Utils.AppInfoUtils
import com.google.android.material.snackbar.Snackbar
import com.google.common.collect.ImmutableList
import org.intellij.lang.annotations.Language
import java.util.Locale

class InAppPurchases : AppCompatActivity(), SubscriptionPurchaseInterface {
    private lateinit var leftRightAnimation: ObjectAnimator
    lateinit var binding: ActivityInAppPurchasesBinding
    private var mSharePrefHelper: SharedPreferencesClass? = null
    private var check: String = ""
    private var checkbillvalue: String = ""
    private var isFromMain = false
    var fromSplash = 0

    //ids
    private var oneMonth: String = "01_month"
    private var week: String = "01_week"
    var yearly: String = "06_month"

    private var billingClient: BillingClient? = null
    private var productDetailsList: ArrayList<ProductDetails>? = ArrayList()
    var retryCount = 1

    var checkInternet = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var selectedLanguage = PrefUtil(this).getString("Language_Localization", "en")
        val locale = Locale(selectedLanguage)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        binding = ActivityInAppPurchasesBinding.inflate(layoutInflater)



             setContentView(binding.root)
        leftRightAnimation =ObjectAnimator.ofPropertyValuesHolder(
            binding.subbtn,
            PropertyValuesHolder.ofFloat("scaleX", 1f, 1.05f),
            PropertyValuesHolder.ofFloat("scaleY", 1f, 1.05f)
        ).apply {
            duration = 400
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            interpolator = DecelerateInterpolator() // Smooth transition
        }

       leftRightAnimation.start()

        checkbillvalue = PrefUtil(this).getString("plan_purchased") ?: ""
        Log.e("TESTTAG", "checkvaluebill: ${checkbillvalue}")


        val showImage = intent.getBooleanExtra("skip_btn", false)
        if (showImage) {
            binding.tvSkip.visibility = View.INVISIBLE
            binding.tvBack.visibility = View.VISIBLE
        } else {

            binding.tvSkip.visibility = View.VISIBLE
            binding.tvBack.visibility = View.INVISIBLE
        }
        binding.tvSkip.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("fromInapp", false)
             //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
        }
        binding.tvBack.setOnClickListener {
            onBackPressed()
        }

        binding.canceltxt.setOnClickListener { AppInfoUtils(this@InAppPurchases).openSubscriptionPage() }

        checkInternet = isNetworkAvailable(this@InAppPurchases)

        if (PrefUtil(this).getBool("1month", false)) {
            binding.const1month.setBackgroundResource(R.drawable.selected_sim)
            binding.const6month.setBackgroundResource(R.drawable.unselected_simple)
            binding.consYear.setBackgroundResource(R.drawable.unselected_simple)
            binding.month1.setTextColor(resources.getColor(R.color.white))
            binding.month6.setTextColor(resources.getColor(R.color.black))
            binding.year.setTextColor(resources.getColor(R.color.black))
            binding.mnth1pricetext.setTextColor(resources.getColor(R.color.white))
            binding.mnth6pricetext.setTextColor(resources.getColor(R.color.black))
            binding.yearpricetext.setTextColor(resources.getColor(R.color.black))
            binding.yeartext.setTextColor(resources.getColor(R.color.black))
            binding.mnth1text.setTextColor(resources.getColor(R.color.white))
            binding.mnth6text.setTextColor(resources.getColor(R.color.black))
            binding.notrial.setTextColor(resources.getColor(R.color.white))


            check = week


        } else if (PrefUtil(this).getBool("6month", false)) {
            binding.const1month.setBackgroundResource(R.drawable.unselected_simple)
            binding.const6month.setBackgroundResource(R.drawable.selected_sim)
            binding.consYear.setBackgroundResource(R.drawable.unselected_simple)
            binding.month1.setTextColor(resources.getColor(R.color.black))
            binding.month6.setTextColor(resources.getColor(R.color.white))
            binding.year.setTextColor(resources.getColor(R.color.black))
            binding.mnth1pricetext.setTextColor(resources.getColor(R.color.black))
            binding.mnth6pricetext.setTextColor(resources.getColor(R.color.white))
            binding.yearpricetext.setTextColor(resources.getColor(R.color.black))
            binding.yeartext.setTextColor(resources.getColor(R.color.black))
            binding.mnth1text.setTextColor(resources.getColor(R.color.black))
            binding.mnth6text.setTextColor(resources.getColor(R.color.white))
            binding.notrial.setTextColor(resources.getColor(R.color.heading_2))


            check = oneMonth

        } else if (PrefUtil(this).getBool("year", false)) {
            binding.const1month.setBackgroundResource(R.drawable.unselected_simple)
            binding.const6month.setBackgroundResource(R.drawable.unselected_simple)
            binding.consYear.setBackgroundResource(R.drawable.selected_sim)
            binding.month1.setTextColor(resources.getColor(R.color.black))
            binding.month6.setTextColor(resources.getColor(R.color.black))
            binding.year.setTextColor(resources.getColor(R.color.white))
            binding.mnth1pricetext.setTextColor(resources.getColor(R.color.black))
            binding.mnth6pricetext.setTextColor(resources.getColor(R.color.black))
            binding.yearpricetext.setTextColor(resources.getColor(R.color.white))
            binding.yeartext.setTextColor(resources.getColor(R.color.white))
            binding.mnth1text.setTextColor(resources.getColor(R.color.black))
            binding.mnth6text.setTextColor(resources.getColor(R.color.black))
            binding.notrial.setTextColor(resources.getColor(R.color.heading_2))


            check = yearly
        }
        mSharePrefHelper = SharedPreferencesClass(applicationContext)

        mSharePrefHelper!!.setBooleanPreferences(
            mSharePrefHelper!!.REMOVE_AD_ACTIVITY_OPEN, true
        )
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases()
            .setListener { billingResult, list ->

                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && list != null) {
                    Log.e("TESTTAG", "onCreate1: $list")

                    for (purchase in list) {
                        verifySubPurchase(purchase)
                        Toast.makeText(this, "Purchase Successfull", Toast.LENGTH_SHORT).show()

                        Log.e("TESTTAG", "onCreate2: ${purchase.products}")
                    }
                }
            }.build()
        establishConnection()
        initialization()
        GooglePlayBuySubscription.purchasesInterface = this
        Log.e("TESTTAG", "list: ${GooglePlayBuySubscription.mSkuDetailsList}")
        if (!GooglePlayBuySubscription.mSkuDetailsList.isNullOrEmpty()) {
            Log.e("TESTTAG", "list: ${GooglePlayBuySubscription.mSkuDetailsList}")

            GooglePlayBuySubscription.mSkuDetailsList?.let {
                if (it.size > 0) {
                    binding.mnth1pricetext.text = "${it[1].price}"


                    PrefUtil(this).setString("oneMonth", it[1].title)
                    price1 = it[1].price

                    PrefUtil(this@InAppPurchases).setString("oneMonth1", it[1].price)

                }
                //new
                if (it.size > 1) {
                    binding.mnth6pricetext.text = "${it[0].price}"
//                    binding.textCurrency1.text = "${it[1].priceCurrencyCode}"
                    PrefUtil(this).setString("sixMonth", it[0].title)
                    PrefUtil(this@InAppPurchases).setString("sixMonth1", it[0].price)
                    price6 = it[0].price

                }
                if (it.size > 2) {
                    binding.yearpricetext.text = " ${it[2].price}"
//                    binding.textCurrency2.text = "${it[2].priceCurrencyCode}"
                    PrefUtil(this).setString("oneYear", it[2].title)
                    PrefUtil(this@InAppPurchases).setString("oneYear1", it[1].price)
                    price12 = it[2].price


                }

            }
        } else {
            Log.e("TESTTAG", "list empty: ${GooglePlayBuySubscription.mSkuDetailsList}")

        }


    }

    override fun productPurchasedSuccessful() {
        PrefUtil(this).setBool("is_premium", true)

//        startActivity(Intent(this@InAppPurchases, MainActivity::class.java))
//        finish()
        PrefUtil(this).setString("plan_purchased", check)
        Log.e("TESTTAG", "Successfulll Purchase ${check} value")

//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//        finish()

            val intent = Intent(this@InAppPurchases, MainActivity::class.java)
            intent.putExtra("fromInapp", false)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    override fun productPurchaseFailed() {
        Toast.makeText(this@InAppPurchases, "Subscription Failed", Toast.LENGTH_LONG).show()
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    private fun establishConnection() {
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    getProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
                if (retryCount <= 3) establishConnection()
                retryCount++
            }
        })
    }

    private fun verifySubPurchase(purchase: Purchase) {
        val acknowledgePurchaseParams =
            AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
        billingClient!!.acknowledgePurchase(acknowledgePurchaseParams) { billingResult: BillingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                PrefUtil(this).setBool("is_premium", true)
                Log.e("TESTTAG", "Successfulll Purchase inside verify")

                Toast.makeText(this, "verifySubPurchase", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun getProducts() {
        //for getting the pricing from console
        val skuList: MutableList<String> = java.util.ArrayList()
        skuList.add("01_month")
        skuList.add("01_week")
        skuList.add("06_month")


        val param2 = SkuDetailsParams.newBuilder()
        param2.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
        billingClient!!.querySkuDetailsAsync(
            param2.build()
        ) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.e("TESTTAG", "CALLED SIZE ${skuDetailsList}")

            } else {
                Toast.makeText(
                    applicationContext, " Error " + billingResult.debugMessage, Toast.LENGTH_SHORT
                ).show()
            }
        }


        val productList = ImmutableList.of(
            //Product 1
            QueryProductDetailsParams.Product.newBuilder().setProductId("01_week")
                .setProductType(BillingClient.ProductType.SUBS).build(),
            //Product 2
            QueryProductDetailsParams.Product.newBuilder().setProductId("01_month")
                .setProductType(BillingClient.ProductType.SUBS).build(),
            // Product 3
            QueryProductDetailsParams.Product.newBuilder().setProductId("06_month")
                .setProductType(BillingClient.ProductType.SUBS).build()
        )
        val params = QueryProductDetailsParams.newBuilder().setProductList(productList).build()

        billingClient!!.queryProductDetailsAsync(params) { billingResult: BillingResult?, prodDetailsList: List<ProductDetails>? ->
            prodDetailsList?.forEach {
                productDetailsList?.add(it)
            }
//            prodDetailsList?.let { productDetailsList?.addAll(it) }
        }
    }

    fun launchPurchaseFlow(productDetails: ProductDetails) {
        assert(productDetails.subscriptionOfferDetails != null)

        val productDetailsParamsList = ImmutableList.of(
            BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails)
                .setOfferToken(productDetails.subscriptionOfferDetails!![0].offerToken).build()
        )
        val billingFlowParams =
            BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList)
                .build()
        val billingResult =
            billingClient!!.launchBillingFlow(this@InAppPurchases!!, billingFlowParams)
    }

    override fun onResume() {
        super.onResume()
        billingClient!!.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build()
        ) { billingResult: BillingResult, list: List<Purchase> ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.e("TESTTAG", "onResume::: $list")

                for (purchase in list) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                        verifySubPurchase(purchase)
                    }
                }
            }
        }
        if (PrefUtil(this).getBool("is_premium", false) == false) {
            PrefUtil(this).setString("plan_purchased", "")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (PrefUtil(this).getBool("is_premium", false) == false){
            Log.d("TestTag","Its Cancelled")
        }
    }

    private fun initialization() {
        if (PrefUtil(this).getBool("is_premium", false) == false) {
            PrefUtil(this).setString("plan_purchased", "")

        }


        binding.const1month.setOnClickListener {
            binding.const1month.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    binding.const1month.animate().scaleX(1f).scaleY(1f).duration = 100
                }
        //    if (checkInternet == false) {
         //       Toast.makeText(this, "Please Turn Internet Connection On", Toast.LENGTH_SHORT)
          //          .show()
       //     } else {
//                if (checkbillvalue.equals("01_month")) {
                if (checkbillvalue == "01_week") {
                    Log.e("TESTTAG", "Purchased 1months")

//                    Toast.makeText(this,"Plan Already Selected",Toast.LENGTH_SHORT).show()
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "You have already purchased this plan",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    check = week

                    binding.const1month.setBackgroundResource(R.drawable.selected_sim)
                    binding.const6month.setBackgroundResource(R.drawable.unselected_simple)
                    binding.consYear.setBackgroundResource(R.drawable.unselected_simple)
                    binding.month1.setTextColor(resources.getColor(R.color.white))
                    binding.month6.setTextColor(resources.getColor(R.color.black))
                    binding.year.setTextColor(resources.getColor(R.color.black))
                    binding.mnth1pricetext.setTextColor(resources.getColor(R.color.white))
                    binding.mnth6pricetext.setTextColor(resources.getColor(R.color.black))
                    binding.yearpricetext.setTextColor(resources.getColor(R.color.black))
                    binding.yeartext.setTextColor(resources.getColor(R.color.black))
                    binding.mnth1text.setTextColor(resources.getColor(R.color.white))
                    binding.mnth6text.setTextColor(resources.getColor(R.color.black))
                    binding.notrial.setTextColor(resources.getColor(R.color.white))



                    PrefUtil(this).setBool("01_week", true)
                    PrefUtil(this).setBool("01_month", false)
                    PrefUtil(this).setBool("06_month", false)

                } else {
                    Log.e("TESTTAG", "else 1months")

                    check = week

                    binding.const1month.setBackgroundResource(R.drawable.selected_sim)
                    binding.const6month.setBackgroundResource(R.drawable.unselected_simple)
                    binding.consYear.setBackgroundResource(R.drawable.unselected_simple)
                    binding.month1.setTextColor(resources.getColor(R.color.white))
                    binding.month6.setTextColor(resources.getColor(R.color.black))
                    binding.year.setTextColor(resources.getColor(R.color.black))
                    binding.mnth1pricetext.setTextColor(resources.getColor(R.color.white))
                    binding.mnth6pricetext.setTextColor(resources.getColor(R.color.black))
                    binding.yearpricetext.setTextColor(resources.getColor(R.color.black))
                    binding.yeartext.setTextColor(resources.getColor(R.color.black))
                    binding.mnth1text.setTextColor(resources.getColor(R.color.white))
                    binding.mnth6text.setTextColor(resources.getColor(R.color.black))
                    binding.notrial.setTextColor(resources.getColor(R.color.white))



                    PrefUtil(this).setBool("01_week", true)
                    PrefUtil(this).setBool("01_month", false)
                    PrefUtil(this).setBool("06_month", false)

                }


       //     }

        }

        binding.const6month.setOnClickListener {
            binding.recommended.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    binding.recommended.animate().scaleX(1f).scaleY(1f).duration = 100
                }

            binding.const6month.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    binding.const6month.animate().scaleX(1f).scaleY(1f).duration = 100
                }
//            if (checkInternet == false) {
//                Toast.makeText(this, "Please Turn Internet Connection On", Toast.LENGTH_SHORT)
//                    .show()
//            } else {
//                if (checkbillvalue == "01_week") {
                if (checkbillvalue == "01_month") {

                    Log.e("TESTTAG", "Purchased 6months")


                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "You have already purchased this plan",
                        Snackbar.LENGTH_SHORT
                    ).show()

                    check = oneMonth

                    binding.const1month.setBackgroundResource(R.drawable.unselected_simple)
                    binding.const6month.setBackgroundResource(R.drawable.selected_sim)
                    binding.consYear.setBackgroundResource(R.drawable.unselected_simple)
                    binding.month1.setTextColor(resources.getColor(R.color.black))
                    binding.month6.setTextColor(resources.getColor(R.color.white))
                    binding.year.setTextColor(resources.getColor(R.color.black))
                    binding.mnth1pricetext.setTextColor(resources.getColor(R.color.black))
                    binding.mnth6pricetext.setTextColor(resources.getColor(R.color.white))
                    binding.yearpricetext.setTextColor(resources.getColor(R.color.black))
                    binding.yeartext.setTextColor(resources.getColor(R.color.black))
                    binding.mnth1text.setTextColor(resources.getColor(R.color.black))
                    binding.mnth6text.setTextColor(resources.getColor(R.color.white))
                    binding.notrial.setTextColor(resources.getColor(R.color.heading_2))

                    PrefUtil(this).setBool("01_week", false)
                    PrefUtil(this).setBool("01_month", true)
                    PrefUtil(this).setBool("06_month", false)
                } else {
                    Log.e("TESTTAG", "else 6months")

                    check = oneMonth

                    binding.const1month.setBackgroundResource(R.drawable.unselected_simple)
                    binding.const6month.setBackgroundResource(R.drawable.selected_sim)
                    binding.consYear.setBackgroundResource(R.drawable.unselected_simple)
                    binding.month1.setTextColor(resources.getColor(R.color.black))
                    binding.month6.setTextColor(resources.getColor(R.color.white))
                    binding.year.setTextColor(resources.getColor(R.color.black))
                    binding.mnth1pricetext.setTextColor(resources.getColor(R.color.black))
                    binding.mnth6pricetext.setTextColor(resources.getColor(R.color.white))
                    binding.yearpricetext.setTextColor(resources.getColor(R.color.black))
                    binding.yeartext.setTextColor(resources.getColor(R.color.black))
                    binding.mnth1text.setTextColor(resources.getColor(R.color.black))
                    binding.mnth6text.setTextColor(resources.getColor(R.color.white))
                    binding.notrial.setTextColor(resources.getColor(R.color.heading_2))



                    PrefUtil(this).setBool("01_week", false)
                    PrefUtil(this).setBool("01_month", true)
                    PrefUtil(this).setBool("06_month", false)
                }


      //      }

        }

        binding.consYear.setOnClickListener {

            binding.consYear.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    binding.consYear.animate().scaleX(1f).scaleY(1f).duration = 100
                }
//            if (checkInternet == false) {
//                Toast.makeText(this, "Please Turn Internet Connection On", Toast.LENGTH_SHORT)
//                    .show()
//            } else {
//                if (checkbillvalue == "06_month") {
                if (checkbillvalue == "06_month") {
                    Log.e("TESTTAG", "Purchased 1year")

                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "You have already purchased this plan",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    check = yearly

                    binding.const1month.setBackgroundResource(R.drawable.unselected_simple)
                    binding.const6month.setBackgroundResource(R.drawable.unselected_simple)
                    binding.consYear.setBackgroundResource(R.drawable.selected_sim)
                    binding.month1.setTextColor(resources.getColor(R.color.black))
                    binding.month6.setTextColor(resources.getColor(R.color.black))
                    binding.year.setTextColor(resources.getColor(R.color.white))
                    binding.mnth1pricetext.setTextColor(resources.getColor(R.color.black))
                    binding.mnth6pricetext.setTextColor(resources.getColor(R.color.black))
                    binding.yearpricetext.setTextColor(resources.getColor(R.color.white))
                    binding.yeartext.setTextColor(resources.getColor(R.color.white))
                    binding.mnth1text.setTextColor(resources.getColor(R.color.black))
                    binding.mnth6text.setTextColor(resources.getColor(R.color.black))
                    binding.notrial.setTextColor(resources.getColor(R.color.heading_2))


                    PrefUtil(this).setBool("01_week", false)
                    PrefUtil(this).setBool("01_month", false)
                    PrefUtil(this).setBool("06_month", true)

                } else {
                    Log.e("TESTTAG", "else 1year")

                    check = yearly

                    binding.const1month.setBackgroundResource(R.drawable.unselected_simple)
                    binding.const6month.setBackgroundResource(R.drawable.unselected_simple)
                    binding.consYear.setBackgroundResource(R.drawable.selected_sim)
                    binding.month1.setTextColor(resources.getColor(R.color.black))
                    binding.month6.setTextColor(resources.getColor(R.color.black))
                    binding.year.setTextColor(resources.getColor(R.color.white))
                    binding.mnth1pricetext.setTextColor(resources.getColor(R.color.black))
                    binding.mnth6pricetext.setTextColor(resources.getColor(R.color.black))
                    binding.yearpricetext.setTextColor(resources.getColor(R.color.white))
                    binding.yeartext.setTextColor(resources.getColor(R.color.white))
                    binding.mnth1text.setTextColor(resources.getColor(R.color.black))
                    binding.mnth6text.setTextColor(resources.getColor(R.color.black))
                    binding.notrial.setTextColor(resources.getColor(R.color.heading_2))



                    PrefUtil(this).setBool("01_week", false)
                    PrefUtil(this).setBool("01_month", false)
                    PrefUtil(this).setBool("06_month", true)
                }


         //   }


        }

        binding.subbtn.setOnClickListener {
            if (checkInternet == false) {
                Toast.makeText(this, "Please Turn Internet Connection On", Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                if (productDetailsList?.size!! > 0) {
                    if (check == week) {
                        Toast.makeText(this, " 1 week plan selected", Toast.LENGTH_SHORT).show()
                        launchPurchaseFlow(productDetailsList!![1])

                    } else if (check == oneMonth) {
                        Toast.makeText(this, " 1 Month plan selected", Toast.LENGTH_SHORT).show()
                        launchPurchaseFlow(productDetailsList!![0])

                    } else if (check == yearly) {
                        Toast.makeText(this, " 6 Months selected", Toast.LENGTH_SHORT).show()
                        launchPurchaseFlow(productDetailsList!![2])

                    } else {
                        Toast.makeText(this, " 1 Month plan selected", Toast.LENGTH_SHORT).show()
                        launchPurchaseFlow(productDetailsList!![0])
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    leftRightAnimation.cancel()
    }


 // Start the animation

    override fun onBackPressed() {
        val isBackPressEnabled = intent.getBooleanExtra("skip_btn", false)
        if (isBackPressEnabled) {
            super.onBackPressed()
            finish()
        }
        else{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fromInapp", false)
           // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        // Otherwise, do nothing
    }

    companion object {
        var price1 = ""
        var price6 = ""
        var price12 = ""
    }
}