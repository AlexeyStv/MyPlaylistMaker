package com.practicum.android.playlistmaker.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    
    //возвращает не всегда корректные данные в объект Track --> trackId = null или trackName = ""
    //@GET("/search?entity")
    //fun search(@Query("term") song: String): Call<ITunesResponse>

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ITunesResponse>
}