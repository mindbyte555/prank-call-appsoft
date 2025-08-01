package com.example.baseapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.baseapp.Application.MyApplication.Companion.noadshow
import com.example.baseapp.inAppPurchases.InAppPurchases
import com.example.baseapp.localization.LocalizationActivity
import com.example.baseapp.ringtones.RingtoneActivity
import com.example.baseapp.themes.ThemeActivity
import com.example.baseapp.utils.Constants.clickWithDebounce
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentSettingBinding
import com.example.iwidgets.Utils.AppInfoUtils


class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    lateinit var prefutil: PrefUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefutil= PrefUtil(requireContext())


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        try {

            val textprank = requireActivity().findViewById<TextView>(R.id.mainhead)
            val drawerimg = requireActivity().findViewById<ImageView>(R.id.drawerimg)
            val premimg = requireActivity().findViewById<ImageView>(R.id.premiumiconmain)
            textprank.visibility=View.GONE
            drawerimg.visibility=View.GONE
            premimg.visibility=View.GONE

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.ringtone.clickWithDebounce {
            val intent = Intent(context, RingtoneActivity::class.java)
            context?.startActivity(intent)
        }

        binding.calltheme.clickWithDebounce {
            val intent = Intent(context, ThemeActivity::class.java)
            context?.startActivity(intent)
        }
        binding.premiumiconmain.clickWithDebounce {
            val intent = Intent(context, InAppPurchases::class.java)
            context?.startActivity(intent)
        }
        binding.upgrdnow.clickWithDebounce {
            noadshow =false

            val intent = Intent(context, InAppPurchases::class.java)
            context?.startActivity(intent)
        }
        binding.applang.clickWithDebounce {
            val intent = Intent(context, LocalizationActivity::class.java)
            context?.startActivity(intent)

        }

        binding.rateus.clickWithDebounce {
            noadshow=false

            AppInfoUtils(requireActivity()).rateApp()
        }

        binding.share.clickWithDebounce {
            noadshow=false

            AppInfoUtils(requireActivity()).shareApp()

        }
        binding.consent.clickWithDebounce {
            noadshow=false

            Toast.makeText(requireContext(),"Consent Clicked",Toast.LENGTH_SHORT).show()

        }
        binding.priv.clickWithDebounce {
            noadshow=false

            AppInfoUtils(requireActivity()).openPrivacy()

        }
    }

    override fun onResume() {
        super.onResume()
        try {

            val textprank = requireActivity().findViewById<TextView>(R.id.mainhead)
            val drawerimg = requireActivity().findViewById<ImageView>(R.id.drawerimg)
            val premimg = requireActivity().findViewById<ImageView>(R.id.premiumiconmain)
            textprank.visibility=View.GONE
            drawerimg.visibility=View.GONE
            premimg.visibility=View.GONE

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




}