package com.example.prjhearttunes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Favorite : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var lstFavSongs: ListView
    lateinit var favSngArray : ArrayList<SongDetails>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themePrefMA = this.getSharedPreferences(currThemePref, Context.MODE_PRIVATE)
        currTheme = themePrefMA.getInt("CurrTheme",0)
        setTheme(this)
        setContentView(R.layout.activity_favorite)

        drawerLayout=findViewById(R.id.drawer_layout)
        navigationView=findViewById(R.id.nav_view)
        toolbar=findViewById(R.id.toolbar)

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
        lstFavSongs = findViewById(R.id.lstfavSong)

        favSngArray = ArrayList()

//        for (i in favIdList.indices) {
////            Log.e("myError", "Inside For loop")
////            sngDurationArray.add(MediaPlayerArray[i].duration.toString())
//
//            val sngDetails = SongDetails(
//                sngTitleArray[favIdList[i]],
//                sngDurationArray[favIdList[i]].toLong(),
//                sngArtistArray[favIdList[i]]
//            )
//
//            favSngArray.add(sngDetails)
//
//            CurrSongIDArray.add(sngIdArray[favIdList[i]])
//            CurrSongTitleArray.add(sngTitleArray[favIdList[i]])
//            CurrSongDurationArray.add(sngDurationArray[favIdList[i]])
//            CurrSongArtistArray.add(sngArtistArray[favIdList[i]])
//        }

        lstFavSongs = findViewById(R.id.lstfavSong)

//        val isFavPref = this.getSharedPreferences(favSongPref, Context.MODE_PRIVATE)
//        val gson = Gson()
//        val json = isFavPref.getString("Fav List", null)
//        val type: Type = object : TypeToken<ArrayList<SongDetails?>?>() {}.type
//        favList = gson.fromJson<SongDetails>(json, type) as ArrayList<SongDetails>

//        lstFavSongs.adapter = MusicAdapter(this, favSngArray)
        lstFavSongs.adapter = MusicAdapter(this, favList)

        lstFavSongs.setOnItemClickListener { parent, view, position, id ->

            mp.pause()
//            seekTime = 0
            firstAttempt = true
//            sngIndex = id.toInt()

            val i = favIdList[id.toInt()]
            val SharedPreferences = this.getSharedPreferences(currSongPref, Context.MODE_PRIVATE)
            val editor = SharedPreferences.edit()
            editor.putInt("CurrSongId", sngIdArray[i])
            editor.putInt("CurrSongIndex", i)
            editor.putString("CurrSongName", sngTitleArray[i])
            editor.putString("CurrSongArtist", sngArtistArray[i])
            editor.putInt("CurrSongDuration", sngDurationArray[i].toInt())
            editor.apply()

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
                startActivity(Intent(this@Favorite, MainActivity::class.java))
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }
}
