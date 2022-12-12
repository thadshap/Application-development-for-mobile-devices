package com.example.myfirstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.firstname -> Log.w("Tag", "Thadshajini is selected.")//Toast.makeText(this, "Thadshajini is selected.", Toast.LENGTH_SHORT).show()
            R.id.lastname -> Log.e("Tag", "Paramsothy is selected.")//Toast.makeText(this, "Paramsothy is selected.", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}