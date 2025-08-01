package com.example.baseapp.localization

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fakecall.R

class RecyclerViewAdapter(
    private val dataList: List<Item>,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private var selectedItemPosition = RecyclerView.NO_POSITION
    fun setSelectedItemPosition(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }

    private var filteredList: List<Item> = dataList
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.localizationimageView)
        val textView: TextView = itemView.findViewById(R.id.localizationTextView)
        val radiolang: RadioButton = itemView.findViewById(R.id.langbutton)




        init {
            itemView.setOnClickListener(this)
            radiolang.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (v == radiolang) {
                    // Direct click on the radio button, update selection if not already selected
                    if (selectedItemPosition != position) {
                        updateSelection(position)
                    }
                } else {
                    // Click anywhere else in the item should toggle the radio state
                    updateSelection(position)
                }
            }
        }

        fun bind(item: Item, isSelected: Boolean) {
            imageView.setImageResource(item.imageResourceId)
            textView.text = item.name

            // Set or clear the radio button based on selection
            radiolang.isChecked = isSelected
            val color = itemView.context.resources.getColor(R.color.btn_purple)
            radiolang.buttonTintList = ColorStateList.valueOf(color)
            if (isSelected) {
                textView.setTextColor(itemView.context.resources.getColor(R.color.btn_purple))
                textView.setTypeface(null, Typeface.BOLD)
            } else {
                textView.setTextColor(itemView.context.resources.getColor(R.color.black))
                textView.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredList[position]
        val isSelected = position == selectedItemPosition
        holder.bind(item, isSelected)
    }

    private fun updateSelection(newPosition: Int) {
        if (selectedItemPosition != newPosition) {
            val previousSelectedPosition = selectedItemPosition
            selectedItemPosition = newPosition
            notifyItemChanged(previousSelectedPosition)  // Update old selected item
            notifyItemChanged(selectedItemPosition)      // Update new selected item
            onItemClickListener(newPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemlistener_localization, parent, false)
        return ViewHolder(view)
    }


//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(filteredList[position])
//    }

    override fun getItemCount(): Int {
        return filteredList.size
    }





}