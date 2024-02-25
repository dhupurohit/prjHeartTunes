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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Suppress("NAME_SHADOWING")
class Player : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var imgQueue: ImageView
    lateinit var imgPlay: ImageView
    lateinit var imgNext: ImageView
    lateinit var imgPrev: ImageView
    lateinit var imgRepeat: ImageView
    lateinit var imgFav: ImageView
    lateinit var imgShuffel: ImageView
    lateinit var txtTitle: TextView
    lateinit var txtArtist: TextView
    lateinit var txtEndTime: TextView
    lateinit var seekBar: SeekBar
    lateinit var sngArtArray: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themePrefMA = this.getSharedPreferences(currThemePref, Context.MODE_PRIVATE)
        currTheme = themePrefMA.getInt("CurrTheme", 0)
        setTheme(this)
        setContentView(R.layout.activity_player)

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
        navigationView.setCheckedItem(R.id.nav_home)


        imgQueue = findViewById(R.id.imgQueue)
        imgPlay = findViewById(R.id.imgPlay)
        imgPrev = findViewById(R.id.imgPrev)
        imgNext = findViewById(R.id.imgNext)
        imgRepeat = findViewById(R.id.imgRepeat)
        imgShuffel = findViewById(R.id.imgShuffel)
        imgFav = findViewById(R.id.imgFav)
        txtEndTime = findViewById(R.id.txtEndTime)
        txtTitle = findViewById(R.id.txtSongName)
        txtArtist = findViewById(R.id.txtArtistName)
        seekBar = findViewById(R.id.seekBar)

        sngArtArray = ArrayList()
//        favList = ArrayList()
//        favIdList = ArrayList()
//        favSongArtistArray = ArrayList()
//        favSongIDArray = ArrayList()
//        favSongDurationArray = ArrayList()
//        favSongTitleArray = ArrayList()


        val currSongPref = this.getSharedPreferences(currSongPref, Context.MODE_PRIVATE)
        var mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
//        mpId = currSongPref.getInt("CurrSongId",0)
//        val title = currSongPref.getString("CurrSongName","")
//        txtTitle.text = title?.let { formatSongName(it) }
//        txtArtist.text = currSongPref.getString("CurrSongArtist","")
//        val dur = currSongPref.getInt("CurrSongDuration",0).toLong()
//        txtEndTime.text = formatDuration(dur)

        val isPlayingShPref = this.getSharedPreferences(isSongPlaying, Context.MODE_PRIVATE)
