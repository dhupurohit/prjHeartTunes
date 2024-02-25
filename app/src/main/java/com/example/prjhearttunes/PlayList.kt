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
import com.example.prjhearttunes.PlayLists.PlayListSongs
import com.google.android.material.navigation.NavigationView

class PlayList : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var plyLst: ListView
    lateinit var plyLstArray : Array<String>
//    lateinit var plyLstArrayAdapter : ArrayAdapter<String>
    lateinit var artistIdArray : ArrayList<Int>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themePrefMA = this.getSharedPreferences(currThemePref, Context.MODE_PRIVATE)
        currTheme = themePrefMA.getInt("CurrTheme",0)
        setTheme(this)
        setContentView(R.layout.activity_play_list)

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

        plyLst = findViewById(R.id.plyLst)
        plyLstArray = arrayOf("Morning", "Night Sleep", "WorkOut", "Heartbeats", "Old is Gold", "Sad", "Love")

//        plyLstArrayAdapter = ArrayAdapter(
//            this,
//            R.layout.group_list_view,
//            plyLstArray
//        )

        plyLst.adapter = ListAdapter(this,plyLstArray)

        plyLst.setOnItemClickListener { parent, view, position, id ->


            artistIdArray = ArrayList()

            when(plyLstArray[position])
            {
                "Morning" -> {
                    artistIdArray.add(10)
                    artistIdArray.add(11)
                }
                "Night Sleep" -> {
                    artistIdArray.add(2)
                    artistIdArray.add(4)
                    artistIdArray.add(7)
                    artistIdArray.add(9)
                }
                "WorkOut" -> {
                    artistIdArray.add(3)
                    artistIdArray.add(5)
                    artistIdArray.add(11)
                }
                "Heartbeats" -> {
                    artistIdArray.add(2)
                    artistIdArray.add(6)
                    artistIdArray.add(7)
                }
                "Old is Gold" -> {
                    artistIdArray.add(0)
                    artistIdArray.add(1)
                    artistIdArray.add(7)
                }
                "Sad" -> {
                    artistIdArray.add(4)
                    artistIdArray.add(9)
                }
                "Love" -> {
                    artistIdArray.add(2)
                    artistIdArray.add(6)
                    artistIdArray.add(7)
                    artistIdArray.add(8)
                    artistIdArray.add(9)
                    artistIdArray.add(10)
                }

            }

            val intent = Intent(this, PlayListSongs::class.java)
            val bundle = Bundle()
            bundle.putIntegerArrayList("Curent Artist Array", artistIdArray)
            intent.putExtras(bundle)
            intent.putExtra("PlayListName",plyLstArray[position])
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
                startActivity(Intent(this@PlayList, MainActivity::class.java))
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }
}
