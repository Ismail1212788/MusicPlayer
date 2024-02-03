package com.example.myplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class Mediaplayer : AppCompatActivity() {

    var isplay=-1

    private val pickDirectoryResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val uri = data?.data
            uri?.let { processSelectedDirectory(uri) }
        }
    }

    private lateinit var songList: ArrayList<String>
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var listView: ListView
    private lateinit var bpickd: Button
    private lateinit var buttonPlay: Button
    private  lateinit var song:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mediaplayer)
        val i=Intent(applicationContext,NotificationService::class.java)
        ContextCompat.startForegroundService(this,i)
        songList = ArrayList()
        listView = findViewById(R.id.listView)
        bpickd=findViewById(R.id.buttonPickDirectory)
        buttonPlay=findViewById(R.id.buttonPlay)
        bpickd.setOnClickListener {
            openDirectoryPicker()
        }


        listView.setOnItemClickListener { _, _, position, _ ->
            buttonPlay.text="Play"
            buttonPlay.visibility=View.VISIBLE
            isplay=-1
            song = songList[position]
        }
    }

    @SuppressLint("ForegroundServiceType")
    fun performonclick(v:View){
        if(isplay==0){
            buttonPlay.text="play"
            mediaPlayer?.pause()
            isplay=1

        }else if(isplay==1){
            buttonPlay.text="pause"
            mediaPlayer?.start()
            isplay=0
        }
        else if(isplay==-1){


            buttonPlay.text="Pause"
            Handler().postDelayed({
                playMusic(song)
                mediaPlayer?.start()
            },200)
        isplay=0
        }


    }

    private fun openDirectoryPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        pickDirectoryResultLauncher.launch(intent)
    }

    @SuppressLint("Range")
    private fun processSelectedDirectory(uri: android.net.Uri) {
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA
        )
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            if(it.moveToNext()){
                buttonPlay.visibility= View.VISIBLE
            }else{
                buttonPlay.visibility= View.INVISIBLE
            }
            while (it.moveToNext()) {
                val path = it.getString(it.getColumnIndex(MediaStore.Audio.Media.DATA))
                songList.add(path)

            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, songList)
        listView.adapter = adapter


    }

    private fun playMusic(filePath: String) {


        mediaPlayer?.release()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(filePath)
        mediaPlayer?.prepare()
        mediaPlayer?.start()

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }


    //////////////////////////
    // Notification Work

    private fun startNotificationService() {
        val serviceIntent = Intent(this, NotificationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

}
