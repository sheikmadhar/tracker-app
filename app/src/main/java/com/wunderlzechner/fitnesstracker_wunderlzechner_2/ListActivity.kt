package com.wunderlzechner.fitnesstracker_wunderlzechner_2

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.smsprogramerz.sqlitedbkotlin.TrackModelClassView


class ListActivity : AppCompatActivity() {
    var listActivity: ListActivity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        listActivity = this;
        val actionbar = supportActionBar
        //set back button
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
//        calling the viewEmployee method of DatabaseHandler class to read the records
        val trk: List<TrackModelClassView> = databaseHandler.viewTrack()
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
        val myListAdapter = MyListAdapter(
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
        val listViews = findViewById(R.id.listViews) as ListView
        listViews.adapter = myListAdapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val i = Intent(this@ListActivity, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
        finish()
    }
}