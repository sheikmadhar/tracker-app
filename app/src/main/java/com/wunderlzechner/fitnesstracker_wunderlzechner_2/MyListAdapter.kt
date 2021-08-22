package com.wunderlzechner.fitnesstracker_wunderlzechner_2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


class MyListAdapter(
    private val context: Activity,
    private val id: Array<String>,
    private val title: Array<String>,
    private val startTime: Array<String>,
    private val endTime: Array<String>,
    private val startlat: Array<String>,
    private val startlng: Array<String>,
    private val endlat: Array<String>,
    private val endlng: Array<String>


) : ArrayAdapter<String>(context, R.layout.custom_list, title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val txt_id = rowView.findViewById(R.id.txt_id) as TextView
        val txt_title = rowView.findViewById(R.id.txt_title) as TextView
        val txt_time = rowView.findViewById(R.id.txt_time) as TextView
        val txt_distance = rowView.findViewById(R.id.txt_distance) as TextView
        val linearLayout_LL = rowView.findViewById(R.id.linearLayout) as LinearLayout
        linearLayout_LL.setOnClickListener{
//
            val id = id[position]
            GotoChange(id)

        }

        txt_id.text = "${id[position]}"
        txt_title.text = "${title[position]}"

        val lat1: String = startlat[position]
        val lat2: String = endlat[position]
        val lng1: String = startlng[position]
        val lng2: String = endlng[position]

        if (lat1 != "" && lat2 != "") {
            val latd1: Double? = lat1.toDouble()
            val latd2: Double? = lat2.toDouble()
            val lngd1: Double? = lng1.toDouble()
            val lngd2: Double? = lng2.toDouble()
            val loc1 = Location("")
            loc1.latitude = latd1!!
            loc1.longitude = lngd1!!
            val loc2 = Location("")
            loc2.latitude = latd2!!
            loc2.longitude = lngd2!!
            val distanceInMeters: Float = loc1.distanceTo(loc2)
            val newmeter = ((distanceInMeters / 1000).toString())

            Log.d("distanceInMeters", newmeter.toString());
            txt_distance.text = newmeter.toString()+" kms"
        }


        var startDate: Date? = null
        var endDate: Date? = null

        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy-hh-mm-ss")
        startDate = simpleDateFormat.parse(startTime[position])
        endDate = simpleDateFormat.parse(endTime[position])

        var difference: Long = endDate.getTime() - startDate.getTime()
        val seconds = difference / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        var days = hours / 24

        var duration: String

        if (days.toString().equals("0")) {
            if (hours.toString().equals("0")) {
                if (minutes.toString().equals("0")) {
                    if (seconds.toString().equals("0")) {
                        duration = "Just"
                    } else {
                        duration = seconds.toString() + " secs "
                    }

                } else {
                    duration = minutes.toString() + " mins " + seconds.toString() + " secs "
                }
            } else {
                duration =
                    hours.toString() + " hrs " + minutes.toString() + " mins " + seconds.toString() + " secs "
            }
        } else {
            duration =
                days.toString() + " days " + hours.toString() + " hrs " + minutes.toString() + " mins " + seconds.toString() + " secs "
        }
        txt_time.text = duration


        return rowView
    }

    private fun GotoChange(id: String) {
        val TokenSave = context.getSharedPreferences("Track", Context.MODE_PRIVATE)
        val TokenSaveeditor = TokenSave.edit()
        TokenSaveeditor.putString("ID", id)
        TokenSaveeditor.commit()
        val intent = Intent(context, ChangeActivity::class.java)
        intent.putExtra("ID", id)
        context.startActivity(intent)
    }

}