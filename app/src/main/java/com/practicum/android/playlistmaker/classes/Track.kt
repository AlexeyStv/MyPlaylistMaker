package com.practicum.android.playlistmaker.classes

import android.os.Parcelable
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
    val artworkUrl100: String?
) : Parcelable