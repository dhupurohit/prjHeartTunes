package com.example.prjhearttunes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.prjhearttunes.R.layout.group_list_view
import com.google.android.material.navigation.NavigationView

class Artists : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var artistsList: ListView
    lateinit var artistsListArray : Array<String>
    lateinit var artistIdArray : ArrayList<Int>
//    lateinit var plyLstArrayAdapter : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themePrefMA = this.getSharedPreferences(currThemePref, Context.MODE_PRIVATE)
        currTheme = themePrefMA.getInt("CurrTheme",0)
        setTheme(this)
        setContentView(R.layout.activity_artists)

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

        artistsList = findViewById(R.id.artists_List)


        artistsListArray = arrayOf("Kishore Kumar", "Lata Mangeshkar", "Md.Rafi", "Nikhil D'Souza",
            "Jigar", "Arijit Singh", "Pritam Chakraborty", "Shreya Ghoshal","Kailash Kher", "Sonu Nigam", "Tochi Raina",
            "Amit Trivedi", "Atif Aslam")


//        plyLstArrayAdapter = ArrayAdapter(
//            this,
//            group_list_view,
//            artistsListArray
//        )

        artistsList.adapter = ListAdapter(this,artistsListArray)

        artistsList.setOnItemClickListener { parent, view, position, id ->


            artistIdArray = ArrayList()

            when(artistsListArray[position])
            {
                "Kishore Kumar" -> {
                    artistIdArray.add(0)
                }
                "Lata Mangeshkar" -> {
                    artistIdArray.add(0)
                    artistIdArray.add(1)
                }
                "Md.Rafi" -> {
                    artistIdArray.add(1)
                }
                "Nikhil D'Souza" -> {
                    artistIdArray.add(2)
                }
                "Jigar" -> {
                    artistIdArray.add(3)
                }
                "Arijit Singh" -> {
                    artistIdArray.add(4)
                    artistIdArray.add(9)
                }
                "Pritam Chakraborty" -> {
                    artistIdArray.add(5)
                }
                "Shreya Ghoshal" -> {
                    artistIdArray.add(6)
                    artistIdArray.add(8)
                    artistIdArray.add(9)
                }
                "Kailash Kher" -> {
                    artistIdArray.add(7)
                }
                "Sonu Nigam" -> {
                    artistIdArray.add(6)
                }
                "Tochi Raina" -> {
                    artistIdArray.add(8)
                }
                "Amit Trivedi" -> {
                    artistIdArray.add(10)
                }
                "Atif Aslam" -> {
                    artistIdArray.add(11)
                }
            }

            val intent = Intent(this, ArtistSongs::class.java)
            val bundle = Bundle()
            bundle.putIntegerArrayList("Curent Artist Array", artistIdArray)
            intent.putExtras(bundle)
            intent.putExtra("ArtistName",artistsListArray[position])
            startActivity(intent)

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
                startActivity(Intent(this@Artists, MainActivity::class.java))
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }

}
