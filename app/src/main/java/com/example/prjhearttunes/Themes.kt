package com.example.prjhearttunes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class Themes : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var thDef: CardView
    lateinit var thDB: CardView
    lateinit var thG: CardView
    lateinit var thB: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themePrefTA = this.getSharedPreferences(currThemePref, Context.MODE_PRIVATE)
        currTheme = themePrefTA.getInt("CurrTheme",0)
        setTheme(this)
        setContentView(R.layout.activity_themes)

        drawerLayout=findViewById(R.id.drawer_layout)
        navigationView=findViewById(R.id.nav_view)
        toolbar=findViewById(R.id.toolbar)
        thDef=findViewById(R.id.Th_Default)
        thDB=findViewById(R.id.Th_DarkBlue)
        thG=findViewById(R.id.Th_Green)
        thB=findViewById(R.id.Th_Brown)

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
        navigationView.setCheckedItem(R.id.nav_themes)

        val themePref = this.getSharedPreferences(currThemePref, Context.MODE_PRIVATE)
        val editor = themePref.edit()


        thDef.setOnClickListener {
//            theme.applyStyle(R.style.AppTheme_Purple, true)

            editor.putInt("CurrTheme", 0)
            editor.apply()
            editor.commit()

//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)

           finish()
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }
        thDB.setOnClickListener {
//            theme.applyStyle(R.style.AppTheme_DarkBlue, true)
//            setTheme(R.style.AppTheme_DarkBlue)
//            application.setTheme(R.style.AppTheme_DarkBlue)

//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            editor.putInt("CurrTheme", 1)
            editor.apply()
            editor.commit()

//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)

            finish()
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )

        }

        thG.setOnClickListener {

            editor.putInt("CurrTheme", 2)
            editor.apply()
            editor.commit()

            finish()
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }

        thB.setOnClickListener {

            editor.putInt("CurrTheme", 3)
            editor.apply()
            editor.commit()

            finish()
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
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
                startActivity(Intent(this@Themes, MainActivity::class.java))
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }
}
