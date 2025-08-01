package com.example.baseapp.chatActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.baseapp.localization.Item
import com.example.fakecall.R

class ChatAdapter (
    private val dataList: List<ChatListItem>,

    private val onItemClickListener: (Int) -> Unit

) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemlistener_chat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatdata = dataList[position]
        holder.imageView.setImageResource(chatdata.imageResourceId)
        holder.textView.text = chatdata.name
        holder.itemView.setOnClickListener {
            onItemClickListener(position)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.fakechatimg)
        val textView: TextView = itemView.findViewById(R.id.fakechattv)
    }
}