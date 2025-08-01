package com.example.baseapp.chatfragmentActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivityChatFragmentContainerBinding

class ChatFragmentContainerActivity : AppCompatActivity() {
    val binding: ActivityChatFragmentContainerBinding by lazy { ActivityChatFragmentContainerBinding.inflate(layoutInflater) }
//    val navController by lazy {
//navController(R.id.frame_chatContainer)
//
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val message = intent.getStringExtra("CHAT_PROFILE")
        val number = intent.getIntExtra("CHAT_PIC", 0)

        // Create a bundle and pass the values to it
        val bundle = Bundle().apply {
            putString("CHAT_PROFILE", message)
            putInt("CHAT_PIC", number)
        }

        // Set the bundle as arguments for the fragment
//        navController.navigate(R.id.chatFragment2, bundle)

    }
    fun FragmentActivity.navController(navControllerr: Int): NavController {
        return (supportFragmentManager.findFragmentById(navControllerr) as NavHostFragment).navController
    }
}