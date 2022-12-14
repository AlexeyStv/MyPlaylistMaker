package com.practicum.android.playlistmaker.classes

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.android.playlistmaker.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale


class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ivArtistNameTime: ImageView = itemView.findViewById(R.id.ivAlbumPicture)
    private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
    private val tvArtistNameTime: TextView = itemView.findViewById(R.id.tvMusicBand)

    fun bind(model: Track) {
        tvTrackName.text = model.trackName
        val timerTrack =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
        val nameTime = model.artistName.plus("  ·  ").plus(timerTrack) //.plus("  ·  ").plus(model.trackId)
        tvArtistNameTime.text = nameTime
        val imageUrl = model.artworkUrl100

        Glide.with(itemView)
            .load(imageUrl)
            .placeholder(R.drawable.ic_no_image)
            .fitCenter()
            .transform(RoundedCorners(2))
            .into(ivArtistNameTime)
    }
}