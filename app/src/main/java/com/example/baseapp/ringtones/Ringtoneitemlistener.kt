package com.example.baseapp.ringtones

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import java.io.File



class RingtoneItemListener(private val audioFiles: List<Pair<Long, String>>,
                           private var selectedPosition: Int = -1,context: Context) :
    RecyclerView.Adapter<RingtoneItemListener.AudioFileViewHolder>() {

    val prefutil: PrefUtil = PrefUtil(context)


    interface OnItemClickListener {
        fun onItemClick(audioUri: Uri)
    }
    init {

        if (prefutil.getInt("ringtone",-1)!=-1)
        {
            selectedPosition=prefutil.getInt("ringtone",-1)
        }
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


    inner class AudioFileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val fileNameTextView: TextView = itemView.findViewById(R.id.textViewFileName)
        val radioButton: RadioButton = itemView.findViewById(R.id.ringbutton)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val audioUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                        audioFiles[position].first
                    )
                    listener?.onItemClick(audioUri)
                    // Update selected position and notify adapter of the change
                    updateSelectedPosition(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioFileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemlistener_ringtone, parent, false)
        return AudioFileViewHolder(view)
    }


override fun onBindViewHolder(holder: AudioFileViewHolder, position: Int) {
    val (id, fileName) = audioFiles[position]
    holder.fileNameTextView.text = fileName

    // Set the RadioButton's checked state based on selectedPosition
    holder.radioButton.isChecked = selectedPosition == position

    // Enable the RadioButton only if it's the selected item
    holder.radioButton.isEnabled = selectedPosition == position

    // Set click listener to update selected position
    holder.itemView.setOnClickListener {
        val adapterPosition = holder.adapterPosition
        if (adapterPosition != RecyclerView.NO_POSITION) {
            updateSelectedPosition(adapterPosition)
            val audioUri = ContentUris.withAppendedId(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, id)



            listener?.onItemClick(audioUri)
        }
    }
}
    override fun getItemCount(): Int {
        // Return the number of items in the list
        return audioFiles.size
    }

    private fun updateSelectedPosition(position: Int) {
        if (selectedPosition != position) {
            val previousSelectedPosition = selectedPosition
            selectedPosition = position
            prefutil.setInt("ringtone",position)

            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
    }
}