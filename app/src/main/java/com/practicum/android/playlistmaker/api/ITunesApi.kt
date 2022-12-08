package com.practicum.android.playlistmaker.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    // https://itunes.apple.com
    // @GET ---> /search?entity=song
    // https://itunes.apple.com/search?term=jack+johnson

    @GET("/search?")
    fun search(@Query("term") text: String) : Call<ITunesResponse>
}