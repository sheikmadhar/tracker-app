package com.wunderlzechner.fitnesstracker_wunderlzechner_2

data class Track(val id: Long, val timestamp: Long, var startend: Int, val latitude: Double, val longitude: Double)
// title & message: entfernt.
// startend einfügen (1 = start // 2 = end)