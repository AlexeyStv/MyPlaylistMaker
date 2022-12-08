package com.practicum.android.playlistmaker.classes

/*
// trackName: String // Название композиции
// artistName: String // Имя исполнителя
// trackTimeMillis: String // Продолжительность трека
// artworkUrl100: String // Ссылка на изображение обложки
*/

//data class Track(val trackName: String, val artistName: String, val trackTime: String, val artworkUrl100: String)

import android.os.Parcel
import android.os.Parcelable

data class Track(val trackName: String?,
                 val artistName: String?,
                 val trackTimeMillis: Long,
                 val artworkUrl100: String?) :
    Parcelable { constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString()) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeLong(trackTimeMillis)
        parcel.writeString(artworkUrl100)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}