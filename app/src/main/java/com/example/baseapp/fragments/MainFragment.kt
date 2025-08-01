package com.example.baseapp.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.baseapp.ActivityCustomcall
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.adsManager.AdsManager.Companion.showCollape
import com.example.baseapp.adsManager.AdsManager.Companion.showInterstitial
import com.example.baseapp.adsManager.RewardAdObjectManager
import com.example.baseapp.adsManager.RewardAdObjectManager.mActivity
import com.example.baseapp.callsActivity.CallActivity
import com.example.baseapp.chatActivity.ChatActivity
import com.example.baseapp.chatFragtoActivity.ChatMainActivity
import com.example.baseapp.coin.CoinActivity
import com.example.baseapp.inAppPurchases.InAppPurchases
import com.example.baseapp.utils.EventNames.SEEALL_CALL
import com.example.baseapp.utils.EventNames.SEEALL_CHAT
import com.example.baseapp.utils.EventNames.SIMULATE_CALLMAIN
import com.example.baseapp.utils.EventNames.Shedule
import com.example.baseapp.utils.EventNames.sms
import com.example.baseapp.utils.EventNames.video_call
import com.example.baseapp.utils.FirebaseCustomEvents
import com.example.baseapp.utils.PrefUtil
import com.example.baseapp.utils.StmulateActivity
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentMainBinding
import com.mindinventory.midrawer.MIDrawerView

class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    lateinit var prefutil: PrefUtil
    var message = ""
    var number = 0
    var btnhide:RelativeLayout?=null
    var mobile = 1111111111111111111
    private var isImageChooserOpen = false
    private val PERMISSION_REQUEST_STORAGE = 1001
    private val PICK_IMAGE_REQUEST = 1002
    var shouldShowAd = true
    private var coin: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefutil = PrefUtil(requireContext())

        val fromTheme = arguments?.getBoolean("fromTheme", false) ?: false
        shouldShowAd = !fromTheme



    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.scheduletext.isSelected=true
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)
            val bottomNavigationImage = requireActivity().findViewById<ImageView>(R.id.homeImg)
            val textprank = requireActivity().findViewById<TextView>(R.id.mainhead)
            val drawerimg = requireActivity().findViewById<ImageView>(R.id.drawerimg)
            val premimg = requireActivity().findViewById<ImageView>(R.id.premiumiconmain)

            val mainconst = requireActivity().findViewById<MIDrawerView>(R.id.mainconsy)
            mainconst.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.main_bg))
//            bottomNavigation?.visibility = View.VISIBLE
//            bottomNavigationImage?.visibility = View.VISIBLE
            textprank.visibility=View.VISIBLE
            drawerimg.visibility=View.VISIBLE
            premimg.visibility=View.VISIBLE

        } catch (e: Exception) {
            e.printStackTrace()
        }


        return binding?.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.e("TestTag","onViewCreated")


        val chatfrag = arguments?.getBoolean("fromChatActivity") ?: false
        val sms_chatfrag = arguments?.getBoolean("fromsms_chat") ?: false

//        val callfrag = prefutil.getBool("calls", )?: false
        val callfrag = arguments?.getBoolean("fromCallActivity") ?: false


//        binding.editname.setOnLongClickListener(OnLongClickListener { //  Do Something or Don't
//            true
//        })

        if (chatfrag == true) {

            message = arguments?.getString("chatprofile").toString()
            number = arguments?.getInt("chatno")!!
            Log.d("Testtag", "value of profile in fragment ${message}")
            Log.d("Testtag", "value of pic fragment ${number}")
//            val action = MainFragmentDirections.actionMainFragmentToChatFragment(message, number)
//            findNavController().navigate(action)
            PrefUtil(requireContext()).setBool("sms",true)
            val action = MainFragmentDirections.actionMainFragmentToScedhuleFragment(
                message,
                number,
                3333333
            )
            findNavController().navigate(action)

            prefutil?.setString("profile", message)
            prefutil?.setInt("profile_int", number)


        }

        else if (sms_chatfrag == true) {

            message = arguments?.getString("chatprofile").toString()
            number = arguments?.getInt("chatno")!!

            val intent = Intent(requireContext(), ChatMainActivity::class.java)
            intent.putExtra("msg", message)
            intent.putExtra("number", number)
            startActivity(intent)

        }

        else if (callfrag == true) {
            message = arguments?.getString("profilecall").toString()
            number = arguments?.getInt("profilepic")!!
            mobile = arguments?.getLong("profilenumber")!!
            prefutil.setBool("calls", false)

            val action = MainFragmentDirections.actionMainFragmentToScedhuleFragment(
                message,
                number,
                mobile
            )
            prefutil?.setString("profile", message)
            prefutil?.setInt("profile_int", number)

            findNavController().navigate(action)

        }

        coin = PrefUtil(requireContext()).getInt("coinValue", 0)
        binding.cointxt.text = coin.toString()

