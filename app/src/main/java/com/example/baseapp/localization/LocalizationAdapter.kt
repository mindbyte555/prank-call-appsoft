package com.example.baseapp.localization

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fakecall.R

class LocalizationAdapter  (private var stringList: List<String>,private val imgList: List<Int>,
private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<LocalizationAdapter.ViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION
    fun setSelectedItemPosition(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged() // Notify the adapter to refresh all items
    }
    fun filterList(filteredItems: List<String>) {
        // Assume your adapter uses a List<String> for simplicity; adjust as needed
        stringList = filteredItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemlistener_localization, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stringList[position], imgList[position],position)
    }

    override fun getItemCount(): Int {
        return stringList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val localtextView: TextView = itemView.findViewById(R.id.localizationTextView)
        private val localimageview: ImageView = itemView.findViewById(R.id.localizationimageView)


        init {
            itemView.setOnClickListener(this)
        }

        fun bind(text: String, img:Int,position: Int) {
            localtextView.text = text
            localimageview.setImageResource(img)

            if (position == selectedItemPosition) {
                localtextView.setTextColor(itemView.context.resources.getColor(R.color.btn_purple))
            } else {
                localtextView.setTextColor(itemView.context.resources.getColor(R.color.black))
            }
        }

        override fun onClick(v: View?) {
            val previousSelectedPosition = selectedItemPosition
            selectedItemPosition = adapterPosition
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedItemPosition)

            // Call the interface method to send the clicked item position to activity
            onItemClickListener(adapterPosition)

        }

    }



}