package com.example.baseapp.fragments.CustomFragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.baseapp.fragments.AudioCallFragment
import com.example.baseapp.fragments.CustomFragment.customCallFragment.CustomCallFragment
import com.example.baseapp.fragments.CustomFragment.customVideoFragment.CustomVideoCallFragment
import com.example.baseapp.fragments.VideoCall.VideoCallFragment
import com.example.baseapp.tabadapter.TabAdapter
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.FragmentCustomBinding


class CustomFragment : Fragment() {

    private lateinit var binding: FragmentCustomBinding

    var message=""
    var number=0
    lateinit var prefutil: PrefUtil
    private var isImageChooserOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefutil= PrefUtil(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomBinding.inflate(inflater, container, false)
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
//        setRetainInstance(true)

//        setupTabs()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {}
        binding.customeeditname.setOnLongClickListener(View.OnLongClickListener { //  Do Something or Don't
            true
        })

        binding.imagecall.setOnClickListener {

            openImageChooser()
        }

        prefutil.setString("custm_profile",binding.customeeditname.text.toString())

        prefutil.setString("custm_num",binding.customeeditnumber.text.toString())
        var textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                prefutil.setString("custm_profile",binding.customeeditname.text.toString())
            }
        }

        var textWatchernum: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // this function is called when text is edited
            }

            override fun afterTextChanged(s: Editable) {
                prefutil.setString("custm_num",binding.customeeditnumber.text.toString())
            }
        }
        binding.customeeditname.addTextChangedListener(textWatcher)
        binding.customeeditnumber.addTextChangedListener(textWatchernum)





    }
    fun setupTabs() {

        val tabadapter = TabAdapter(childFragmentManager)






        tabadapter.addFragment(CustomVideoCallFragment(), getString(R.string.video_call))
        tabadapter.addFragment(CustomCallFragment(),  getString(R.string.call))
        tabadapter.addFragment(CustomSmsfragment(),  getString(R.string.sms))



        binding.callschduleviewpager.offscreenPageLimit = 3
        binding.callschduleviewpager.adapter = tabadapter




        binding.scheduletabLayout.setupWithViewPager( binding.callschduleviewpager)
        binding.scheduletabLayout.getTabAt(0)
        binding.scheduletabLayout.getTabAt(1)
        binding.scheduletabLayout.getTabAt(2)



    }

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    isImageChooserOpen = false
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {

        try {
            val imageUri: Uri? = data.data
            if (imageUri != null) {
                Glide.with(this)
                    .load(imageUri)
                    .apply(RequestOptions().override(200, 200))
                    .centerCrop()
                    .into(binding.imagecall)
                prefutil.setString("profile_img", imageUri.toString())
            }
        } catch (e: Exception) {
            Log.e("ImageLoadingError", "Error loading image: ${e.message}")
        }
    }
}

    fun openImageChooser() {
        if (!isImageChooserOpen) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*" // Set MIME type to image
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
            isImageChooserOpen = true // Set the flag to true
        }
    }
    companion object {
        private const val PICK_IMAGE_REQUEST = 123
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

    override fun onDestroy() {
        super.onDestroy()
        isImageChooserOpen = false
    }
}