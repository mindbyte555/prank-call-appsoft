package com.example.baseapp.fragments.sms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.google.android.material.card.MaterialCardView

class ThemeSmsAdapter (var context: Context,     private val itemList: List<SmsData>, private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<ThemeSmsAdapter.ViewHolder>() {

    val prefutil: PrefUtil = PrefUtil(context)
    var pos=prefutil.getInt("smstheme",0)

    private var selectedItemPosition: Int = pos

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.themeimg)
        val textchat: TextView = view.findViewById(R.id.chatcat)
        val materialchat: MaterialCardView = view.findViewById(R.id.materialchat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.smsadapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.imageView)
        holder.textchat.text=item.name


        if (position == selectedItemPosition) {

            holder.materialchat.strokeColor = ContextCompat.getColor(context, R.color.tab_selected)

        } else {
            holder.materialchat.strokeColor = ContextCompat.getColor(context, R.color.white)
        }

        holder.itemView.setOnClickListener {
            val previousSelectedItemPosition = selectedItemPosition
            selectedItemPosition = holder.adapterPosition
            notifyItemChanged(previousSelectedItemPosition)
            notifyItemChanged(selectedItemPosition)
            onItemClick(holder.adapterPosition)

        }
    }





    override fun getItemCount() = itemList.size
}

