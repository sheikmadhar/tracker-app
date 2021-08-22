package com.wunderlzechner.fitnesstracker_wunderlzechner_2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        // Database properties
        private const val DATABASE_NAME = "trackerdb"
        private const val DATABASE_TABLE_NAME = "tracks"
        private const val DATABASE_VERSION = 2

        // Database table column names
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_TIMESTAMP = "timestamp"
        private const val KEY_TIMESTAMPEND = "timestampend"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
        private const val KEY_LATITUDEEND = "latitudeend"
        private const val KEY_LONGITUDEEND = "longitudeend"

        // Database create table statement
        private const val CREATE_TABLE = ("""CREATE TABLE $DATABASE_TABLE_NAME(
                $KEY_ID INTEGER PRIMARY KEY,
                $KEY_TITLE STRING,
                $KEY_TIMESTAMP INT,
                $KEY_TIMESTAMPEND INT,
                $KEY_LATITUDE FLOAT,
                $KEY_LONGITUDE FLOAT,
                $KEY_LATITUDEEND FLOAT,
                $KEY_LONGITUDEEND FLOAT
            )""")

        // Database cursor array
        private val CURSOR_ARRAY = arrayOf(
                KEY_ID,
                KEY_TITLE,
                KEY_TIMESTAMP,
                KEY_LATITUDE,
                KEY_LONGITUDE,
                KEY_LATITUDEEND,
                KEY_LONGITUDEEND
        )

        // Drop table statement
        private const val DROP_TABLE = "DROP TABLE IF EXISTS $DATABASE_TABLE_NAME"

        // Database select all statement
        private const val SELECT_ALL = "SELECT * FROM $DATABASE_TABLE_NAME"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL(DROP_TABLE)
        db.execSQL(CREATE_TABLE)
    }

    // Get all tracks from database
    fun getAllTracks(): List<Track> {
        val track = ArrayList<Track>()
        val cursor = readableDatabase.rawQuery(SELECT_ALL, null)
        cursor.moveToFirst().run {
            do {
                cursorToTrack(cursor)?.let {
                    track.add(it)
                }
            } while (cursor.moveToNext())
        }

        readableDatabase.close()

        return track
    }

    // Insert Track into database
    fun insertTrack(track: Track): Long {
        val values = trackToContentValues(track)

        return writableDatabase.insert(DATABASE_TABLE_NAME, null, values)
    }

    // Get single Track from database
    fun getTrack(id: Long): Track? {
        val track: Track?
        val cursor = readableDatabase.query(
                DATABASE_TABLE_NAME, CURSOR_ARRAY, "$KEY_ID=?",
                arrayOf(id.toString()), null, null, null, null
        )

        cursor.moveToFirst()
        track = cursorToTrack(cursor)
        cursor.close()

        return track
    }

    private fun cursorToTrack(cursor: Cursor):Track? {
        var track: Track? = null
        if (cursor?.count == 0) return null
        cursor.run {
            track = Track(
                    getLong(getColumnIndex(KEY_ID)),
                    getString(getColumnIndex(KEY_TITLE)),
                    getLong(getColumnIndex(KEY_TIMESTAMP)),
                    getLong(getColumnIndex(KEY_TIMESTAMPEND)),
                    getDouble(getColumnIndex(KEY_LATITUDE)),
                    getDouble(getColumnIndex(KEY_LONGITUDE)),
                    getDouble(getColumnIndex(KEY_LATITUDEEND)),
                    getDouble(getColumnIndex(KEY_LONGITUDEEND))
            )
        }

        return track
    }

    // Update single Track
    fun updateTrack(track: Track): Int {
        return writableDatabase.update(DATABASE_TABLE_NAME,
                trackToContentValues(track),
                "$KEY_ID=?",
                arrayOf(track.id.toString()))
    }

    // Create new ContentValues object from Track
    // ERRORS existing, please fix.
    private fun trackToContentValues(track: Track): ContentValues {
        val values = ContentValues()
        values.put(KEY_TITLE, track.title)
        values.put(KEY_TIMESTAMP, track.timestamp)
        values.put(KEY_TIMESTAMPEND, track.timestampend)
        values.put(KEY_LATITUDE, track.latitude)
        values.put(KEY_LONGITUDE, track.longitude)
        values.put(KEY_LATITUDEEND, track.latitudeend)
        values.put(KEY_LONGITUDEEND, track.longitudeend)

        return values
    }

    // Delete Track
    fun deleteTrack(track: Track) {
        writableDatabase.delete(
                DATABASE_TABLE_NAME,
                "$KEY_ID=?",
                arrayOf(track.id.toString())
        )
    }
}