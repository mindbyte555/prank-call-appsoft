package com.example.baseapp.fragments.chat

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.baseapp.adsManager.AdsManager
import com.example.baseapp.alarmManagers.AlarmReceiver
import com.example.baseapp.alarmManagers.VideoAlarmReciver
import com.example.baseapp.fragments.chat.answer.AnswerAdapter
import com.example.baseapp.fragments.chat.question.QuestionAdapter
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentChatBinding
import com.google.android.gms.ads.AdView
import java.util.concurrent.atomic.AtomicBoolean


class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val answers: MutableList<String> = mutableListOf()
    private val questionchat: MutableList<String> = mutableListOf()
    val args: ChatFragmentArgs by navArgs()
    var message = ""
    var number = 0
    lateinit var answerAdapter: AnswerAdapter
    lateinit var prefutil: PrefUtil
    var adView: AdView? = null
    private val initialLayoutComplete = AtomicBoolean(false)
    var isConnected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)
            val mainconst = requireActivity().findViewById<ConstraintLayout>(R.id.mainconsy)
            mainconst.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            val textprank = requireActivity().findViewById<TextView>(R.id.mainhead)
            val drawerimg = requireActivity().findViewById<ImageView>(R.id.drawerimg)
            val premimg = requireActivity().findViewById<ImageView>(R.id.premiumiconmain)
            textprank.visibility = View.GONE
            drawerimg.visibility = View.GONE
            premimg.visibility = View.GONE

            val bottomNavigationImage = requireActivity().findViewById<ImageView>(R.id.homeImg)
            bottomNavigation?.visibility = View.GONE
            bottomNavigationImage?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isConnected = isInternetAvailable()


        prefutil = PrefUtil(requireContext())

        setRetainInstance(true)
        if (answers.isEmpty()) {
            answers.add("Hi")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val questions = listOf(
            "What are your hobbies and interests?",
            "Have you traveled to any interesting\nplaces recently?",
            " Do you have any pets?",
            "What's your favorite outdoor activity?"
        )
        binding = FragmentChatBinding.inflate(inflater, container, false)
        answerAdapter = AnswerAdapter(answers, questionchat)

        binding.backbtnchat.setOnClickListener {
            val action = ChatFragmentDirections.actionChatFragmentToMainFragment()
            findNavController().navigate(action)

        }
        binding.questionlistrecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.chatrecycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = QuestionAdapter(questions) { question, it ->


            questionchat.add(question)
            if (it == 0) {
                answers.add("I enjoy reading books, hiking in nature, and\ntrying out new recipes in the kitchen.")
            } else if (it == 1) {
                answers.add("Yes, I recently visited Japan and\nwas fascinated by the rich culture\n,delicious food, and stunning landscapes")
            } else if (it == 2) {
                answers.add("Yes, I have a dog named Max.")
            } else if (it == 3) {
                answers.add("Hiking in the mountains.")
            }


            answerAdapter.notifyDataSetChanged()
        }

        binding.chatrecycler.adapter = answerAdapter
        binding.questionlistrecycler.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackPressHandler()

//            binding.bannerAd.viewTreeObserver.addOnGlobalLayoutListener {
//        if (!initialLayoutComplete.getAndSet(true)) {
//        AdsManager.loadBannerAd(binding.bannerAd,
//            requireActivity(),
//            object : AdsManager.AdmobBannerAdListener {
//                override fun onAdFailed() {
//                }
//                override fun onAdLoaded() {
//                }
//            })?.let {
//            adView = it
//            }
//        }
//            }


        message = args.chatprofile
        number = args.chatpic
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                val action = ChatFragmentDirections.actionChatFragmentToMainFragment()
//                findNavController().navigate(action)
//            }
//        })
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                findNavController().popBackStack(R.id.mainFragment, false)
//
//            }
//        })


//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
////            val action = ChatFragmentDirections.actionChatFragmentToMainFragment()
////            findNavController().navigate(action)
//        }





        binding.profilename.text = message


        binding.callimg.setOnClickListener {
            if (number == 1) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 1)
                val intent = Intent(requireContext(), AlarmReceiver::class.java)
                requireContext().sendBroadcast(intent)
            } else if (number == 2) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 2)
                val intent = Intent(requireContext(), AlarmReceiver::class.java)
                requireContext().sendBroadcast(intent)
            } else if (number == 3) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 3)
                val intent = Intent(requireContext(), AlarmReceiver::class.java)
                requireContext().sendBroadcast(intent)
            } else if (number == 4) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 4)
                val intent = Intent(requireContext(), AlarmReceiver::class.java)
                requireContext().sendBroadcast(intent)

            } else if (number == 5) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 5)
                val intent = Intent(requireContext(), AlarmReceiver::class.java)
                requireContext().sendBroadcast(intent)
            } else if (number == 6) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 6)
                val intent = Intent(requireContext(), AlarmReceiver::class.java)
                requireContext().sendBroadcast(intent)
            } else if (number == 7) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 7)
                val intent = Intent(requireContext(), AlarmReceiver::class.java)
                requireContext().sendBroadcast(intent)
            } else if (number == 8) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 8)
                val intent = Intent(requireContext(), AlarmReceiver::class.java)
                requireContext().sendBroadcast(intent)
            }


        }
        binding.videoimg.setOnClickListener {
            if (number == 1) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 1)
                val intent = Intent(requireContext(), VideoAlarmReciver::class.java)
                requireContext().sendBroadcast(intent)
            } else if (number == 2) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 2)
                val intent = Intent(requireContext(), VideoAlarmReciver::class.java)
                requireContext().sendBroadcast(intent)
            } else if (number == 3) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 3)
                val intent = Intent(requireContext(), VideoAlarmReciver::class.java)
                requireContext().sendBroadcast(intent)
            } else if (number == 4) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 4)
                val intent = Intent(requireContext(), VideoAlarmReciver::class.java)
                requireContext().sendBroadcast(intent)
            } else if (number == 5) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 5)
                val intent = Intent(requireContext(), VideoAlarmReciver::class.java)
                requireContext().sendBroadcast(intent)
            } else if (number == 6) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 6)
                val intent = Intent(requireContext(), VideoAlarmReciver::class.java)
                requireContext().sendBroadcast(intent)
            } else if (number == 7) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 7)
                val intent = Intent(requireContext(), VideoAlarmReciver::class.java)
                requireContext().sendBroadcast(intent)
            } else if (number == 8) {
                prefutil.setString("profile", message)
                prefutil.setInt("profile_int", 8)
                val intent = Intent(requireContext(), VideoAlarmReciver::class.java)
                requireContext().sendBroadcast(intent)
            }
        }

        if (number == 1) {
            Glide.with(this)
                .load(R.drawable.ic_alex)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagechat)
        } else if (number == 2) {
            Glide.with(this)
                .load(R.drawable.ic_messi)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagechat)
        } else if (number == 3) {
            Glide.with(this)
                .load(R.drawable.ic_emma)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagechat)
        } else if (number == 4) {
            Glide.with(this)
                .load(R.drawable.ic_ron)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagechat)
        }

        if (number == 5) {
            Glide.with(this)
                .load(R.drawable.ic_ang)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagechat)
        } else if (number == 6) {
            Glide.with(this)
                .load(R.drawable.ic_pry)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagechat)
        } else if (number == 7) {
            Glide.with(this)
                .load(R.drawable.ic_tom)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagechat)
        } else if (number == 8) {
            Glide.with(this)
                .load(R.drawable.ic_keenu)
                .apply(RequestOptions().override(200, 200))
                .centerCrop()
                .into(binding.imagechat)
        }

    }

    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }
    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = ChatFragmentDirections.actionChatFragmentToMainFragment()
                findNavController().navigate(action)
            }
        })
    }

    override fun onResume() {
//        adView?.let {
//            it.resume()
//            Log.e("TestTag", "onResume: called", )
//        }
        super.onResume()
        try {
            val bottomNavigation = requireActivity().findViewById<ConstraintLayout>(R.id.imageView5)
            val bottomNavigationImage = requireActivity().findViewById<ImageView>(R.id.homeImg)
            val textprank = requireActivity().findViewById<TextView>(R.id.mainhead)
            val drawerimg = requireActivity().findViewById<ImageView>(R.id.drawerimg)
            val premimg = requireActivity().findViewById<ImageView>(R.id.premiumiconmain)
            textprank.visibility = View.GONE
            drawerimg.visibility = View.GONE
            premimg.visibility = View.GONE
            bottomNavigation?.visibility = View.GONE
            bottomNavigationImage?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("TestTag","OnPause")

//        adView?.let { it.pause() }
//        adView?.let {
//            it.destroy()
//            val parent = it.parent as ViewGroup?
//            parent?.removeView(adView)
//        }
//        adView=null
    }

//    override fun onDestroy() {
//        Log.d("TestTag","Ondestroy")
//        adView?.let {
//            it.destroy()
//            val parent = it.parent as ViewGroup?
//            parent?.removeView(adView)
//        }
//        adView=null
//        super.onDestroy()
//    }

}
