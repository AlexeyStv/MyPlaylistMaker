package com.practicum.android.playlistmaker.classes

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(private val shPreferences: SharedPreferences) {

    val TRACKS_KEY = "key_tracks_history"

    fun addTrack(newTrack: Track) {
        val historyTracks: MutableList<Track> = getTracksHistory().toMutableList()

        historyTracks.remove(newTrack)
        if (historyTracks.size == 10)
            historyTracks.remove(historyTracks[9])

        historyTracks.add(0, newTrack)

        val arrayTracks: Array<Track> = historyTracks.toTypedArray()
        val json = Gson().toJson(arrayTracks)
        shPreferences.edit()
            .putString(TRACKS_KEY, json)
            .apply()
    }

    fun clearPreferences() {
        val json = Gson().toJson(emptyArray<Track>())
        shPreferences.edit()
            .putString(TRACKS_KEY, json)
            .apply()
    }

    fun getTracksHistory(): Array<Track> {
        val json = shPreferences.getString(TRACKS_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }
}