//        binding.stimcallImg.setOnClickListener {
//
//            checkGalleryPermissionAndOpenChooser()
//        }
//

        binding.coinlay.clickWithDebounce{
            startActivity(Intent(context, CoinActivity::class.java))

        }
        binding.premlayout.clickWithDebounce{
            startActivity(Intent(context, InAppPurchases::class.java))

        }
        binding.premiumiconmain.clickWithDebounce{
            startActivity(Intent(context, InAppPurchases::class.java))

        }
        binding.simulatebtn.clickWithDebounce {
            Log.d("TAG", "Counter is :$count")
            showInterstitial(
                activity = requireActivity(),
                listener = object : AdsManager.InterstitialAdListener {

                    override fun onAdClosed() {
                        startActivity(Intent(context, StmulateActivity::class.java))


                    }})


        }


        binding.fakeChat.clickWithDebounce {


            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                SEEALL_CHAT,
                "true"
            )
//            val intent = Intent(context, ChatActivity::class.java)
//            startActivity(intent)
            AdsManager.showInterstitial(true, requireActivity(), object : AdsManager.InterstitialAdListener {
                override fun onAdClosed() {
                    startActivity(Intent(context, ChatActivity::class.java))

                }

            })
        }

        binding.audio.clickWithDebounce {

            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                SEEALL_CALL,
                "true"
            )
//            val intent = Intent(context, CallActivity::class.java)
//            startActivity(intent)
            AdsManager.showInterstitial(true, requireActivity(), object : AdsManager.InterstitialAdListener {
                override fun onAdClosed() {
                    var intent = Intent(context, CallActivity::class.java)
                    intent.putExtra("audio",true)
                    startActivity(intent)

                }

            })
        }
        binding.video.clickWithDebounce {

            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                video_call,
                "true"
            )

            AdsManager.showInterstitial(true, requireActivity(), object : AdsManager.InterstitialAdListener {
                override fun onAdClosed() {
                   var intent = Intent(context, CallActivity::class.java)
                    intent.putExtra("video",true)
                    startActivity(intent)
                }

            })
        }
        binding.Shedule.clickWithDebounce {

            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                Shedule,
                "true"
            )

            AdsManager.showInterstitial(true, requireActivity(), object : AdsManager.InterstitialAdListener {
                override fun onAdClosed() {
                    startActivity(Intent(context, ActivityCustomcall::class.java))

                }

            })
        }

        binding.smsChat.clickWithDebounce {

            FirebaseCustomEvents(requireActivity()).createFirebaseEvents(
                sms,
                "true"
            )


            showInterstitial(true, requireActivity(), object : AdsManager.InterstitialAdListener {
                override fun onAdClosed() {
                    var intent = Intent(context, ActivityCustomcall::class.java)
                    intent.putExtra("sms",true)
                    PrefUtil(requireContext()).setBool("sms",true)

                    startActivity(intent)
                }

            })
        }


    }




    fun TextView.disableCopyPaste() {
        isLongClickable = false
        setTextIsSelectable(false)
        customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu): Boolean {
                return false
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {}
        }
    }

    override fun onResume() {
        super.onResume()
         btnhide=requireActivity().findViewById(R.id.bannerAdmain)
        RewardAdObjectManager.coin = PrefUtil(requireActivity()).getInt("coinValue", 0)
        binding.cointxt.text=RewardAdObjectManager.coin.toString()

        if (PrefUtil(requireContext()).getBool("is_premium", false) || !showCollape) {
            btnhide?.visibility=View.GONE
        }
       else
       {
            btnhide?.visibility = View.VISIBLE
        }
//        binding.editname.text.clear()
//        binding.editnumber.text.clear()
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)


//            if(imguri!="alexandra")
//            {
//                try {
//                    if (imguri != null) {
//                        Glide.with(this)
//                            .load(imguri.toUri())
//                            .apply(RequestOptions().override(200, 200))
//                            .centerCrop()
//                            .into(binding.stimcallImg)
//
//                    }
//                } catch (e: Exception) {
//                    Log.e("ImageLoadingError", "Error loading image: ${e.message}")
//                }
//
//            }
            bottomNavigation?.visibility = View.VISIBLE

        }catch (e : Exception){
            e.printStackTrace()
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        isImageChooserOpen = false
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        isImageChooserOpen = false
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {

            try {
                val imageUri: Uri? = data.data
//                if (imageUri != null) {
//                    Glide.with(this)
//                        .load(imageUri)
//                        .apply(RequestOptions().override(200, 200))
//                        .centerCrop()
//                        .into(binding.stimcallImg)
//                    imguri=imageUri.toString()
//                    prefutil.setString("stimulate_img", imageUri.toString())
//
//                }
            } catch (e: Exception) {
                Log.e("ImageLoadingError", "Error loading image: ${e.message}")
            }
        }
    }

    fun openImageChooser() {
        if (!isImageChooserOpen) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
            isImageChooserOpen = true
        }
    }
    fun checkGalleryPermissionAndOpenChooser() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            openImageChooser()
        } else {
            requestPermissions(arrayOf(permission), PERMISSION_REQUEST_STORAGE)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser()
            } else {
                Toast.makeText(requireContext(), "Permission required to access gallery", Toast.LENGTH_SHORT).show()
            }
        }
    }



    fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = requireActivity().currentFocus
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
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

    companion object {
        var chatfrag = false
        private const val PICK_IMAGE_REQUEST = 123
        var count = 1

    }
}