//        val isFavPref = this.getSharedPreferences(favSongPref, Context.MODE_PRIVATE)

        val suffPref = this.getSharedPreferences(isSongShuffel, Context.MODE_PRIVATE)
        shuffle = suffPref.getBoolean("shuffleing?", false)

        if (shuffle) {
            imgShuffel.setImageResource(R.drawable.ic_shuffle)
        } else {
            imgShuffel.setImageResource(R.drawable.ic_shuffle_light)
        }

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

        var sngDetails: SongDetails
        sngDetails= SongDetails(
            txtTitle.text.toString(),
            sngDurationArray[sngIndex].toLong(),
            txtArtist.text.toString()
        )

        if (favList.contains(sngDetails))
        {imgFav.setImageResource(R.drawable.ic_favorite_fill)}
        else
        {imgFav.setImageResource(R.drawable.ic_favorite_borde)}

        handler.postDelayed(object : Runnable {
            override fun run() {
                if (isPlayingShPref.getBoolean("IsPlaying", false)) {
                    imgPlay.setImageResource(R.drawable.ic_pause)
                } else {
                    imgPlay.setImageResource(R.drawable.ic_play_)
                }

                if (favList.contains(sngDetails))
                {imgFav.setImageResource(R.drawable.ic_favorite_fill)}
                else
                {imgFav.setImageResource(R.drawable.ic_favorite_borde)}

                if (seekBar.progress == seekBar.max) {
//
                    if (favList.contains(sngDetails)) {
                        diffArray = true

                        if (mp.isLooping) {
                            var isIt = isPlayingShPref.getBoolean("IsPlaying", false)

                            isIt = playSong(this@Player, mpId, imgPlay, isIt)

                            val editor = isPlayingShPref.edit()
                            editor.putBoolean("IsPlaying", isIt)
                            editor.apply()
//                        editor.commit()
                        } else {
                            playNextSong(this@Player, mpId, true)
                            mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
                            sngDetails= SongDetails(
                                txtTitle.text.toString(),
                                sngDurationArray[sngIndex].toLong(),
                                txtArtist.text.toString()
                            )
                        }
                    } else {
                        diffArray = false
                        if (mp.isLooping) {
                            var isIt = isPlayingShPref.getBoolean("IsPlaying", false)

                            isIt = playSong(this@Player, mpId, imgPlay, isIt)

                            val editor = isPlayingShPref.edit()
                            editor.putBoolean("IsPlaying", isIt)
                            editor.apply()
//                        editor.commit()
                        } else if (shuffle) {
                            shuffelSongs(this@Player, mpId)
                            mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
                            sngDetails= SongDetails(
                                txtTitle.text.toString(),
                                sngDurationArray[sngIndex].toLong(),
                                txtArtist.text.toString()
                            )
                        } else {
                            playNextSong(this@Player, mpId, false)
                            mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
                            sngDetails= SongDetails(
                                txtTitle.text.toString(),
                                sngDurationArray[sngIndex].toLong(),
                                txtArtist.text.toString()
                            )
                        }
                    }
                }
                handler.postDelayed(this, 1000)
            }
        }, 0)


        imgQueue.setOnClickListener {

            val bundle = intent.extras
            val intent = Intent(this, Queue::class.java)
            if (diffArray) {
                sngArtArray = bundle?.getIntegerArrayList("Curent Artist Array") as ArrayList<Int>
                val b = Bundle()
                b.putIntegerArrayList("Curent Artist Array", sngArtArray)
                intent.putExtras(b)
            }

            startActivity(intent)
        }

        imgPlay.setOnClickListener {

            var isIt = isPlayingShPref.getBoolean("IsPlaying", false)

            isIt = playSong(this, mpId, imgPlay, isIt)

            val editor = isPlayingShPref.edit()
            editor.putBoolean("IsPlaying", isIt)
            editor.apply()
//            editor.commit()

        }

        imgPrev.setOnClickListener {
            if (mp.isLooping) {
                var isIt = isPlayingShPref.getBoolean("IsPlaying", false)

                isIt = playSong(this@Player, mpId, imgPlay, isIt)

                val editor = isPlayingShPref.edit()
                editor.putBoolean("IsPlaying", isIt)
                editor.apply()
//                        editor.commit()
            } else if (shuffle) {
                sngIndex -= 1
                mpId -= 1
                shuffelSongs(this@Player, mpId)
                mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
                sngDetails= SongDetails(
                    txtTitle.text.toString(),
                    sngDurationArray[sngIndex].toLong(),
                    txtArtist.text.toString()
                )
            } else {
                playPrevSong(this@Player, mpId)
                mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
                sngDetails= SongDetails(
                    txtTitle.text.toString(),
                    sngDurationArray[sngIndex].toLong(),
                    txtArtist.text.toString()
                )
            }
        }

        imgNext.setOnClickListener {
            if (mp.isLooping) {
                seekTime = 0
                var isIt = isPlayingShPref.getBoolean("IsPlaying", false)

                isIt = playSong(this@Player, mpId, imgPlay, isIt)

                val editor = isPlayingShPref.edit()
                editor.putBoolean("IsPlaying", isIt)
                editor.apply()

            } else if (shuffle) {
                sngIndex += 1
                mpId += 1
                shuffelSongs(this@Player, mpId)
                mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
                sngDetails= SongDetails(
                    txtTitle.text.toString(),
                    sngDurationArray[sngIndex].toLong(),
                    txtArtist.text.toString()
                )
            }
            else {
                    playNextSong(this@Player, mpId, false)
                mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
                sngDetails= SongDetails(
                    txtTitle.text.toString(),
                    sngDurationArray[sngIndex].toLong(),
                    txtArtist.text.toString()
                )
            }
        }

        imgFav.setOnClickListener {

            if (favList.contains(sngDetails)) {
                // is in favourite

                favIdList.remove(sngIndex)
//                favSongIDArray.remove(mpId)
//                favSongTitleArray.remove(txtTitle.text.toString())
//                favSongDurationArray.remove(sngDurationArray[sngIndex])
//                favSongArtistArray.remove(txtArtist.text.toString())

                favList.remove(sngDetails)
                imgFav.setImageResource(R.drawable.ic_favorite_borde)

            } else {
                // is not in favourite

                favIdList.add(sngIndex)
//                favSongIDArray.add(mpId)
//                favSongTitleArray.add(txtTitle.text.toString())
//                favSongDurationArray.add(sngDurationArray[sngIndex])
//                favSongArtistArray.add(txtArtist.text.toString())

                favList.add(sngDetails)
                imgFav.setImageResource(R.drawable.ic_favorite_fill)

            }
//            fav(imgFav, )

        }

        imgRepeat.setOnClickListener {

            repeat(imgRepeat)
        }

        imgShuffel.setOnClickListener {
            val suffPref = this@Player.getSharedPreferences(isSongShuffel, Context.MODE_PRIVATE)
            shuffle = suffPref.getBoolean("shuffleing?", false)
            val e = suffPref.edit()
            if (shuffle) {
                e.putBoolean("shuffleing?", false)
                e.apply()
                e.commit()
                imgShuffel.setImageResource(R.drawable.ic_shuffle_light)
                shuffle = false
            } else if (!shuffle) {
                e.putBoolean("shuffleing?", true)
                e.apply()
                e.commit()
                imgShuffel.setImageResource(R.drawable.ic_shuffle)
                shuffle = true
                sngIndex += 1
                mpId += 1
//                shuffelSongs(this, mpId)
//                mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
            }


//            shuffleSongs(this, mpId)
//            mpId = setAllTextMA(currSongPref, txtTitle, txtArtist, txtEndTime)
        }


    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        when (menuItem.itemId) {
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
                startActivity(Intent(this@Player, MainActivity::class.java))
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }
}
