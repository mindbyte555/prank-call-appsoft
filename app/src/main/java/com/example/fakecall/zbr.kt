package com.example.fakecall

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.fakecall.databinding.ActivityZbrBinding

import com.google.android.material.button.MaterialButton

class zbr : AppCompatActivity() {
    lateinit var binding: ActivityZbrBinding
     var subject="Feedback from user"
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityZbrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.error.setOnClickListener {
            binding.suggestionconstrain.visibility= View.INVISIBLE
            binding.erroconstrain.visibility= View.VISIBLE
            binding.error.setBackgroundResource(R.drawable.selected_zbr)
            binding.error.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.suggestion.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.send.text="Send Issue"
            binding.suggestion.setBackgroundResource(R.drawable.inselback)


        }
        binding.textView16.setOnClickListener {
            subject=binding.textView16.text.toString()
            binding.textView16.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.textView22.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView17.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView18.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView19.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView20.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView16.setBackgroundResource(R.drawable.zbr2_dark)
            binding.textView17.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView18.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView19.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView20.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView22.setBackgroundResource(R.drawable.back_zbr2)
        }
        binding.textView17.setOnClickListener {
            subject=binding.textView17.text.toString()
            binding.textView17.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.textView16.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView22.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView18.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView19.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView20.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView17.setBackgroundResource(R.drawable.zbr2_dark)
            binding.textView16.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView18.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView19.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView20.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView22.setBackgroundResource(R.drawable.back_zbr2)
        }
        binding.textView18.setOnClickListener {
            subject=binding.textView18.text.toString()
            binding.textView18.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.textView16.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView17.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView22.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView19.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView20.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView18.setBackgroundResource(R.drawable.zbr2_dark)
            binding.textView17.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView16.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView19.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView20.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView22.setBackgroundResource(R.drawable.back_zbr2)
        }
        binding.textView19.setOnClickListener {
            subject=binding.textView19.text.toString()
            binding.textView19.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.textView16.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView17.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView18.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView22.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView20.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView19.setBackgroundResource(R.drawable.zbr2_dark)
            binding.textView17.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView18.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView16.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView20.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView22.setBackgroundResource(R.drawable.back_zbr2)
        }
        binding.textView20.setOnClickListener {
            subject=binding.textView20.text.toString()
            binding.textView20.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.textView16.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView17.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView18.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView19.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView22.setTextColor(ContextCompat.getColor(this, R.color.black))

            binding.textView20.setBackgroundResource(R.drawable.zbr2_dark)
            binding.textView17.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView18.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView19.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView16.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView22.setBackgroundResource(R.drawable.back_zbr2)
        }
        binding.textView22.setOnClickListener {
            subject=binding.textView22.text.toString()
            binding.textView22.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.textView16.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView17.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView18.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView19.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView20.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.textView22.setBackgroundResource(R.drawable.zbr2_dark)
            binding.textView17.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView18.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView19.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView16.setBackgroundResource(R.drawable.back_zbr2)
            binding.textView20.setBackgroundResource(R.drawable.back_zbr2)
        }
        binding.suggestion.setOnClickListener {
            binding.suggestionconstrain.visibility= View.VISIBLE
            binding.erroconstrain.visibility= View.INVISIBLE
            binding.error.setBackgroundResource(R.drawable.inselback)
            binding.error.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.send.text="send Feedback"
            binding.suggestion.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.suggestion.setBackgroundResource(R.drawable.selected_zbr)
        }
        // Set up a TextWatcher to count characters and update the textView15
       binding.editTextFeedback.addTextChangedListener(object : TextWatcher
       {
            override fun afterTextChanged(s: Editable?) {
                val length = s?.length ?: 0
                binding.textView15.text = "$length/500"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
       })
        binding.editTexterror.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(s: Editable?) {
                val length = s?.length ?: 0
                binding.textViewerroreditcount.text = "$length/500"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
binding.imageView9.setOnClickListener {
    onBackPressed()
}
        // Handle Send button click
        binding.send.setOnClickListener {
            if (binding.send.text=="Send Issue")
            {
                val feedbackText =  binding.editTexterror.text.toString()

                // Ensure feedback is not empty
                if (feedbackText.isNotEmpty())
                {
                    sendFeedbackEmail(feedbackText)
                }
                else {
                    binding.editTexterror.error = "Please enter your feedback."
                }
            }
            else
            {
                subject="Feedback from User"
                val feedbackText =  binding.editTextFeedback.text.toString()

                // Ensure feedback is not empty
                if (feedbackText.isNotEmpty())
                {
                    sendFeedbackEmail(feedbackText)
                }
                else {
                    binding.editTextFeedback.error = "Please enter your feedback."
                }
            }

        }
       }

    // Function to send feedback via email
    private fun sendFeedbackEmail(feedbackText: String) {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("support@example.com"))  // Update with your email
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, feedbackText)
        }

        // Check if an email app is available
        if (emailIntent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(emailIntent, "Send feedback using..."))
        }
    }
}
