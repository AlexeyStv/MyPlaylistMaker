package com.practicum.android.playlistmaker.classes

import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.parcelize.Parcelize

/*
    trackName: String // Название композиции
    artistName: String // Имя исполнителя
    trackTimeMillis: String // Продолжительность трека
    artworkUrl100: String // Ссылка на изображение обложки
*/

//@Parcelize
@Parcelize
data class Track(
    val trackId: String?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long,
    val artworkUrl100: String?,

    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?
) : Parcelable {
    fun getArtworkUrl512(): String {
        return artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg") ?: ""
    }

    fun getDuration(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    }
}