package com.example.prjhearttunes

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import android.widget.Toast
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.example.prjhearttunes.Queue.Queue


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    //Variables
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var menu: Menu
    lateinit var cvLib: CardView
    lateinit var cvArt: CardView
    lateinit var cvFav: CardView
    lateinit var cvPList: CardView
    lateinit var imgQueue: ImageView
    lateinit var txtSongName: TextView
    lateinit var txtArtistName: TextView
    lateinit var imgPlay: ImageView
    lateinit var imgNext: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestRuntimePermission()
        val themePrefMA = this.getSharedPreferences(currThemePref, Context.MODE_PRIVATE)
        currTheme = themePrefMA.getInt("CurrTheme",0)
        setTheme(this)
        setAllArray(this)
        setContentView(R.layout.activity_main)

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


        cvLib=findViewById(R.id.CV_Library)
        cvArt=findViewById(R.id.CV_Artists)
        cvFav=findViewById(R.id.CV_Favorite)
        cvPList=findViewById(R.id.CV_PlayList)
        imgQueue=findViewById(R.id.imgQueue)
        txtSongName=findViewById(R.id.txtSongName)
        txtArtistName=findViewById(R.id.txtArtistName)
        imgPlay=findViewById(R.id.imgPlay)
        imgNext=findViewById(R.id.imgNext)


        cvLib.setOnClickListener {
            val intent = Intent(this@MainActivity, Library::class.java)
            startActivity(intent)
        }

        cvArt.setOnClickListener {
            val intent = Intent(this@MainActivity, Artists::class.java)
            startActivity(intent)
        }

        cvFav.setOnClickListener {
            val intent = Intent(this@MainActivity, Favorite::class.java)
            startActivity(intent)
        }

        cvPList.setOnClickListener {
            val intent = Intent(this@MainActivity, PlayList::class.java)
            startActivity(intent)
        }

        imgQueue.setOnClickListener {
            val intent = Intent(this, Queue::class.java)
            startActivity(intent)
        }

        val currSongPref=this.getSharedPreferences(currSongPref, Context.MODE_PRIVATE)
        var mpId = setAllTextMA(currSongPref, txtSongName, txtArtistName, null)

//        mpId = currSongPref.getInt("CurrSongId",0)
//        var sngIndex = currSongPref.getInt("CurrSongIndex",0)
//        var title = currSongPref.getString("CurrSongName","")
//        txtSongName.text = title?.let { formatSongName(it) }
//        txtArtistName.text = currSongPref.getString("CurrSongArtist","")

        val isPlayingShPref = this.getSharedPreferences(isSongPlaying,Context.MODE_PRIVATE)

        if (isPlayingShPref.getBoolean("IsPlaying",false))
        {
            imgPlay.setImageResource(R.drawable.ic_pause)
        }
        else
        {
            imgPlay.setImageResource(R.drawable.ic_play_)
        }

        val sngDetails = SongDetails(
            txtSongName.text.toString(),
            sngDurationArray[sngIndex].toLong(),
            txtArtistName.text.toString()
        )

//        val mp = MediaPlayer.create(this,mpId)

        imgPlay.setOnClickListener {

            var isIt = isPlayingShPref.getBoolean("IsPlaying",false)

            isIt = playSong(this,mpId,imgPlay,isIt)

            val editor = isPlayingShPref.edit()
            editor.putBoolean("IsPlaying", isIt)
            editor.apply()
            editor.commit()

//            if (!mp.isPlaying) {
//                mp.start()
//                imgPlay.setImageResource(R.drawable.ic_pause)
//            }
//            else if (mp.isPlaying){
//                mp.pause()
//                imgPlay.setImageResource(R.drawable.ic_play_)
//            }
        }

        imgNext.setOnClickListener {

            Log.e("myError", "imgNextClicked")
            if (favList.contains(sngDetails)) {
                playNextSong(this, mpId, true)
            }
            else {
                playNextSong(this, mpId, false)
            }
            mpId = setAllTextMA(currSongPref, txtSongName, txtArtistName, null)

        }


    }

    override fun onResume() {

        val themePref = this.getSharedPreferences(currThemePref, Context.MODE_PRIVATE)
        currTheme = themePref.getInt("CurrTheme",0)
        setTheme(this)
//        setContentView(R.layout.activity_main)

        navigationView.setCheckedItem(R.id.nav_home)
        diffArray = false

        var mpId = 0
        val currSongPref=this.getSharedPreferences(currSongPref, Context.MODE_PRIVATE)
        mpId = currSongPref.getInt("CurrSongId",0)
        var title = currSongPref.getString("CurrSongName","")
        txtSongName.text = title?.let { formatSongName(it) }
        txtArtistName.text = currSongPref.getString("CurrSongArtist","")

        val isPlayingShPref = this.getSharedPreferences(isSongPlaying,Context.MODE_PRIVATE)

        if (isPlayingShPref.getBoolean("IsPlaying",false))
        {
            imgPlay.setImageResource(R.drawable.ic_pause)
        }
        else
        {
            imgPlay.setImageResource(R.drawable.ic_play_)
        }

        val sngDetails = SongDetails(
            txtSongName.text.toString(),
            sngDurationArray[sngIndex].toLong(),
            txtArtistName.text.toString()
        )


        imgPlay.setOnClickListener {
            Log.e("myError","cur pos = " + mp.currentPosition)

            var isIt = isPlayingShPref.getBoolean("IsPlaying",false)

            isIt = playSong(this,mpId,imgPlay,isIt)

            val editor = isPlayingShPref.edit()
            editor.putBoolean("IsPlaying", isIt)
            editor.apply()
            editor.commit()
        }

        imgNext.setOnClickListener {
            Log.e("myError", "imgNextClicked")
            if (favList.contains(sngDetails)) {
                playNextSong(this, mpId, true)
            }
            else {
                playNextSong(this, mpId, false)
            }
            mpId = setAllTextMA(currSongPref, txtSongName, txtArtistName, null)
        }

        super.onResume()
    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        when (menuItem.getItemId()) {
            R.id.nav_home -> {
            }
            R.id.nav_themes -> {
                val intent = Intent(this@MainActivity, Themes::class.java)
                startActivity(intent)
            }
            R.id.nav_drive -> {
                val intent = Intent(this@MainActivity, DriveMode::class.java)
                startActivity(intent)
            }
            R.id.nav_timer -> {
                val intent = Intent(this@MainActivity, Timer::class.java)
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
            super.onBackPressed()
        }
    }

    private fun requestRuntimePermission(){
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE),11)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 11)
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted",Toast.LENGTH_SHORT).show()
            else
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE),11)
    }

    fun CurrentSong(view: View) {
        val intent = Intent(this@MainActivity, Player::class.java)
        startActivity(intent)

    }
}
