package com.example.baseapp.consent

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import com.example.baseapp.PermissionActivity
import com.example.fakecall.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class ConsentActivity : AppCompatActivity() {
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consent)
        showBottomSheetDialog()
    }
    private fun showBottomSheetDialog() {
        dialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.dialog_permissions_new, null)
        dialog.setContentView(view)

        val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
            behavior.isHideable = false
            it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        val btnContinue = view.findViewById<TextView>(R.id.btnGrantPermissions)
        btnContinue.setOnClickListener {
//            val enterAnimation = R.anim.slide_in_right_
//            val exitAnimation = R.anim.slide_out_left_
//            val animationOptions = ActivityOptionsCompat.makeCustomAnimation(
//                this, enterAnimation, exitAnimation
//            )

            startActivity(
                Intent(this@ConsentActivity, PermissionActivity::class.java),
              )
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            dialog.dismiss()
            finish()
        }

//        val ss = SpannableString("By continuing, you agree to the Prank Call Privacy Policy which describes how the data is handled.")
//        val clickableSpan: ClickableSpan = object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                showPrivacyDialog()
//            }
//        }

//        // Set the clickable span
//        ss.setSpan(clickableSpan, 60, 74, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//        // Set the foreground color span to blue
//        val colorSpan = ForegroundColorSpan(Color.parseColor("#9386FF"))
//
//        ss.setSpan(colorSpan, 60, 74, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

//        val textView = view.findViewById<TextView>(R.id.tv_pp)
//        textView.text = ss
//        textView.movementMethod = LinkMovementMethod.getInstance()
//        textView.highlightColor = Color.TRANSPARENT // Set to transparent to avoid highlight color

        dialog.setCancelable(false)
        dialog.show()
    }
}