package com.example.prjhearttunes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.prjhearttunes.Queue.Queue
import com.google.android.material.navigation.NavigationView

class DriveMode : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var imgQueue: ImageView
    lateinit var imgPlay: ImageView
    lateinit var imgNext: ImageView
    lateinit var imgPrev: ImageView
    lateinit var imgRep: ImageView
    lateinit var imgFav: ImageView
    lateinit var txtTitle: TextView
    lateinit var txtArtist: TextView
    lateinit var txtEndTime: TextView
    lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themePrefMA = this.getSharedPreferences(currThemePref, Context.MODE_PRIVATE)
        currTheme = themePrefMA.getInt("CurrTheme",0)
        setTheme(this)
        setContentView(R.layout.activity_drive_mode)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        navigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_drive)


        imgQueue = findViewById(R.id.imgQueue)
        imgPlay = findViewById(R.id.imgPlay)
        imgNext = findViewById(R.id.imgNext)
        imgPrev = findViewById(R.id.imgPrev)
        imgRep = findViewById(R.id.imgRep)
        imgFav = findViewById(R.id.imgFav)
        imgNext = findViewById(R.id.imgNext)
        imgNext = findViewById(R.id.imgNext)
        txtEndTime = findViewById(R.id.txtEndTime)
        txtTitle = findViewById(R.id.txtSongName)
        txtArtist = findViewById(R.id.txtArtistName)
        seekBar = findViewById(R.id.seekBar)


        val currSongPref = this.getSharedPreferences(currSongPref, Context.MODE_PRIVATE)
//        mpId = currSongPref.getInt("CurrSongId",0)
        var mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
//        var title = currSongPref.getString("CurrSongName","")
//        txtTitle.text = title?.let { formatSongName(it) }
//        txtArtist.text = currSongPref.getString("CurrSongArtist","")
//        var dur = currSongPref.getInt("CurrSongDuration",0).toLong()
//        txtEndTime.text = formatDuration(dur)

        val isPlayingShPref = this.getSharedPreferences(isSongPlaying, Context.MODE_PRIVATE)

//        if (isPlayingShPref.getBoolean("IsPlaying",false))
//        {
//            imgPlay.setImageResource(R.drawable.ic_pause)
//        }
//        else
//        {
//            imgPlay.setImageResource(R.drawable.ic_play_)
//        }

        seekBar.progress = 0
        val handler = Handler()
        initializeSeekBar(seekBar, handler)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mp.seekTo(progress)
                }
            }

        })

        val sngDetails = SongDetails(
            txtTitle.text.toString(),
            sngDurationArray[sngIndex].toLong(),
            txtArtist.text.toString()
        )

        handler.postDelayed(object : Runnable {
            override fun run() {
                if (isPlayingShPref.getBoolean("IsPlaying", false)) {
                    imgPlay.setImageResource(R.drawable.ic_pause)
                } else {
                    imgPlay.setImageResource(R.drawable.ic_play_)
                }


                if (seekBar.progress == seekBar.max)
                {
                    if (favList.contains(sngDetails)) {
                        diffArray = true

                        if (mp.isLooping) {
                            var isIt = isPlayingShPref.getBoolean("IsPlaying", false)

                            isIt = playSong(this@DriveMode, mpId, imgPlay, isIt)

                            val editor = isPlayingShPref.edit()
                            editor.putBoolean("IsPlaying", isIt)
                            editor.apply()
//                        editor.commit()
                        } else {
                            playNextSong(this@DriveMode, mpId, true)
                            mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
                        }
                    } else {
                        diffArray = false
                        if (mp.isLooping) {
                            var isIt = isPlayingShPref.getBoolean("IsPlaying", false)

                            isIt = playSong(this@DriveMode, mpId, imgPlay, isIt)

                            val editor = isPlayingShPref.edit()
                            editor.putBoolean("IsPlaying", isIt)
                            editor.apply()
//                        editor.commit()
                        } else {
                            playNextSong(this@DriveMode, mpId, false)
                            mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
                        }
                    }
                }
                handler.postDelayed(this, 1000)
            }
        }, 0)



        imgQueue.setOnClickListener {
            val intent = Intent(this, Queue::class.java)
            val bundle = Bundle()
            bundle.putBoolean("IsDriveMode", true)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        imgPlay.setOnClickListener {
            var isIt = isPlayingShPref.getBoolean("IsPlaying", false)

            isIt = playSong(this, mpId, imgPlay, isIt)

            val editor = isPlayingShPref.edit()
            editor.putBoolean("IsPlaying", isIt)
            editor.apply()
            editor.commit()
        }

        imgPrev.setOnClickListener {
            playPrevSong(this, mpId)
            mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
        }

        imgNext.setOnClickListener {
            if (mp.isLooping) {
                var isIt = isPlayingShPref.getBoolean("IsPlaying", false)

                isIt = playSong(this@DriveMode, mpId, imgPlay, isIt)

                val editor = isPlayingShPref.edit()
                editor.putBoolean("IsPlaying", isIt)
                editor.apply()
//                        editor.commit()
            } else {
                playNextSong(this@DriveMode, mpId, false)
                mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
            }
        }

        imgFav.setOnClickListener {
            if (favList.contains(sngDetails)) {
                // is in favourite

                favSongIDArray.remove(mpId)
                favSongTitleArray.remove(txtTitle.text.toString())
                favSongDurationArray.remove(sngDurationArray[sngIndex])
                favSongArtistArray.remove(txtArtist.text.toString())

                favList.remove(sngDetails)
                imgFav.setImageResource(R.drawable.ic_favorite_borde)
            } else {
                // is not in favourite

                favSongIDArray.add(mpId)
                favSongTitleArray.add(txtTitle.text.toString())
                favSongDurationArray.add(sngDurationArray[sngIndex])
                favSongArtistArray.add(txtArtist.text.toString())

                favList.add(sngDetails)
                imgFav.setImageResource(R.drawable.ic_favorite_fill)
            }
        }

        imgRep.setOnClickListener {
            repeat(imgRep)
        }

    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        when (menuItem.getItemId()) {
            R.id.nav_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_themes -> {
                val intent = Intent(this, Themes::class.java)
                startActivity(intent)
            }
            R.id.nav_drive -> {

            }
            R.id.nav_timer -> {
                val intent = Intent(this, Timer::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()

            R.id.nav_quit -> this.finishAffinity()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (isTaskRoot) {
                startActivity(Intent(this@DriveMode, MainActivity::class.java))
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }
}
