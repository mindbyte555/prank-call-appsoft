package com.example.baseapp.ringtones

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseapp.MainActivity
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import com.example.fakecall.databinding.ActivityRingtoneBinding

class RingtoneActivity : AppCompatActivity() {
    lateinit var binding: ActivityRingtoneBinding
    private lateinit var adapter: RingtoneItemListener
    var rington=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityRingtoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val audioFiles = fetchAudioFiles(contentResolver)

        try {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            window.navigationBarColor = resources.getColor(R.color.white, theme)

            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = true
            }
        } catch (e: Exception) {
        }
        val audioFiles = fetchAllRingtones(this)

        adapter = RingtoneItemListener(audioFiles,-1,this)
        adapter.setOnItemClickListener(object : RingtoneItemListener.OnItemClickListener {
            override fun onItemClick(audioUri: Uri) {
                Log.d("Testtag", "Playing Ringtone URI: $audioUri")
                rington=true
                setRingtone(this@RingtoneActivity,audioUri)
            }


        })
        binding.ringtonerecycle.layoutManager = LinearLayoutManager(this)

        binding.ringtonerecycle.adapter=adapter

        binding.ringtonedonebtn.setOnClickListener {
            if(rington)
            {
                Toast.makeText(this,"Ringtone Saved Successfully",Toast.LENGTH_SHORT).show()
                finish()

            }
            else{
                if (PrefUtil(this).getInt("ringtone",-1)!=-1){
                    finish()
                }
                else{
                    Toast.makeText(this,"Please Select First",Toast.LENGTH_SHORT).show()

                }
            }
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)

        }
        binding.ringtonebackbtn.setOnClickListener {
            onBackPressed()

        }


    }


    fun fetchAllRingtones(context: Context): List<Pair<Long, String>> {
        val ringtoneList = mutableListOf<Pair<Long, String>>()

        val ringtoneManager = RingtoneManager(context)


        val cursor = ringtoneManager.cursor  // This retrieves a cursor containing all ringtones

        try {
            if (cursor.moveToFirst()) {
                val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)

                do {
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn)

                    val ringtoneUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                    ringtoneList.add(Pair(id, title))
                    Log.d("Testtag", "ID: $id, Title: $title, Uri: $ringtoneUri ")
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("Testtag", "Error fetching ringtones: ${e.message}")
        } finally {
            cursor.close()  // Always close the cursor to avoid memory leaks
        }

        return ringtoneList
    }
    fun setRingtone(context: Context, audioUri: Uri) {
        Log.d("Testtag", "setRingtone URI: $audioUri")

        try {
            if (audioUri != null) {
                RingtoneManager.setActualDefaultRingtoneUri(
                    context,
                    RingtoneManager.TYPE_RINGTONE,
                    audioUri
                )

            } else {
                Log.e("RingtonePlay", "audioUri is null")
            }

        } catch (e: Exception) {
            Toast.makeText(context, "Failed to set ringtone: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

}