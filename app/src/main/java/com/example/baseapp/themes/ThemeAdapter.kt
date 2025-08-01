package com.example.baseapp.themes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R


class ThemeAdapter(var context: Context,private val itemList: List<Int>,    private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<ThemeAdapter.ViewHolder>() {

    val prefutil: PrefUtil = PrefUtil(context)
    var pos=prefutil.getInt("lasttheme",0)

    private var selectedItemPosition: Int = pos

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.themeimg)
        val blurimageView: ImageView = view.findViewById(R.id.themeblurimg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.theme_itemlistener, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        Glide.with(holder.itemView.context)
            .load(item)
            .into(holder.imageView)

        if (position == selectedItemPosition) {
            holder.blurimageView.visibility = View.VISIBLE
//            prefutil.setInt("lasttheme",position)
        } else {
            holder.blurimageView.visibility = View.GONE
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

