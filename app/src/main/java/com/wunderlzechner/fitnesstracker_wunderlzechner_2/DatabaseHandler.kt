package com.wunderlzechner.fitnesstracker_wunderlzechner_2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.smsprogramerz.sqlitedbkotlin.TrackModelClassChange
import com.smsprogramerz.sqlitedbkotlin.TrackModelClassView
import java.util.ArrayList

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 3
        private val DATABASE_NAME = "trackerdb"
        private val TABLE_CONTACTS = "tracks"

        private val KEY_ID = "id"
        private val KEY_TITLE = "title"
        private val KEY_TIMESTAMP = "timestamp"
        private val KEY_TIMESTAMPEND = "timestampend"
        private val KEY_LATITUDE = "latitude"
        private val KEY_LONGITUDE = "longitude"
        private val KEY_LATITUDEEND = "latitudeend"
        private val KEY_LONGITUDEEND = "longitudeend"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT," + KEY_TIMESTAMP + " LONG," + KEY_TIMESTAMPEND + " LONG," + KEY_LATITUDE + " LONG,"+ KEY_LONGITUDE + " LONG,"+ KEY_LATITUDEEND + " LONG,"+ KEY_LONGITUDEEND + " LONG"+ ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }


    //method to insert data
    fun addTrack(track: TrackModelClassStart):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, track.title)
        contentValues.put(KEY_TIMESTAMP, track.currentTime )
        contentValues.put(KEY_TIMESTAMPEND,"" )
        contentValues.put(KEY_LATITUDE, track.lat)
        contentValues.put(KEY_LONGITUDE,track.lng )
        contentValues.put(KEY_LATITUDEEND, "")
        contentValues.put(KEY_LONGITUDEEND,"" )
        // Inserting Row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    fun updateEndTrack(track: TrackModelClassEnd):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, track.id)
        contentValues.put(KEY_TIMESTAMPEND, track.currentTimeend )
        contentValues.put(KEY_LATITUDEEND, track.latend)
        contentValues.put(KEY_LONGITUDEEND,track.lngend )

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues,"id="+track.id,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    fun updateTrack(track: TrackModelClassChange):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, track.trackId)
        contentValues.put(KEY_TITLE, track.trackTitle)
        contentValues.put(KEY_TIMESTAMP, track.trackStartTime)
        contentValues.put(KEY_TIMESTAMPEND, track.trackEndTime )
        contentValues.put(KEY_LATITUDE, track.tracklat)
        contentValues.put(KEY_LONGITUDE,track.tracklng )
        contentValues.put(KEY_LATITUDEEND, track.tracklatend)
        contentValues.put(KEY_LONGITUDEEND,track.tracklngend )

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues,"id="+track.trackId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    fun viewTrack():List<TrackModelClassView>{
        val trackList:ArrayList<TrackModelClassView> = ArrayList<TrackModelClassView>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var trackId: Int
        var trackTitle: String
        var trackStartTime: String
        var trackEndTime: String
        var tracklat: String
        var tracklng: String
        var tracklatend: String
        var tracklngend: String


        if (cursor.moveToFirst()) {
            do {
                trackId = cursor.getInt(cursor.getColumnIndex("id"))
                trackTitle = cursor.getString(cursor.getColumnIndex("title"))
                trackStartTime = cursor.getString(cursor.getColumnIndex("timestamp"))
                trackEndTime = cursor.getString(cursor.getColumnIndex("timestampend"))
                tracklat = cursor.getString(cursor.getColumnIndex("latitude"))
                tracklng = cursor.getString(cursor.getColumnIndex("longitude"))
                tracklatend = cursor.getString(cursor.getColumnIndex("latitudeend"))
                tracklngend = cursor.getString(cursor.getColumnIndex("longitudeend"))


                val trk= TrackModelClassView(trackId = trackId, trackTitle = trackTitle, trackStartTime = trackStartTime,trackEndTime = trackEndTime, tracklat = tracklat, tracklng = tracklng, tracklatend = tracklatend, tracklngend = tracklngend)
                trackList.add(trk)
            } while (cursor.moveToNext())
        }
        return trackList
    }
    fun ChangeTrack(ids: String):List<TrackModelClassChange>{
        val trackList:ArrayList<TrackModelClassChange> = ArrayList<TrackModelClassChange>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS WHERE $KEY_ID = $ids"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var trackId: Int
        var trackTitle: String
        var trackStartTime: String
        var trackEndTime: String
        var tracklat: String
        var tracklng: String
        var tracklatend: String
        var tracklngend: String


        if (cursor.moveToFirst()) {
            do {
                trackId = cursor.getInt(cursor.getColumnIndex("id"))
                trackTitle = cursor.getString(cursor.getColumnIndex("title"))
                trackStartTime = cursor.getString(cursor.getColumnIndex("timestamp"))
                trackEndTime = cursor.getString(cursor.getColumnIndex("timestampend"))
                tracklat = cursor.getString(cursor.getColumnIndex("latitude"))
                tracklng = cursor.getString(cursor.getColumnIndex("longitude"))
                tracklatend = cursor.getString(cursor.getColumnIndex("latitudeend"))
                tracklngend = cursor.getString(cursor.getColumnIndex("longitudeend"))


                val trkn= TrackModelClassChange(trackId = trackId, trackTitle = trackTitle, trackStartTime = trackStartTime,trackEndTime = trackEndTime, tracklat = tracklat, tracklng = tracklng, tracklatend = tracklatend, tracklngend = tracklngend)
                trackList.add(trkn)
            } while (cursor.moveToNext())
        }
        return trackList
    }
    val allCotacts: ArrayList<String>
        get() {
            val db = this.readableDatabase
            val array_list = ArrayList<String>()
            val res = db.rawQuery("select (id) as fullname from " + TABLE_CONTACTS + " ORDER BY id DESC LIMIT 1", null)
            res!!.moveToFirst()
            while (res.isAfterLast == false) {
                if (res != null && res.count > 0) array_list.add(res.getString(res.getColumnIndex("fullname")))
                res.moveToNext()
            }
            return array_list
        }

}