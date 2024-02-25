package com.example.prjhearttunes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.prjhearttunes.R.*
import com.google.android.material.navigation.NavigationView
import java.lang.reflect.Field
import kotlin.math.log


class Library : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var lstSongs: ListView
    lateinit var songArray: ArrayList<SongDetails>

    @SuppressLint("InlinedApi", "Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themePrefMA = this.getSharedPreferences(currThemePref, Context.MODE_PRIVATE)
        currTheme = themePrefMA.getInt("CurrTheme",0)
        setTheme(this)
        setContentView(layout.activity_library)

        drawerLayout = findViewById(id.drawer_layout)
        navigationView = findViewById(id.nav_view)
        toolbar = findViewById(id.toolbar)
        setSupportActionBar(toolbar)


//        sngTitl/ArrayList()

        navigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            string.navigation_drawer_open,
            string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(id.nav_home)


        songArray = ArrayList()

        for (i in sngTitleArray.indices) {


//            sngDurationArray.add(MediaPlayerArray[i].duration.toString())

            val sngDetails = SongDetails(
                sngTitleArray[i],
                sngDurationArray[i].toLong(),
                sngArtistArray[i]
            )

            songArray.add(sngDetails)

        }


        lstSongs = findViewById(id.lstSong)


        lstSongs.adapter = MusicAdapter(this, songArray)


        lstSongs.setOnItemClickListener { parent, view, position, id ->

            mp.pause()
//            seekTime = 0
            firstAttempt = true
//            sngIndex = id.toInt()

            val SharedPreferences = this.getSharedPreferences(currSongPref, Context.MODE_PRIVATE)
            val editor = SharedPreferences.edit()
            editor.putInt("CurrSongId", sngIdArray[id.toInt()])
            editor.putInt("CurrSongIndex", position)
            editor.putString("CurrSongName", sngTitleArray[id.toInt()])
            editor.putString("CurrSongArtist", sngArtistArray[id.toInt()])
            editor.putInt("CurrSongDuration", sngDurationArray[id.toInt()].toInt())
            editor.apply()
            editor.commit()

            val isPlayingShPref = this.getSharedPreferences(isSongPlaying, Context.MODE_PRIVATE)

                val intent = Intent(this, Player::class.java)
                startActivity(intent)
                val e = isPlayingShPref.edit()
                e.putBoolean("IsPlaying", false)
                e.apply()
                e.commit()


        }

    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val drawerLayout: DrawerLayout = findViewById(id.drawer_layout)
        when (menuItem.itemId) {
            id.nav_home -> {
                super.onBackPressed()
            }
            id.nav_themes -> {
                val intent = Intent(this, Themes::class.java)
                startActivity(intent)
            }
            id.nav_drive -> {
                val intent = Intent(this, DriveMode::class.java)
                startActivity(intent)
            }
            id.nav_timer -> {
                val intent = Intent(this, Timer::class.java)
                startActivity(intent)
            }
            id.nav_share -> Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()

            id.nav_quit -> this.finishAffinity()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (isTaskRoot) {
                startActivity(Intent(this@Library, MainActivity::class.java))
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }


}
