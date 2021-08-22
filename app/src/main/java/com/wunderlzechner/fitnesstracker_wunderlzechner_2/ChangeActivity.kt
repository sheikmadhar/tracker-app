package com.wunderlzechner.fitnesstracker_wunderlzechner_2

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.smsprogramerz.sqlitedbkotlin.TrackModelClassChange


class ChangeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)
        val GetToken = applicationContext.getSharedPreferences(
            "Track",
            AppCompatActivity.MODE_PRIVATE
        )
        val token = GetToken.getString("ID", "")
        val actionbar = supportActionBar
        //set back button
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val trk: List<TrackModelClassChange> = databaseHandler.ChangeTrack(token.toString())
        val trkArrayId = Array<String>(trk.size) { "0" }
        val trkArrayTitle = Array<String>(trk.size) { "null" }
        val trkArrayStartTime = Array<String>(trk.size) { "null" }
        val trkArrayEndTime = Array<String>(trk.size) { "null" }
        val trkArraylat = Array<String>(trk.size) { "null" }
        val trkArraylng = Array<String>(trk.size) { "null" }
        val trkArraylatend = Array<String>(trk.size) { "null" }
        val trkArraylngend = Array<String>(trk.size) { "null" }
        var index = 0
        for (e in trk) {
            trkArrayId[index] = e.trackId.toString()
            trkArrayTitle[index] = e.trackTitle
            trkArrayStartTime[index] = e.trackStartTime
            trkArrayEndTime[index] = e.trackEndTime
            trkArraylat[index] = e.tracklat
            trkArraylng[index] = e.tracklng
            trkArraylatend[index] = e.tracklatend
            trkArraylngend[index] = e.tracklngend
            index++
        }
        //creating custom ArrayAdapter
        val myListAdaptern = MyListAdapterN(
            this,
            trkArrayId,
            trkArrayTitle,
            trkArrayStartTime,
            trkArrayEndTime,
            trkArraylat,
            trkArraylng,
            trkArraylatend,
            trkArraylngend
        )
        val listViewschng = findViewById(R.id.listViewsChng) as ListView
        listViewschng.adapter = myListAdaptern
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val i = Intent(this@ChangeActivity, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
        finish()
    }
}