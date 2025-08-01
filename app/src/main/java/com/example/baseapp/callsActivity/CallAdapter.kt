package com.example.baseapp.callsActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fakecall.R

class CallAdapter (
    private val dataList: List<CallItem>,

    private val onItemClickListener: (Int) -> Unit

) : RecyclerView.Adapter<CallAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemlistener_call, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatdata = dataList[position]
        holder.imageView.setImageResource(chatdata.imageResourceId)
        holder.name.text = chatdata.name
        holder.number.text=chatdata.number.toString()
        holder.itemView.setOnClickListener {
            onItemClickListener(position)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.picture)
        val name: TextView = itemView.findViewById(R.id.name)
        val number: TextView = itemView.findViewById(R.id.phone)

    }
}