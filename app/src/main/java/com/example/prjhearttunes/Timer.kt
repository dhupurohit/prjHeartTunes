package com.example.prjhearttunes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Timer : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var lstTimer: ListView
    lateinit var lstTimerArray: Array<String>
    lateinit var lstTimerArrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themePrefMA = this.getSharedPreferences(currThemePref, Context.MODE_PRIVATE)
        currTheme = themePrefMA.getInt("CurrTheme",0)
        setTheme(this)
        setContentView(R.layout.activity_timer)

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
        navigationView.setCheckedItem(R.id.nav_timer)


        lstTimer = findViewById(R.id.lstTimer)
        lstTimerArray = arrayOf(
            "No Timer",
            "5 Minutes",
            "10 Minutes",
            "15 Minutes",
            "20 Minutes",
            "25 Minutes",
            "30 Minutes",
            "1 hour",
            "1 hour 30 Minutes",
            "2 hour"
        )

        lstTimerArrayAdapter = ArrayAdapter(
            this,
            android.R.layout.select_dialog_singlechoice,
            lstTimerArray
        )

        lstTimer.choiceMode = ListView.CHOICE_MODE_SINGLE

        lstTimer.adapter = lstTimerArrayAdapter

        lstTimer.setItemChecked(0, true)
        lstTimer.setOnItemClickListener { parent, view, position, id ->

            var ms = 10000
            Toast.makeText(this,"Id : " + id, Toast.LENGTH_SHORT).show()
//            var timer = object : CountDownTimer(ms.toLong(), 1000) {
//
//                override fun onTick(millisUntilFinished: Long) {
//                    Toast.makeText(this@Timer,"Time remaining : " + millisUntilFinished, Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onFinish() {
//                            this@Timer.finishAffinity()
//                }
//            }
//
//            if (position == 0)
//            {
//                timer.cancel()
//                Toast.makeText(this, "No Timer Set", Toast.LENGTH_SHORT).show()
//            } else
//            {
//                var pos = 1
//                var toLong = pos.toLong()
//                if (id == toLong)
//                {
//                    ms = 300000
//                    Toast.makeText(this,"Set timer is for : " + ms/60 , Toast.LENGTH_SHORT)
//                    timer.start()
//                }
//                when (position)
//                {
//                    1 -> {
//                        ms = 300000
//                        timer.start()
//                        Toast.makeText(this,"Set timer is for : " + ms/60 , Toast.LENGTH_SHORT)
//                    }
//                    2 -> {
//                        ms = 600000
//                        timer.start()
//                    }
//                    3 -> {
//                        ms = 900000
//                        timer.start()
//                    }
//                    4 -> {
//                        ms = 1200000
//                        timer.start()
//                    }
//                    5 -> {
//                        ms = 1500000
//                        timer.start()
//                    }
//                    6 -> {
//                        ms = 1800000
//                        timer.start()
//                    }
//                    7 -> {
//                        ms = 3600000
//                        timer.start()
//                    }
//                    8 -> {
//                        ms = 5400000
//                        timer.start()
//                    }
//                    9 -> {
//                        ms = 7200000
//                        timer.start()
//                    }
//                }

//            }
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
                val intent = Intent(this, DriveMode::class.java)
                startActivity(intent)
            }
            R.id.nav_timer -> {

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
                startActivity(Intent(this@Timer, MainActivity::class.java))
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }
}
