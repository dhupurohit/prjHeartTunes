package com.example.prjhearttunes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ArtistSongs : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var lstSongs: ListView
    lateinit var songArray: ArrayList<SongDetails>
    lateinit var sngArtArray: ArrayList<Int>
    lateinit var artistName: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themePrefMA = this.getSharedPreferences(currThemePref, Context.MODE_PRIVATE)
        currTheme = themePrefMA.getInt("CurrTheme",0)
        setTheme(this)
        setContentView(R.layout.activity_artist_songs)

        drawerLayout=findViewById(R.id.drawer_layout)
        navigationView=findViewById(R.id.nav_view)
        toolbar=findViewById(R.id.toolbar)
        artistName = findViewById(R.id.hdPlaylistName)
        Log.e("myError", "After find view by id")

        setSupportActionBar(toolbar)

        navigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close  )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_home)


        songArray = ArrayList()
        CurrSongArtistArray = ArrayList()
        CurrSongIDArray = ArrayList()
        CurrSongDurationArray = ArrayList()
        CurrSongTitleArray = ArrayList()
        sngArtArray = ArrayList()
//        Log.e("myError", "After ArrayList")

        val bundle = intent.extras
        sngArtArray = bundle?.getIntegerArrayList("Curent Artist Array") as ArrayList<Int>
//        Log.e("myError", "After CurrSongArtistArray")
        artistName.text = intent.getStringExtra("ArtistName")
//        Log.e("myError", "After ArtistName")

        for (i in sngArtArray.indices) {
//            Log.e("myError", "Inside For loop")
//            sngDurationArray.add(MediaPlayerArray[i].duration.toString())

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

        lstSongs = findViewById(R.id.artistsng)

        lstSongs.adapter = MusicAdapter(this, songArray)

        lstSongs.setOnItemClickListener { parent, view, position, id ->

//            Toast.makeText(this@ArtistSongs, "id : " + id , Toast.LENGTH_SHORT).show()
//            Toast.makeText(this@ArtistSongs, "position : " + position , Toast.LENGTH_SHORT).show()

            mp.pause()
//            seekTime = 0
            firstAttempt = true
//            sngIndex = id.toInt()
            diffArray = true

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
            Log.e("myError", "Passing Bundle : " + sngArtArray.toString())
            intent.putExtras(bundle)
            startActivity(intent)
            val e = isPlayingShPref.edit()
            e.putBoolean("IsPlaying", false)
            e.apply()
            e.commit()


        }


    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        when (menuItem.getItemId()) {
            R.id.nav_home -> {
                super.onBackPressed()
            }
            R.id.nav_themes -> {
                val intent = Intent(this, Themes::class.java)
                startActivity(intent)
            }
            R.id.nav_drive -> {
                val intent = Intent(this, DriveMode::class.java)
                startActivity(intent)
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
                startActivity(Intent(this@ArtistSongs, Artists::class.java))
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }

}
