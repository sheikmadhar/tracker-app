package com.wunderlzechner.fitnesstracker_wunderlzechner_2

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.smsprogramerz.sqlitedbkotlin.TrackModelClassChange
import okhttp3.OkHttpClient
import okhttp3.Request


class MyListAdapterN(
    private val context: ChangeActivity,
    private val id: Array<String>,
    private val title: Array<String>,
    private val startTime: Array<String>,
    private val endTime: Array<String>,
    private val startlat: Array<String>,
    private val startlng: Array<String>,
    private val endlat: Array<String>,
    private val endlng: Array<String>


) : ArrayAdapter<String>(context, R.layout.change_list, title) {
    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap: GoogleMap
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val GetToken = context.getSharedPreferences("Track", AppCompatActivity.MODE_PRIVATE)
        val token = GetToken.getString("ID", "")

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.change_list, null, true)

        val txt_id = rowView.findViewById(R.id.Ctxt_id) as TextView
        val txt_title = rowView.findViewById(R.id.Ctxt_title) as TextView
        val txt_stime = rowView.findViewById(R.id.Ctxt_stime) as TextView
        val txt_etime = rowView.findViewById(R.id.Ctxt_etime) as TextView
        val txt_slat = rowView.findViewById(R.id.Ctxt_slat) as TextView
        val txt_slng = rowView.findViewById(R.id.Ctxt_slng) as TextView
        val txt_elat = rowView.findViewById(R.id.Ctxt_elat) as TextView
        val txt_elng = rowView.findViewById(R.id.Ctxt_elng) as TextView
        val btn_update = rowView.findViewById(R.id.btn_update) as Button


        txt_id.text = "${id[position]}"
        txt_title.text = "${title[position]}"
        txt_stime.text = "${startTime[position]}"
        txt_etime.text = "${endTime[position]}"
        txt_slat.text = "${startlat[position]}"
        txt_slng.text = "${startlng[position]}"
        txt_elat.text = "${endlat[position]}"
        txt_elng.text = "${endlng[position]}"

        val S_Slat: String = "${startlat[position]}"
        val D_Slat: Double? = S_Slat.toDouble()
        val S_Slng: String = "${startlng[position]}"
        val D_Slng: Double? = S_Slng.toDouble()

        val S_Elat: String = "${endlat[position]}"
        val D_Elat: Double? = S_Elat.toDouble()
        val S_Elng: String = "${endlng[position]}"
        val D_Elng: Double? = S_Elng.toDouble()


        mapFragment = context.supportFragmentManager.findFragmentById(R.id.mapacc) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
            Log.d("GoogleMap", D_Slat.toString()+D_Slng.toString())
//            googleMap.isMyLocationEnabled = true
            val location1 = LatLng(D_Slat!!, D_Slng!!)
            googleMap.addMarker(MarkerOptions().position(location1).title("Start"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location1,12f))

            val location2 = LatLng(D_Elat!!, D_Elng!!)
            googleMap.addMarker(MarkerOptions().position(location2).title("End"))

            Log.d("GoogleMap", "before URL")
            val URL = getDirectionURL(location1,location2)
            Log.d("GoogleMap", "URL : $URL")
            GetDirection(URL).execute()

        })

        btn_update.setOnClickListener{
            //creating the instance of DatabaseHandler class
            val get_id =txt_id.getText().toString();
            val get_title =txt_title.getText().toString();
            val get_stime =txt_stime.getText().toString();
            val get_etime =txt_etime.getText().toString();
            val get_slat =txt_slat.getText().toString();
            val get_slng =txt_slng.getText().toString();
            val get_elat =txt_elat.getText().toString();
            val get_elng =txt_elng.getText().toString();


            val databaseHandler: DatabaseHandler = DatabaseHandler(context)
            val status = databaseHandler.updateTrack(
                TrackModelClassChange(
                    Integer.parseInt(get_id),
                    get_title,
                    get_stime,
                    get_etime,
                    get_slat,
                    get_slng,
                    get_elat,
                    get_elng

                )
            )
            if (status > -1) {
                Toast.makeText(context, "Track update", Toast.LENGTH_LONG).show()
                val i = Intent(context, ListActivity::class.java)
                context.startActivity(i)
            }
        }

        return rowView
    }
    fun getDirectionURL(origin:LatLng,dest:LatLng) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${dest.latitude},${dest.longitude}&sensor=false&mode=driving&key=AIzaSyADM6Tt1skrEQ-934LQWczwE_OoQtSW87A"
    }

    private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body()!!.string()
            Log.d("GoogleMap" , " data : $data")
            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data,GoogleMapDTO::class.java)

                val path =  ArrayList<LatLng>()

                for (i in 0..(respObj.routes[0].legs[0].steps.size-1)){
//                    val startLatLng = LatLng(respObj.routes[0].legs[0].steps[i].start_location.lat.toDouble()
//                            ,respObj.routes[0].legs[0].steps[i].start_location.lng.toDouble())
//                    path.add(startLatLng)
//                    val endLatLng = LatLng(respObj.routes[0].legs[0].steps[i].end_location.lat.toDouble()
//                            ,respObj.routes[0].legs[0].steps[i].end_location.lng.toDouble())
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.BLUE)
                lineoption.geodesic(true)
            }
            googleMap.addPolyline(lineoption)
        }
    }

    public fun decodePolyline(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }

        return poly
    }



}