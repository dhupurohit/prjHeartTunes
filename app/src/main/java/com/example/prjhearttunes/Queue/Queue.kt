package com.example.prjhearttunes.Queue


import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.prjhearttunes.*
import java.lang.reflect.Field


class Queue : AppCompatActivity() {

    lateinit var queSong: ListView
    lateinit var songArray: ArrayList<SongDetails>
    lateinit var sngArtArray: ArrayList<Int>
    //    lateinit var sngTitleArray: ArrayList<String>
//    lateinit var sngDurationArray: ArrayList<String>
    //    lateinit var sngIdArray: ArrayList<Int>
//    lateinit var MediaPlayerArray: ArrayList<MediaPlayer>
    lateinit var btnQueueClose: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themePrefQA = this.getSharedPreferences(currThemePref, Context.MODE_PRIVATE)
        currTheme = themePrefQA.getInt("CurrTheme",0)
        setTheme(this)
        setContentView(R.layout.activity_queue)

//        sngTitleArray = ArrayList()
//        sngDurationArray = ArrayList()
//        sngIdArray = ArrayList()
//        MediaPlayerArray = ArrayList()
//
//        val fields: Array<Field> = com.example.prjhearttunes.R.raw::class.java.getFields()
//        fields.forEach { field ->
//
//            sngTitleArray.add(field.name)
//            sngIdArray.add(field.getInt(field))
//            MediaPlayerArray.add(
//                MediaPlayer.create(this, field.getInt(field))
//            )
//
//        }


        songArray = ArrayList()
        CurrSongArtistArray = ArrayList()
        CurrSongIDArray = ArrayList()
        CurrSongDurationArray = ArrayList()
        CurrSongTitleArray = ArrayList()
        sngArtArray = ArrayList()

        if (diffArray)
        {
            val bundle = intent.extras
            sngArtArray = bundle?.getIntegerArrayList("Curent Artist Array") as ArrayList<Int>
            Log.e("myError", "Queue Got Bundle : " + sngArtArray.toString())

            for (i in sngArtArray.indices) {


                val sngDetails = SongDetails(
                    sngTitleArray[sngArtArray[i]],
                    sngDurationArray[sngArtArray[i]].toLong(),
                    sngArtistArray[sngArtArray[i]]
                )

                songArray.add(sngDetails)

                CurrSongIDArray.add(sngIdArray[sngArtArray[i]])
                CurrSongTitleArray.add(sngTitleArray[sngArtArray[i]])
                CurrSongDurationArray.add(sngDurationArray[sngArtArray[i]])
                CurrSongArtistArray.add(sngArtistArray[sngArtArray[i]])
            }
        }
        else {
            for (i in sngTitleArray.indices) {

//            sngDurationArray.add(MediaPlayerArray[i].duration.toString())

                val sngDetails = SongDetails(
                    sngTitleArray[i],
                    sngDurationArray[i].toLong(),
                    sngArtistArray[i]
                )

                songArray.add(sngDetails)

            }

        }
        queSong = findViewById(R.id.queSong)
        queSong.choiceMode = ListView.CHOICE_MODE_NONE

        queSong.adapter = MusicAdapter(this, songArray)

        queSong.setOnItemClickListener { parent, view, position, id ->

            mp.pause()
//            seekTime = 0
            firstAttempt = true
//            sngIndex = id.toInt()

            if (diffArray)
            {
                val i = sngArtArray[id.toInt()]
                val SharedPreferences = this.getSharedPreferences(currSongPref, Context.MODE_PRIVATE)
                val editor = SharedPreferences.edit()
                editor.putInt("CurrSongId", sngIdArray[i])
                editor.putInt("CurrSongIndex", i)
                editor.putString("CurrSongName", sngTitleArray[i])
                editor.putString("CurrSongArtist", sngArtistArray[i])
                editor.putInt("CurrSongDuration", sngDurationArray[i].toInt())
                editor.apply()
                editor.commit()

                val isPlayingShPref = this.getSharedPreferences(isSongPlaying, Context.MODE_PRIVATE)

                val intent = Intent(this, Player::class.java)
                val bundle = Bundle()
                bundle.putIntegerArrayList("Curent Artist Array",sngArtArray)
                Log.e("myError", "Queue Passing Bundle : " + sngArtArray.toString())
                intent.putExtras(bundle)
                startActivity(intent)
                val e = isPlayingShPref.edit()
                e.putBoolean("IsPlaying", false)
                e.apply()
                e.commit()

            }
            else {

                val SharedPreferences =
                    this.getSharedPreferences(currSongPref, Context.MODE_PRIVATE)
                val editor = SharedPreferences.edit()
                editor.putInt("CurrSongId", sngIdArray[id.toInt()])
                editor.putInt("CurrSongIndex", position)
                editor.putString("CurrSongName", sngTitleArray[id.toInt()])
                editor.putString("CurrSongArtist", sngArtistArray[id.toInt()])
                editor.putInt("CurrSongDuration", sngDurationArray[id.toInt()].toInt())
                editor.apply()
                editor.commit()

                val isPlayingShPref = this.getSharedPreferences(isSongPlaying, Context.MODE_PRIVATE)

                val bundle = intent.extras
                if (bundle != null) {
                    if (bundle.getBoolean("IsDriveMode")) {
                        val intent = Intent(this, DriveMode::class.java)
                        startActivity(intent)

                        val editor = isPlayingShPref.edit()
                        editor.putBoolean("IsPlaying", false)
                        editor.apply()
                        editor.commit()
                    }
                } else {
                    val intent = Intent(this, Player::class.java)
                    startActivity(intent)

                    val editor = isPlayingShPref.edit()
                    editor.putBoolean("IsPlaying", false)
                    editor.apply()
                    editor.commit()
                }

            }
        }

        btnQueueClose = findViewById(R.id.btnCloseQueue)
        btnQueueClose.setOnClickListener {
            if (isTaskRoot) {
                startActivity(Intent(this@Queue, MainActivity::class.java))
                finish()
            } else {
                super.onBackPressed()
            }
        }

    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            startActivity(Intent(this@Queue, MainActivity::class.java))
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
