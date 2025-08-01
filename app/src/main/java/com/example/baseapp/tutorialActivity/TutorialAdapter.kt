package com.example.baseapp.tutorialActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.fakecall.R

class TutorialAdapter (private val items: List<TutorialDataClass>, private val context: Context) : PagerAdapter() {

    override fun getCount(): Int = items.size

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.tutorial_item_listener, container, false)

        val imageView = view.findViewById<ImageView>(R.id.addtowidgetimageviewactivity)
        val textView = view.findViewById<TextView>(R.id.addtowidgettextviewactivity)

        val item = items[position]
        imageView.setImageResource(item.imageResId)
        textView.text = item.text

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}