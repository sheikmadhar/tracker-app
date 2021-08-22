package com.wunderlzechner.fitnesstracker_wunderlzechner_2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.*


class NewActivity : AppCompatActivity() {
    lateinit var supportMapFragment: SupportMapFragment
    lateinit var client: FusedLocationProviderClient
    lateinit var latlng_str: String
    var arrayAdapter: ArrayAdapter<*>? = null
    lateinit var helper: DatabaseHandler
    var listView: ListView? = null
    var starttrue: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)
        //Assign variable
        val actionbar = supportActionBar
        //set back button
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        helper = DatabaseHandler(this)
        val array_list = helper.allCotacts
        listView = findViewById(R.id.listView)
        arrayAdapter =
            ArrayAdapter(this@NewActivity, android.R.layout.simple_list_item_1, array_list)
        listView?.setAdapter(arrayAdapter)
        supportMapFragment =
            (supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?)!!

        //Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this)
        array_list.clear()
        array_list.addAll(helper.allCotacts)
        arrayAdapter?.notifyDataSetChanged()
        listView?.invalidateViews()
        listView?.refreshDrawableState()

        findViewById<View>(R.id.Btn_End).setOnClickListener {
            // value of item that is clicked
            starttrue = false
            getCurrentLocation()
            val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy-hh-mm-ss")
            val currentTimeend: String = simpleDateFormat.format(Date())

            val latitudeend: String = latlng_str.split(",").get(0)
            val longitudeend: String = latlng_str.split(",").get(1)

            array_list.clear()
            array_list.addAll(helper.allCotacts)
            arrayAdapter?.notifyDataSetChanged()
            listView?.invalidateViews()
            listView?.refreshDrawableState()
            val itemValue = listView?.getItemAtPosition(0) as String

            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val status = databaseHandler.updateEndTrack(
                TrackModelClassEnd(
                    Integer.parseInt(itemValue),
                    currentTimeend,
                    latitudeend,
                    longitudeend

                )
            )
            if (status > -1) {
                val btnstart = findViewById<Button>(R.id.Btn_Start)
                val btnend = findViewById<Button>(R.id.Btn_End)
                val title = findViewById<EditText>(R.id.Title_ID)
                btnstart.visibility = View.VISIBLE
                btnend.visibility = View.GONE
                title.text.clear()
                Toast.makeText(applicationContext, "Track update", Toast.LENGTH_LONG).show()
                val i = Intent(this@NewActivity, ListActivity::class.java)
                startActivity(i)
            }

        }
        //Check permission
        if (ActivityCompat.checkSelfPermission(
                this@NewActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //When permission granted
            // call method
            getCurrentLocation()
        } else {
            //When permission dinied
            ActivityCompat.requestPermissions(
                this@NewActivity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 44
            )
        }

    }

    fun startFunction(view: View) {
        starttrue = true
        getCurrentLocation()
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy-hh-mm-ss")
        val currentTime: String = simpleDateFormat.format(Date())

        val latitude: String = latlng_str.split(",").get(0)
        val longitude: String = latlng_str.split(",").get(1)

        val title = findViewById<EditText>(R.id.Title_ID)
        var title_str = title.text.toString()
        if (title_str == "") {
            title_str = currentTime
        }
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        val status =
            databaseHandler.addTrack(
                TrackModelClassStart(
                    title_str,
                    currentTime,
                    latitude,
                    longitude
                )
            )
        if (status > -1) {
            Toast.makeText(applicationContext, "Track Save", Toast.LENGTH_LONG).show()
            val btnstart = findViewById<Button>(R.id.Btn_Start)
            val btnend = findViewById<Button>(R.id.Btn_End)
            btnstart.visibility = View.GONE
            btnend.visibility = View.VISIBLE
            title.text.clear()
        }

    }

    private fun getCurrentLocation() {
        //Initialize task location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val task = client!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                //sync map
                supportMapFragment!!.getMapAsync { googleMap -> // Initialize lat lon
                    val latLng = LatLng(location.latitude, location.longitude)

                    val latlng_s = latLng.toString()
                    val latlng_st: String = latlng_s.split(":").get(1)
                    val result: String = latlng_st.replace("(", "")
                    val result1: String = result.replace(")", "")
                    latlng_str = result1
//                    Log.d("latLng", latLng.toString())
                    // create marker
                    val options = MarkerOptions().position(latLng).title("I am here")
                    //Zoom Map
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                    googleMap.addMarker(options)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == 44) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //When permission granted
                // call method
                getCurrentLocation()
            }
        }
    }

    override fun onBackPressed() {
//        moveTaskToBack(true)
        if (starttrue == true) {
            Log.d("BACKBACK", "HAI")
            starttrue = false
            getCurrentLocation()
            val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy-hh-mm-ss")
            val currentTimeend: String = simpleDateFormat.format(Date())

            val latitudeend: String = latlng_str.split(",").get(0)
            val longitudeend: String = latlng_str.split(",").get(1)

//            val array_list = helper.allCotacts
            helper = DatabaseHandler(this)
            val array_list = helper.allCotacts
            listView = findViewById(R.id.listView)
            arrayAdapter =
                ArrayAdapter(this@NewActivity, android.R.layout.simple_list_item_1, array_list)
            listView?.setAdapter(arrayAdapter)
            array_list.clear()
            array_list.addAll(helper.allCotacts)
            arrayAdapter?.notifyDataSetChanged()
            listView?.invalidateViews()
            listView?.refreshDrawableState()
            val itemValue = listView?.getItemAtPosition(0) as String

            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val status = databaseHandler.updateEndTrack(
                TrackModelClassEnd(
                    Integer.parseInt(itemValue),
                    currentTimeend,
                    latitudeend,
                    longitudeend

                )
            )
            if (status > -1) {
                val btnstart = findViewById<Button>(R.id.Btn_Start)
                val btnend = findViewById<Button>(R.id.Btn_End)
                val title = findViewById<EditText>(R.id.Title_ID)
                btnstart.visibility = View.VISIBLE
                btnend.visibility = View.GONE
                title.text.clear()
                Toast.makeText(applicationContext, "Track update", Toast.LENGTH_LONG).show()
                val i = Intent(this@NewActivity, MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
                finish()
            }
        } else {
            val i = Intent(this@NewActivity, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
            finish()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}