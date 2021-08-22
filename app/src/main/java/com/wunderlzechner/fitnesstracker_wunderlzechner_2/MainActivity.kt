package com.wunderlzechner.fitnesstracker_wunderlzechner_2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // onClick “TRACKING START”
    fun GotoNewActivity(view: View) {
        val i = Intent(this@MainActivity, NewActivity::class.java)
        startActivity(i)

    }

    // onClick “EINTRÄGE ANZEIGEN”
    fun GotoListActivity(view: View) {
        val i = Intent(this@MainActivity, ListActivity::class.java)
        startActivity(i)
    }
}