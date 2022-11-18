package com.practicum.android.playlistmaker.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.android.playlistmaker.R

class TracksAdapter(private var tracks: MutableList<Track>)
    : RecyclerView.Adapter<TracksViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_view, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {

        holder.bind(tracks[position])

        holder.itemView.setOnClickListener{
            println(" ---- choose: " + tracks[position].trackName)
        }
    }
    override fun getItemCount(): Int {
        return tracks.size
    }
}